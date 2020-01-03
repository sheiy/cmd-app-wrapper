package com.github.sheiy.cmdappwrapper;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<App.StageReadyEvent> {

    @Value("classpath:/fxml/app.fxml")
    private Resource appResource;
    @Value("classpath:/ico.png")
    private Resource icon;
    private String applicationTitle;
    private ApplicationContext applicationContext;
    private Stage mainStage;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle,
                            ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(App.StageReadyEvent event) {
        try {
            initSystemTray();
            initMainStage(event.getStage());
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private void initMainStage(Stage mainStage) throws IOException {
        Platform.setImplicitExit(false);
        this.mainStage = mainStage;
        final FXMLLoader fxmlLoader = new FXMLLoader(appResource.getURL());
        fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));
        Parent parent = fxmlLoader.load();
        mainStage.setScene(new Scene(parent, 800, 600));
        mainStage.setTitle(applicationTitle);
        mainStage.show();
    }

    private void initSystemTray() throws IOException, AWTException {
        java.awt.SystemTray tray = SystemTray.getSystemTray();
        BufferedImage image = ImageIO.read(icon.getFile());
        final TrayIcon trayIcon = new TrayIcon(image, "CmdAppWrapper");
        trayIcon.setImageAutoSize(true);
        final PopupMenu popupMenu = new PopupMenu();
        final MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> {
            tray.remove(trayIcon);
            App.shutdown();
        });
        popupMenu.add(exit);
        trayIcon.setPopupMenu(popupMenu);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    if (mainStage.isShowing()) {
                        Platform.runLater(() -> mainStage.hide());
                    } else {
                        Platform.runLater(() -> mainStage.show());
                    }
                }
            }
        });
        tray.add(trayIcon);
    }
}
