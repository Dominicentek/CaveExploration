package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class DoubleArgument extends CommandArgument<Double> {
    public DoubleArgument(String name) {
        super(name);
    }
    public Double parse(String[] string, AccessCounter counter) {
        try {
            return Double.parseDouble(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
