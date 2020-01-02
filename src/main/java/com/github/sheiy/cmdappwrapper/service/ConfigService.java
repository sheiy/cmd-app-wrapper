package com.github.sheiy.cmdappwrapper.service;

import java.io.IOException;

public interface ConfigService {
    void set(String key, Object value) throws IOException;

    <T> T get(String key, Class<T> tClass) throws IOException;
}
