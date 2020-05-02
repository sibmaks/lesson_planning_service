package xyz.dma.soft.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "child_info")
@AllArgsConstructor
@NoArgsConstructor
public class ChildInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "child_info_id_gen")
    @SequenceGenerator(name = "child_info_id_gen", sequenceName = "child_info_id_seq")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "parent_name")
    private String parentName;
    @Column(name = "phone")
    private String phone;
}
