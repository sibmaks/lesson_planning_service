package xyz.dma.soft.api.response.chold;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.ChildInfo;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChildAddResponse extends StandardResponse {
    private ChildInfo childInfo;
    private List<CourseSchedulingInfo> courseSchedulingInfos;
}
