package xyz.dma.soft.storage;

import lombok.extern.slf4j.Slf4j;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.springframework.stereotype.Service;
import xyz.dma.soft.conf.SessionInfoStorageConfig;
import xyz.dma.soft.entity.SessionInfo;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class SessionStorage {
    private final static String SESSION_CACHE_NAME = "sessionsCache";

    private DB dbDisk;
    private HTreeMap<String, SessionInfo> persistentMap;
    private final SessionInfoStorageConfig config;
    private final SessionInfoSerializer sessionInfoSerializer;

    public SessionStorage(SessionInfoStorageConfig config, SessionInfoSerializer sessionInfoSerializer) {
        this.config = config;
        this.sessionInfoSerializer = sessionInfoSerializer;
    }

    @PostConstruct
    public void open() {
        try {
            Files.createDirectories(Paths.get(config.getCachePath()));
            dbDisk = DBMaker
                    .fileDB(config.getCachePath() + SESSION_CACHE_NAME)
                    .closeOnJvmShutdown()
                    .checksumHeaderBypass()
                    .transactionEnable()
                    .make();
            persistentMap = dbDisk
                    .hashMap(SESSION_CACHE_NAME + "Disk", Serializer.STRING, sessionInfoSerializer)
                    .expireMaxSize(config.getCacheDiskSize())
                    .expireAfterGet(config.getRemoveTimeout(), TimeUnit.SECONDS)
                    .expireAfterCreate(config.getRemoveTimeout(), TimeUnit.SECONDS)
                    .expireAfterUpdate(config.getRemoveTimeout(), TimeUnit.SECONDS)
                    .expireExecutor(Executors.newScheduledThreadPool(config.getEvictionThreads()))
                    .expireExecutorPeriod(config.getEvictionTimeout())
                    .createOrOpen();
            log.info("*** MapDB session cache is initialized");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void put(String sessionId, SessionInfo sessionInfo) {
        persistentMap.put(sessionId, sessionInfo);
        dbDisk.commit();
    }

    public SessionInfo get(String sessionId) {
        return persistentMap.get(sessionId);
    }

    public void remove(String sessionId) {
        persistentMap.remove(sessionId);
        dbDisk.commit();
    }

    public boolean contains(String sessionId) {
        return persistentMap.containsKey(sessionId);
    }

    public List<SessionInfo> getAll() {
        return new ArrayList<>(persistentMap.values());
    }

    public List<SessionInfo> get(Predicate<SessionInfo> predicate) {
        return persistentMap.values().stream().filter(predicate).collect(toList());
    }

    public void close() {
        try {
            persistentMap.clearWithExpire();
            dbDisk.commit();
            dbDisk.getStore().compact();
            log.info("*** MapDB session cache is disposed");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("*** MapDB session cache is disposed with errors");
        } finally {
            persistentMap.close();
            dbDisk.close();
        }
    }
}