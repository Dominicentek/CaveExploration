package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class LongArgument extends CommandArgument<Long> {
    public LongArgument(String name) {
        super(name);
    }
    public Long parse(String[] string, AccessCounter counter) {
        try {
            return Long.parseLong(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
