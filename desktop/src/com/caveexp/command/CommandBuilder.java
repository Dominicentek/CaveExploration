package com.caveexp.command;

import java.util.ArrayList;

public class CommandBuilder {
    public ArrayList<CommandNode> nodes = new ArrayList<>();
    public CommandBuilder addNode(CommandNode argument) {
        nodes.add(argument);
        return this;
    }
    public ArrayList<CommandNode> get() {
        return nodes;
    }
}
