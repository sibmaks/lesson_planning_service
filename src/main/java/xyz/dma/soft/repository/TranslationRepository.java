package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.i18n.Translation;

import java.util.List;

public interface TranslationRepository extends CrudRepository<Translation, Long> {
    List<Translation> findAllBy();
}
