package com.pa.modules.dam.model;

import com.pa.modules.dam.repository.ChartDto;
import lombok.Data;

import java.util.List;

@Data
public class DynamicCharts {
    private String chartTitle;
    private List<ChartDto> data;
    private int position;
}
