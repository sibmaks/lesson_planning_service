package xyz.dma.soft.domain.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "translation")
public class Translation implements Serializable {
    @EmbeddedId
    private TranslationId translationId;
    @Column(name = "translation")
    private String translation;
    @Column(name = "update_date_time")
    private LocalDateTime updateDateTime;
    @ManyToOne
    private User editor;
}
