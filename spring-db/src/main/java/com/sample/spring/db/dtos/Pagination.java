package com.sample.spring.db.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Pagination<T> {

    private List<T> data;
    private long total;
}
