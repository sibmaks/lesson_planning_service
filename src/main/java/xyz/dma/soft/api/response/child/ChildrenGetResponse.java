package xyz.dma.soft.api.response.child;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.ChildCourseSchedulingInfo;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChildrenGetResponse extends StandardResponse {
    private List<ChildCourseSchedulingInfo> childCourseSchedulingInfos;
}
