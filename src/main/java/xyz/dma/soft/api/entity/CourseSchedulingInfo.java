package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class    CourseSchedulingInfo implements Serializable {
    private Long id;
    private CourseInfo courseInfo;
    private int dayOfWeek;
    private String timeStart;
    private String timeEnd;
}
