package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class BooleanArgument extends CommandArgument<Boolean> {
    public BooleanArgument(String name) {
        super(name);
    }
    public Boolean parse(String[] string, AccessCounter counter) {
        try {
            return Boolean.parseBoolean(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
