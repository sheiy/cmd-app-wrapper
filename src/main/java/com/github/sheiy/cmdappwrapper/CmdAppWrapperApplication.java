package com.github.sheiy.cmdappwrapper;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sheiy
 */
@SpringBootApplication
public class CmdAppWrapperApplication {

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }

}
