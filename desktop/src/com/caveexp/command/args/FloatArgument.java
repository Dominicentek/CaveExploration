package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class FloatArgument extends CommandArgument<Float> {
    public FloatArgument(String name) {
        super(name);
    }
    public Float parse(String[] string, AccessCounter counter) {
        try {
            return Float.parseFloat(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
