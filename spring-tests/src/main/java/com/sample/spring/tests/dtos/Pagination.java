package com.sample.spring.tests.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Pagination<T> {

    private List<T> data;
    private long total;
}
