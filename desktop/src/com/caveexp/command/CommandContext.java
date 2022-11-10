package com.caveexp.command;

import java.util.HashMap;
import java.util.Objects;

public class CommandContext {
    public HashMap<String, Object> values = new HashMap<>();
    public <T> T get(String name) {
        return (T)values.get(name);
    }
}
