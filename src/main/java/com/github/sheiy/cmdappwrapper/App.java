package com.github.sheiy.cmdappwrapper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;


public class App extends Application {

    private ConfigurableApplicationContext applicationContext;
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(CmdAppWrapperApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        threadPoolExecutor = applicationContext.getBean(ThreadPoolExecutor.class);
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        threadPoolExecutor.shutdownNow();
        applicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return ((Stage) getSource());
        }
    }
}
