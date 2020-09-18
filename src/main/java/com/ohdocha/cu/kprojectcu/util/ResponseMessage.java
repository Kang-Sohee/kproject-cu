package com.ohdocha.cu.kprojectcu.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage {

    private boolean success;
    private String message;
    private DataMessage results;

}
