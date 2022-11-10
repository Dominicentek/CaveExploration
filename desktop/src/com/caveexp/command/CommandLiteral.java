package com.caveexp.command;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandLiteral implements CommandNode {
    public HashMap<String, ArrayList<CommandNode>> paths = new HashMap<>();
    public CommandLiteral addPath(String literal, ArrayList<CommandNode> nodes) {
        paths.put(literal, nodes);
        return this;
    }
}
