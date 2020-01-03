package com.github.sheiy.cmdappwrapper.controller;

import com.github.sheiy.cmdappwrapper.service.ConfigService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppController {

    private final ConfigService configService;
    private final ThreadPoolExecutor threadPoolExecutor;

    @FXML
    public Button addCmdApp;
    @FXML
    public CheckBox setAutoStart;
    @FXML
    public TabPane appPane;

    @FXML
    public void initialize() {
        addCmdApp.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("可执行程序", "*.exe"));
            File exeFile = fileChooser.showOpenDialog(((Button) e.getSource()).getScene().getWindow());
            if (exeFile != null) {
                try {
                    List cmdApps = configService.get("cmdApps", List.class);
                    if (cmdApps == null) {
                        cmdApps = new ArrayList(1);
                    }
                    cmdApps.add(exeFile.getPath());
                    configService.set("cmdApps", cmdApps);
                } catch (IOException ex) {
                    log.error("添加App失败", ex);
                    new Alert(Alert.AlertType.ERROR, "保存失败").showAndWait();
                }
            }
        });
        try {
            List cmdApps = configService.get("cmdApps", List.class);
            for (Object cmdApp : cmdApps) {
                Tab tab = new Tab(cmdApp.toString());
                TextArea textArea = new TextArea(cmdApp.toString());
                textArea.setEditable(false);
                tab.setContent(textArea);
                final Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        final File file = new File(cmdApp.toString());
                        final ProcessBuilder processBuilder = new ProcessBuilder(cmdApp.toString())
                                .redirectErrorStream(true)
                                .directory(new File(file.getParent()));
                        Process process = processBuilder.start();
                        try (final InputStream inputStream = process.getInputStream();
                             final BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(inputStream))) {
                            String in;
                            while (true) {
                                in = inputStreamReader.readLine();
                                if (in != null && !in.isEmpty()) {
                                    updateMessage(textArea.getText() + in + System.lineSeparator());
                                }
                            }
                        }
                    }
                };
                task.messageProperty().addListener((observable, oldValue, newValue) -> {
                    textArea.selectPositionCaret(textArea.getLength());
                    textArea.deselect();
                    textArea.setScrollTop(Double.MAX_VALUE);
                });
                textArea.textProperty().bind(task.messageProperty());
                appPane.getTabs().add(tab);
                threadPoolExecutor.submit(task);
            }
        } catch (IOException ex) {
            log.error("配置文件有误", ex);
            new Alert(Alert.AlertType.ERROR, "配置文件有误").showAndWait();
            Platform.exit();
        }
    }
}
