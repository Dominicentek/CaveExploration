package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class IntegerArgument extends CommandArgument<Integer> {
    public IntegerArgument(String name) {
        super(name);
    }
    public Integer parse(String[] string, AccessCounter counter) {
        try {
            return Integer.parseInt(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
