package xyz.dma.soft.storage;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.springframework.stereotype.Component;
import xyz.dma.soft.entity.SessionInfo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Slf4j
@Component
public class SessionInfoSerializer implements Serializer<SessionInfo> {
    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull SessionInfo value) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(out)){
            objectOutputStream.writeObject(value);
        }
    }

    @Override
    public SessionInfo deserialize(@NotNull DataInput2 input, int available) throws IOException {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.internalByteArray());
                ObjectInputStream objectOutputStream = new ObjectInputStream(byteArrayInputStream)){
            return (SessionInfo) objectOutputStream.readObject();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
