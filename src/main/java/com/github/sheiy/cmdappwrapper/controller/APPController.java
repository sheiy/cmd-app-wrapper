package com.github.sheiy.cmdappwrapper.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import org.springframework.stereotype.Component;

@Component
public class APPController {
    @FXML
    public LineChart<String, Double> chart;

    
}
