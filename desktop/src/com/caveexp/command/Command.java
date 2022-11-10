package com.caveexp.command;

import com.caveexp.Main;
import com.caveexp.command.args.*;
import com.caveexp.game.Game;
import com.caveexp.game.achievements.Achievement;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.game.region.Region;
import com.caveexp.game.region.RegionGenerator;
import com.caveexp.game.region.Tile;
import com.caveexp.game.region.TileItem;
import com.caveexp.util.ArrayUtils;
import com.caveexp.util.RNG;
import com.caveexp.util.Registry;

import java.util.ArrayList;
import java.util.Collections;

public class Command {
    public static CommandLiteral commands = new CommandLiteral();
    public static void run(String command) {
        try {
            command = command.trim();
            while (command.contains("  ")) {
                command = command.replaceAll("/\\s+/g", " ");
            }
            if (command.isEmpty()) return;
            String[] args = command.split(" ");
            run(new CommandContext(), new ArrayList<>(Collections.singletonList(commands)), args, 0);
            Game.grantAchievement(Registry.ACHIEVEMENTS.get("cheater"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void run(CommandContext commandContext, ArrayList<CommandNode> nodes, String[] args, int i) {
        for (CommandNode node : nodes) {
            if (i >= args.length) {
                if (node instanceof CommandExecution) {
                    CommandExecution execution = (CommandExecution)node;
                    execution.run(commandContext);
                }
                else System.err.println("Insufficient arguments");
                return;

            }
            if (node instanceof CommandLiteral) {
                CommandLiteral literal = (CommandLiteral)node;
                if (!literal.paths.containsKey(args[i])) {
                    System.err.println("Literal not found: " + args[i]);
                    return;
                }
                run(commandContext, literal.paths.get(args[i]), args, i + 1);
                return;
            }
            if (node instanceof CommandArgument) {
                CommandArgument<?> argument = (CommandArgument<?>)node;
                AccessCounter counter = new AccessCounter(i);
                commandContext.values.put(argument.name, argument.parse(args, counter));
                i = counter.access();
            }
        }
    }
    public static void loadCommands() {
        commands.addPath("inventory", new CommandBuilder().addNode(
            new CommandLiteral().addPath("add", new CommandBuilder().addNode(
                new ListArgument("item", Registry.ITEMS.ids())
            ).addNode(
                (CommandExecution)context -> {
                    Game.inventory.addItem(new InventoryItem(Registry.ITEMS.get(context.<Integer>get("item"))));
                }
            ).addNode(
                new IntegerArgument("count")
            ).addNode(
                (CommandExecution)context -> {
                    Game.inventory.addItem(new InventoryItem(Registry.ITEMS.get(context.<Integer>get("item")), context.get("count")));
                }
            ).get())
        ).addNode(
            new CommandLiteral().addPath("remove", new CommandBuilder().addNode(
                new ListArgument("item", Registry.ITEMS.ids())
            ).addNode(
                (CommandExecution)context -> {
                    Game.inventory.removeItem(new InventoryItem(Registry.ITEMS.get(context.<Integer>get("item"))));
                }
            ).addNode(
                new IntegerArgument("count")
            ).addNode(
                (CommandExecution)context -> {
                    Game.inventory.removeItem(new InventoryItem(Registry.ITEMS.get(context.<Integer>get("item")), context.get("count")));
                }
            ).get())
        ).addNode(
            new CommandLiteral().addPath("replace", new CommandBuilder().addNode(
                new IntegerArgument("slot")
            ).addNode(
                new ListArgument("item", Registry.ITEMS.ids())
            ).addNode(
                (CommandExecution)context -> {
                    Game.inventory.putItem(new InventoryItem(Registry.ITEMS.get(context.<Integer>get("item"))), context.get("slot"));
                }
            ).addNode(
                new IntegerArgument("count")
            ).addNode(
                (CommandExecution)context -> {
                    Game.inventory.putItem(new InventoryItem(Registry.ITEMS.get(context.<Integer>get("item")), context.get("count")), context.get("slot"));
                }
            ).get())
        ).get());
        commands.addPath("achievement", new CommandBuilder().addNode(
            new CommandLiteral().addPath("add", new CommandBuilder().addNode(
                new ListArgument("achievement", ArrayUtils.prepend("everything", Registry.ACHIEVEMENTS.ids()))
            ).addNode(
                (CommandExecution)context -> {
                    int id = context.get("achievement");
                    if (id == 0) {
                        for (Achievement achievement : Registry.ACHIEVEMENTS) {
                            Game.grantAchievement(achievement);
                        }
                    }
                    else Game.grantAchievement(Registry.ACHIEVEMENTS.get(id));
                }
            ).get()).addPath("remove", new CommandBuilder().addNode(
                new ListArgument("achievement", ArrayUtils.prepend("everything", Registry.ACHIEVEMENTS.ids()))
            ).addNode(
                (CommandExecution)context -> {
                    int id = context.get("achievement");
                    if (id == 0) {
                        for (Achievement achievement : Registry.ACHIEVEMENTS) {
                            Game.revokeAchievement(achievement);
                        }
                    }
                    else Game.revokeAchievement(Registry.ACHIEVEMENTS.get(id));
                }
            ).get())
        ).get());
        commands.addPath("teleport", new CommandBuilder().addNode(
            new DoubleArgument("x")
        ).addNode(
            new DoubleArgument("y")
        ).addNode(
            (CommandExecution)context -> {
                Game.currentRegion.playerX = context.get("x");
                Game.currentRegion.playerY = context.get("y");
            }
        ).addNode(
            new IntegerArgument("region")
        ).addNode(
            (CommandExecution)context -> {
                int region = context.get("region");
                if (region != Game.currentRegion.id) Game.grantAchievement(Registry.ACHIEVEMENTS.get("region_travel"));
                if (region == 2) Game.grantAchievement(Registry.ACHIEVEMENTS.get("dark"));
                Game.currentRegion = Region.regions[region];
                Game.currentRegion.playerX = context.get("x");
                Game.currentRegion.playerY = context.get("y");
            }
        ).get());
        commands.addPath("ore", new CommandBuilder().addNode(
            new IntegerArgument("x")
        ).addNode(
            new IntegerArgument("y")
        ).addNode(
            new ListArgument("type", Registry.ORES.ids())
        ).addNode(
            (CommandExecution)context -> {
                Game.currentRegion.tiles[context.<Integer>get("x")][context.<Integer>get("y")].ore = Registry.ORES.get(context.get("type"));
            }
        ).get());
        commands.addPath("tileitem", new CommandBuilder().addNode(
            new IntegerArgument("x")
        ).addNode(
            new IntegerArgument("y")
        ).addNode(
            new ListArgument("item", ArrayUtils.enumNames(TileItem.class))
        ).addNode(
            (CommandExecution)context -> {
                Game.currentRegion.tiles[context.<Integer>get("x")][context.<Integer>get("y")].ore = Registry.ORES.get(context.get("type"));
            }
        ).get());
        commands.addPath("tileprop", new CommandBuilder().addNode(
            new IntegerArgument("x")
        ).addNode(
            new IntegerArgument("y")
        ).addNode(
            new BooleanArgument("light")
        ).addNode(
            new BooleanArgument("air")
        ).addNode(
            (CommandExecution)context -> {
                Tile tile = Game.currentRegion.tiles[context.<Integer>get("x")][context.<Integer>get("y")];
                tile.light = context.get("light");
                tile.air = context.get("air");
            }
        ).get());
        commands.addPath("health", new CommandBuilder().addNode(
            new CommandLiteral().addPath("set", new CommandBuilder().addNode(
                new IntegerArgument("amount")
            ).addNode(
                (CommandExecution)context -> {
                    Game.health = context.get("amount");
                }
            ).get()).addPath("add", new CommandBuilder().addNode(
                new IntegerArgument("amount")
            ).addNode(
                (CommandExecution)context -> {
                    Game.health += context.<Integer>get("amount");
                }
            ).get())
        ).get());
        commands.addPath("spiders", new CommandBuilder().addNode(
            new CommandLiteral().addPath("set", new CommandBuilder().addNode(
                new IntegerArgument("amount")
            ).addNode(
                (CommandExecution)context -> {
                    Game.spidersKilled = context.get("amount");
                }
            ).get()).addPath("add", new CommandBuilder().addNode(
                new IntegerArgument("amount")
            ).addNode(
                (CommandExecution)context -> {
                    Game.spidersKilled += context.<Integer>get("amount");
                }
            ).get())
        ).get());
        commands.addPath("mode", new CommandBuilder().addNode(
            (CommandExecution)context -> {
                Game.sandboxMode = !Game.sandboxMode;
            }
        ).get());
        commands.addPath("die", new CommandBuilder().addNode(
            (CommandExecution)context -> {
                Game.health = 0;
            }
        ).get());
        commands.addPath("regenerate", new CommandBuilder().addNode(
            new IntegerArgument("region")
        ).addNode(
            (CommandExecution)context -> {
                int region = context.<Integer>get("region");
                RNG.setSeed(RegionGenerator.seed);
                Region.regions[region] = RegionGenerator.generateRegion(region);
            }
        ).addNode(
            new LongArgument("seed")
        ).addNode(
            (CommandExecution)context -> {
                int region = context.<Integer>get("region");
                RNG.setSeed(context.<Long>get("seed"));
                Region.regions[region] = RegionGenerator.generateRegion(region);
            }
        ).get());
        commands.addPath("help", new CommandBuilder().addNode(new CommandExecution() {
            public void run(CommandContext context) {
                for (String command : commands.paths.keySet()) {
                    String helpText = generateFromNodes(commands.paths.get(command));
                    System.out.print(command);
                    if (!helpText.isEmpty()) System.out.println(" " + helpText);
                    else System.out.println();
                }
            }
            public String generateFromNodes(ArrayList<CommandNode> nodes) {
                StringBuilder builder = new StringBuilder();
                boolean squareBracketsOpened = false;
                for (int i = 0; i < nodes.size(); i++) {
                    CommandNode node = nodes.get(i);
                    if (node instanceof CommandArgument) {
                        builder.append("<");
                        if (node instanceof ListArgument) {
                            for (String elemenet : ((ListArgument)node).values) {
                                builder.append(elemenet).append("|");
                            }
                            builder.deleteCharAt(builder.length() - 1);
                        }
                        else builder.append(((CommandArgument<?>)node).name);
                        builder.append(">");
                    }
                    if (node instanceof CommandLiteral) {
                        CommandLiteral literal = (CommandLiteral)node;
                        builder.append("<");
                        for (String name : literal.paths.keySet()) {
                            builder.append(name);
                            String helpText = generateFromNodes(literal.paths.get(name));
                            if (!helpText.isEmpty()) builder.append(" ").append(helpText);
                            builder.append("|");

                        }
                        builder.deleteCharAt(builder.length() - 1);
                    }
                    if (node instanceof CommandExecution) {
                        if (squareBracketsOpened) {
                            if (builder.length() != 0) builder.deleteCharAt(builder.length() - 1);
                            builder.append("] ");
                        }
                        squareBracketsOpened = i + 1 < nodes.size();
                        if (squareBracketsOpened) builder.append("[");
                    }
                    else builder.append(" ");
                }
                if (builder.length() != 0) builder.deleteCharAt(builder.length() - 1);
                if (squareBracketsOpened) builder.append("]");
                return builder.toString();
            }
        }).get());
    }
}
