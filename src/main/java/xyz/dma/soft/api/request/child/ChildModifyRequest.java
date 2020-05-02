package xyz.dma.soft.api.request.child;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.ChildInfo;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.StandardRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChildModifyRequest extends StandardRequest {
    private ChildInfo childInfo;
    private List<CourseSchedulingInfo> courseSchedulingInfos;
}
