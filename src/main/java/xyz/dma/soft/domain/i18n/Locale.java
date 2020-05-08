package xyz.dma.soft.domain.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locale")
public class Locale implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "locale_id_gen")
    @SequenceGenerator(name = "locale_id_gen", sequenceName = "locale_id_seq")
    private Long id;
    @Column(name = "country_iso3")
    private String countryIso3;
    @Column(name = "language_iso3")
    private String languageIso3;
}
