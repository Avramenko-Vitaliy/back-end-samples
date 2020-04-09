package com.sample.spring.kafka.controllers.params;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Params {

    private int offset = 0;
    private int limit = 15;

    private String sort;

    private String search;

    @JsonProperty("asc")
    private boolean asc = true;
}
