open module app {
    requires static org.mapstruct.processor;

    requires lombok;

    requires org.slf4j;

    requires com.fasterxml.jackson.databind;

    requires javafx.controls;
    requires javafx.fxml;

    requires spring.boot;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires spring.beans;
    requires spring.core;

    exports com.github.sheiy.cmdappwrapper;
}
