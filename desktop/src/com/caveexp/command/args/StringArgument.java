package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class StringArgument extends CommandArgument<String> {
    public StringArgument(String name) {
        super(name);
    }
    public String parse(String[] string, AccessCounter counter) {
        try {
            return string[counter.access()];
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
