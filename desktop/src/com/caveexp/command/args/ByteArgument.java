package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;

public class ByteArgument extends CommandArgument<Byte> {
    public ByteArgument(String name) {
        super(name);
    }
    public Byte parse(String[] string, AccessCounter counter) {
        try {
            return Byte.parseByte(string[counter.access()]);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
