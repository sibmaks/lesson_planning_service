package xyz.dma.soft.api.response.child;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.ChildInfoEntity;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChildUpdateResponse extends StandardResponse {
    private ChildInfoEntity childInfo;
    private List<CourseSchedulingInfo> courseSchedulingInfos;
}
