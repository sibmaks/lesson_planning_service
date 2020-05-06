package xyz.dma.soft.api.request.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.request.StandardRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LessonsGetRequest extends StandardRequest {
    private Long courseId;
    private String fromDate;
    private String toDate;
}
