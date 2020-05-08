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
@Table(name = "localization")
public class Localization implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "localization_id_gen")
    @SequenceGenerator(name = "localization_id_gen", sequenceName = "localization_id_seq")
    private Long id;
    @Column(name = "code")
    private String code;
}
