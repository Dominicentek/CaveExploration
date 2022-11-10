package com.caveexp.command;

public interface CommandExecution extends CommandNode {
    void run(CommandContext context);
}
