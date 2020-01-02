package com.github.sheiy.cmdappwrapper.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppController {
    @FXML
    public Button addCmdApp;
    @FXML
    public CheckBox setAutoStart;
    @FXML
    public TabPane appPane;

    @FXML
    public void initialize() {
       addCmdApp.setOnMouseClicked(e->{
           System.out.println(e);
       });
    }


}
