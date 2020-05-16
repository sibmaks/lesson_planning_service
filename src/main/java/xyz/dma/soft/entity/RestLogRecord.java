package xyz.dma.soft.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestLogRecord implements Serializable {
    private String id;
    private String address;
    private int status;
    private long timer;
    private String request;
    private Map<String, String> headers;
    private String payload;
    private boolean incomeDirection;

    public String toSimpleFormat() {
        if(incomeDirection) {
            return toRequestSimpleFormat();
        } else {
            return toResponseSimpleFormat();
        }
    }

    private String toRequestSimpleFormat() {
       return new StringBuilder()
                .append("\n----- Income message (").append(id).append(") -----\n")
                .append("Address: ").append(address).append("\n")
                .append("Headers: \n").append(getHeadersKeyValue()).append("\n")
                .append("Request: ").append(request)
               .toString();
    }

    private String toResponseSimpleFormat() {
        return new StringBuilder()
                .append("\n----- Output message (").append(id).append(") -----\n")
                .append("Address: ").append(address).append("\n")
                .append("Status: ").append(status).append("\n")
                .append("Headers: \n").append(getHeadersKeyValue()).append("\n")
                .append("Timer: ").append(timer).append("ms\n")
                .append("Request: ").append(request).append("\n")
                .append("Payload: ").append(payload)
                .toString();
    }

    private String getHeadersKeyValue() {
        if(headers != null) {
            return headers.entrySet().stream()
                    .map(it -> String.format("%s=%s", it.getKey(), it.getValue()))
                    .collect(Collectors.joining("\n"));
        }
        return "";
    }
}
