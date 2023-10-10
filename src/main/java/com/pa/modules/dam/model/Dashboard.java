package com.pa.modules.dam.model;

import lombok.Data;

import java.util.Map;

@Data
public class Dashboard {

    private Long countOfTicket;
    private Long countOfDamdari;
    private Long countOfDam;
    private Long countOfDamWithTab;
    private Long countOfDamIsFahli;
    private Long countOfDamHasLangesh;
    private Long avgOfMilk;
 //   private Map<String, List<ChartDto>> charts;

    private Map<String, DynamicCharts> charts;
}
