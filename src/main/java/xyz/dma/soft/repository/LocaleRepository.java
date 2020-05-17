package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.i18n.Locale;

import java.util.List;

public interface LocaleRepository extends CrudRepository<Locale, Long> {
    List<Locale> findAllBy();
}
