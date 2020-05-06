package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.domain.ChildInfo;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildInfoEntity implements Serializable {
    private Long id;
    private String name;
    private String parentName;
    private String phone;

    public ChildInfoEntity(ChildInfo childInfo) {
        this.id = childInfo.getId();
        this.name = childInfo.getName();
        this.parentName = childInfo.getParentName();
        this.phone = childInfo.getPhone();
    }
}
