package com.github.sheiy.cmdappwrapper.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sheiy.cmdappwrapper.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private static final String RUN_DIR = System.getProperty("user.dir");

    private final ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value) throws IOException {
        File file = Path.of(RUN_DIR, "app.json").toFile();
        Map map;
        if (file.exists()) {
            map = objectMapper.readValue(file, Map.class);
        } else {
            map = new HashMap(30);
        }
        map.put(key, value);
        objectMapper.writeValue(file, map);
    }

    @Override
    public <T> T get(String key, Class<T> tClass) throws IOException {
        File file = Path.of(RUN_DIR, "app.json").toFile();
        Map map;
        if (file.exists()) {
            map = objectMapper.readValue(file, Map.class);
        } else {
            map = new HashMap(30);
        }
        if (map.containsKey(key)) {
            return (T) map.get(key);
        } else {
            return null;
        }
    }
}
