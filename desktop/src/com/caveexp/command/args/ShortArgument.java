package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class ShortArgument extends CommandArgument<Short> {
    public ShortArgument(String name) {
        super(name);
    }
    public Short parse(String[] string, AccessCounter counter) {
        try {
            return Short.parseShort(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
