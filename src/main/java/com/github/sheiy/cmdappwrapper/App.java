package com.github.sheiy.cmdappwrapper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.ThreadPoolExecutor;


public class App extends Application {

    private static ConfigurableApplicationContext applicationContext;
    private ThreadPoolExecutor threadPoolExecutor;
    private static App self;

    @Override
    public void init() {
        self=this;
        applicationContext = new SpringApplicationBuilder(CmdAppWrapperApplication.class)
                .headless(false).web(WebApplicationType.NONE).run();
    }

    @Override
    public void start(Stage stage) {
        threadPoolExecutor = applicationContext.getBean(ThreadPoolExecutor.class);
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        Platform.setImplicitExit(true);
        applicationContext.close();
        Platform.exit();
    }

    public static void shutdown() {
        self.stop();
    }

    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return ((Stage) getSource());
        }
    }

    static class ShutdownEvent extends ApplicationEvent {
        public ShutdownEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return ((Stage) getSource());
        }
    }
}
