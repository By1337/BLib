package org.by1337.api.command;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.by1337.api.command.argument.ArgumentMap;
import org.by1337.api.command.requires.Requires;
import org.by1337.api.command.argument.Argument;
import org.by1337.api.command.argument.ArgumentStrings;
import org.by1337.api.lang.Lang;

import java.util.*;

/**
 * Represents a command that can have subcommands, arguments, and an executor.
 */
@ToString
@Getter
public class Command {
    private final String command;
    private final Map<String, Command> subcommands = new HashMap<>();
    private final LinkedList<Argument> arguments = new LinkedList<>();
    private final List<Requires> requires = new ArrayList<>();
    private final HashSet<String> aliases = new HashSet<>();
    private CommandExecutor executor = (sender, args) -> {
    };

    public Command(String command) {
        this.command = command;
    }

    public Command aliases(String alias) {
        aliases.add(alias);
        return this;
    }


    /**
     * Adds a subcommand to this command.
     *
     * @param subcommand The subcommand to add.
     * @return The current command instance for method chaining.
     */
    public Command addSubCommand(Command subcommand) {
        subcommands.put(subcommand.command, subcommand);
        return this;
    }

    /**
     * Adds an argument to this command.
     *
     * @param argument The argument to add.
     * @return The current command instance for method chaining.
     */
    public Command argument(Argument argument) {
        arguments.add(argument);
        return this;
    }

    /**
     * Adds a requirement to this command. A requirement is a condition that must be met
     * for the command to be executed.
     *
     * @param requirement The requirement to add.
     * @return The current command instance for method chaining.
     */
    public Command requires(Requires requirement) {
        requires.add(requirement);
        return this;
    }

    /**
     * Sets the executor for this command.
     *
     * @param executor The executor to set.
     * @return The current command instance for method chaining.
     */
    public Command executor(CommandExecutor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Processes the command.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     * @throws CommandSyntaxError If there's a syntax error in the command.
     */
    public void process(CommandSender sender, String[] args) throws CommandException {
        // Check requirements
        for (Requires requirement : requires) {
            if (!requirement.check(sender)) {
                return; // Command execution halted due to unmet requirements.
            }
        }

        // Check for subcommands
        if (args.length >= 1) {
            String subcommandName = args[0].isEmpty() ? "help" : args[0];

            if (subcommands.containsKey(subcommandName) || subcommands.values().stream()
                    .anyMatch(subcommand -> subcommand.aliases.contains(subcommandName))) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                Command subcommand = subcommands.getOrDefault(subcommandName, null);
                if (subcommand == null) {
                    for (Command cmd : subcommands.values()) {
                        if (cmd.aliases.contains(subcommandName)) {
                            subcommand = cmd;
                            break;
                        }
                    }
                }
                if (subcommand != null) {
                    subcommand.process(sender, subArgs);
                    return;
                }
            }
        }

        // Process arguments
        Iterator<Argument> argumentIterator = arguments.iterator();
        ArgumentMap<String, Object> argumentValues = new ArgumentMap<>();

        Argument last = null;
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            if (!argumentIterator.hasNext() && last instanceof ArgumentStrings) {
                sb.append(arg).append(" ");
                continue;
            }
            if (argumentIterator.hasNext()) {
                Argument argument = argumentIterator.next();
                last = argument;
                if (argument.getRequires() != null && !argument.getRequires().check(sender)) {
                    break;
                }
                if (last instanceof ArgumentStrings){
                    sb.append(arg).append(" ");
                    continue;
                }
                argumentValues.put(argument.getName(), argument.process(sender, arg));
            } else {
                throw new CommandSyntaxError(String.format(Lang.getMessage("too-many-arguments"), arg));
            }
        }
        if (last instanceof ArgumentStrings argumentStrings) {
            if (!sb.isEmpty()){
                sb.setLength(sb.length() - 1);
            }
            argumentValues.put(argumentStrings.getName(), argumentStrings.process(sender, sb.toString()));
        }

        // Execute the command
        executor.executes(sender, argumentValues);
    }

    /**
     * Generates tab completions for the command.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     * @return A list of tab completions.
     */
    public List<String> getTabCompleter(CommandSender sender, String[] args) {
        // Check requirements
        for (Requires requirement : requires) {
            if (!requirement.check(sender)) {
                return Collections.emptyList(); // No completions if requirements are not met.
            }
        }

        // Check for subcommands
        if (args.length >= 1) {
            String subcommandName = args[0];
            if (!subcommands.isEmpty() && args[0].isEmpty()) return subcommands.keySet().stream().toList();
            if (subcommands.containsKey(subcommandName) ||
                    subcommands.values().stream().anyMatch(subcommand -> subcommand.aliases.contains(subcommandName))
            ) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                Command subcommand = subcommands.getOrDefault(subcommandName, null);
                if (subcommand == null) {
                    for (Command cmd : subcommands.values()) {
                        if (cmd.aliases.contains(subcommandName)) {
                            subcommand = cmd;
                            break;
                        }
                    }
                }
                if (subcommand != null) {
                    return subcommand.getTabCompleter(sender, subArgs);
                }
            }

        }


        List<String> completions = new ArrayList<>();

        try {
            Iterator<Argument> argumentIterator = arguments.iterator();

            Argument last = null;
            for (String arg : args) {
                if (!argumentIterator.hasNext() && last instanceof ArgumentStrings){
                    continue;
                }
                if (argumentIterator.hasNext()) {
                    Argument argument = argumentIterator.next();
                    last = argument;
                    if (argument.getRequires() != null && !argument.getRequires().check(sender)) {
                        break;
                    }
                    completions.clear();

                    completions.addAll(argument.tabCompleter(sender, arg));
                    completions.addAll(argument.getExx());

                } else {
                    if (!args[0].isEmpty()) {
                        List<String> sub = new ArrayList<>();
                        for (String key : subcommands.keySet()) {
                            if (key.startsWith(args[0])) {
                                sub.add(key);
                            }
                        }
                        if (!sub.isEmpty())
                            return sub;
                    }
                    throw new CommandSyntaxError("");
                }
            }
            if (!completions.isEmpty()) {
                return completions;
            }
        } catch (CommandSyntaxError error) {
            return Collections.singletonList(error.getLocalizedMessage());
        }

        return new ArrayList<>(subcommands.keySet());
    }


}
