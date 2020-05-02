package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseInfo implements Serializable {
    private Long id;
    private String name;
    private UserInfo author;
}
