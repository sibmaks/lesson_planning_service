package xyz.dma.soft.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_info")
public class AuthInfo implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "last_auth_date")
    private LocalDateTime lastAuthDate;

    @Column(name = "last_auth_ip")
    private String lastAuthIp;
}
