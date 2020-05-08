package xyz.dma.soft.api.response.scheduling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.ChildSchedulingEntity;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SchedulingGetResponse extends StandardResponse {
    private Map<Integer, List<ChildSchedulingEntity>> dayOfWeekScheduling;
}
