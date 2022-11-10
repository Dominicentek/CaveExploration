package com.caveexp.command.args;

import com.caveexp.command.AccessCounter;
import com.caveexp.command.CommandNode;

public abstract class CommandArgument<T> implements CommandNode {
    public String name;
    public CommandArgument(String name) {
        this.name = name;
    }
    public abstract T parse(String[] string, AccessCounter counter);
}
