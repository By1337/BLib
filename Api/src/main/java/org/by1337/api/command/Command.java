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
public class Command<T> {
    private final String command;
    private final Map<String, Command<T>> subcommands = new HashMap<>();
    private final LinkedList<Argument<T>> arguments = new LinkedList<>();
    private final List<Requires<T>> requires = new ArrayList<>();
    private final HashSet<String> aliases = new HashSet<>();
    private CommandExecutor<T> executor = (sender, args) -> {
    };

    public Command(String command) {
        this.command = command;
    }

    public Command<T> aliases(String alias) {
        aliases.add(alias);
        return this;
    }


    /**
     * Adds a subcommand to this command.
     *
     * @param subcommand The subcommand to add.
     * @return The current command instance for method chaining.
     */
    public Command<T> addSubCommand(Command<T> subcommand) {
        subcommands.put(subcommand.command, subcommand);
        return this;
    }

    /**
     * Adds an argument to this command.
     *
     * @param argument The argument to add.
     * @return The current command instance for method chaining.
     */
    public Command<T> argument(Argument<T> argument) {
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
    public Command<T> requires(Requires<T> requirement) {
        requires.add(requirement);
        return this;
    }

    /**
     * Sets the executor for this command.
     *
     * @param executor The executor to set.
     * @return The current command instance for method chaining.
     */
    public Command<T> executor(CommandExecutor<T> executor) {
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
    public void process(T sender, String[] args) throws CommandException {
        // Check requirements
        for (Requires<T> requirement : requires) {
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
                Command<T> subcommand = subcommands.getOrDefault(subcommandName, null);
                if (subcommand == null) {
                    for (Command<T> cmd : subcommands.values()) {
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
        Iterator<Argument<T>> argumentIterator = arguments.iterator();
        ArgumentMap<String, Object> argumentValues = new ArgumentMap<>();

        Argument<T> last = null;
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            if (!argumentIterator.hasNext() && last instanceof ArgumentStrings) {
                sb.append(arg).append(" ");
                continue;
            }
            if (argumentIterator.hasNext()) {
                Argument<T> argument = argumentIterator.next();
                last = argument;
                if (argument.getRequires() != null && !argument.getRequires().check(sender)) {
                    break;
                }
                if (last instanceof ArgumentStrings) {
                    sb.append(arg).append(" ");
                    continue;
                }
                argumentValues.put(argument.getName(), argument.process(sender, arg));
            } else {
                throw new CommandSyntaxError(String.format(Lang.getMessage("too-many-arguments"), arg));
            }
        }
        if (last instanceof ArgumentStrings<T> argumentStrings) {
            if (!sb.isEmpty()) {
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
    public List<String> getTabCompleter(T sender, String[] args) {
        // Check requirements
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return Collections.emptyList(); // No completions if requirements are not met.
            }
        }

        // Check for subcommands
        if (args.length >= 1) {
            String subcommandName = args[0];//return subcommands.keySet().stream().toList();
            if (!subcommands.isEmpty() && args[0].isEmpty())
                return subcommands.values().stream().filter(c -> c.checkReq(sender)).map(Command::getCommand).toList();
            if (subcommands.containsKey(subcommandName) ||
                    subcommands.values().stream().anyMatch(subcommand -> subcommand.aliases.contains(subcommandName))
            ) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                Command<T> subcommand = subcommands.getOrDefault(subcommandName, null);
                if (subcommand == null) {
                    for (Command<T> cmd : subcommands.values()) {
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
            Iterator<Argument<T>> argumentIterator = arguments.iterator();

            Argument<T> last = null;
            for (String arg : args) {
                if (!argumentIterator.hasNext() && last instanceof ArgumentStrings) {
                    continue;
                }
                if (argumentIterator.hasNext()) {
                    Argument<T> argument = argumentIterator.next();
                    last = argument;
                    if (argument.getRequires() != null && !argument.getRequires().check(sender)) {
                        break;
                    }
                    completions.clear();

                    completions.addAll(argument.tabCompleter(sender, arg));

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

        return subcommands.values().stream().filter(c -> c.checkReq(sender)).map(Command::getCommand).toList();
    }

    public boolean checkReq(T sender) {
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return false;
            }
        }
        return true;
    }

}
