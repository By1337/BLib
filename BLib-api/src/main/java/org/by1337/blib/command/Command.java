package org.by1337.blib.command;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.by1337.blib.BLib;
import org.by1337.blib.command.argument.Argument;
import org.by1337.blib.command.argument.ArgumentMap;
import org.by1337.blib.command.argument.ArgumentStrings;
import org.by1337.blib.command.requires.Requires;
import org.by1337.blib.lang.Lang;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a command that can have subcommands, arguments, and an executor.
 */
public class Command<T> {
    private final String command;
    private final Map<String, Command<T>> subcommands = new HashMap<>();
    private final LinkedList<Argument<T>> arguments = new LinkedList<>();
    private final List<Requires<T>> requires = new ArrayList<>();
    private final HashSet<String> aliases = new HashSet<>();
    private CommandExecutor<T> executor = (sender, args) -> {
    };
    @Nullable
    private Component usage;

    public Command(String command) {
        this.command = command;
    }

    public Command<T> aliases(String alias) {
        aliases.add(alias);
        return this;
    }

    public Command<T> usage(Component usage) {
        this.usage = usage;
        return this;
    }

    public Command<T> usage(String usage) {
        this.usage = BLib.getApi().getMessage().componentBuilder(usage);
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
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return;
            }
        }
        if (args.length >= 1) {
            Command<T> subCommand = findSubCommand(args[0]);
            if (subCommand != null) {
                subCommand.process(sender, Arrays.copyOfRange(args, 1, args.length));
                return;
            }
        }
        Iterator<Argument<T>> argumentIterator = arguments.iterator();
        ArgumentMap<String, Object> argumentMap = new ArgumentMap<>();

        var argsIterator = Arrays.stream(args).iterator();

        Argument<T> last = null;
        StringBuilder sb = new StringBuilder();

        while (argsIterator.hasNext()) {
            String raw = argsIterator.next();

            if (last instanceof ArgumentStrings<T>) {
                sb.append(raw).append(" ");
                continue;
            }

            if (argumentIterator.hasNext()) {
                last = argumentIterator.next();
                if (!last.checkRequires(sender)) {
                    break;
                }
                if (last instanceof ArgumentStrings<T>) {
                    sb.append(raw).append(" ");
                    continue;
                }
                argumentMap.put(last.getName(), last.process(sender, raw));
            } else {
                if (usage != null && sender instanceof CommandSender commandSender) {
                    commandSender.sendMessage(usage);
                    return;
                } else {
                    throw new CommandSyntaxError(String.format(Lang.getMessage("too-many-arguments"), raw));
                }
            }
        }
        if (last instanceof ArgumentStrings<T> && last.checkRequires(sender)) {
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            argumentMap.put(last.getName(), last.process(sender, sb.toString()));
        }

        executor.execute(sender, argumentMap);
    }

    @Nullable
    private Command<T> findSubCommand(String name) {
        var cmd = subcommands.get(name);
        if (cmd == null) {
            for (Command<T> value : subcommands.values()) {
                if (value.aliases.contains(name)) {
                    return value;
                }
            }
        }
        return cmd;
    }

    /**
     * Generates tab completions for the command.
     *
     * @param sender The command sender.
     * @param args   The command arguments.
     * @return A list of tab completions.
     */
    public List<String> getTabCompleter(T sender, String[] args) {
        try {
            if (args.length == 0) return Collections.emptyList();
            List<String> list = getTabCompleter0(sender, args);
            return list;
          /*  String lastParam = args[args.length - 1];
            return list.stream()
                    .filter(s -> !s.isEmpty() && s.startsWith(lastParam))
                    .toList();*/
        } catch (CommandSyntaxError e) {
            return Collections.singletonList(e.getMessage());
        }
    }

    private List<String> getTabCompleter0(T sender, String[] args) throws CommandSyntaxError {
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return Collections.emptyList();
            }
        }
        if (args.length >= 1) {
            Command<T> subCommand = findSubCommand(args[0]);
            if (subCommand != null) {
                return subCommand.getTabCompleter0(sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        Iterator<Argument<T>> argumentIterator = arguments.iterator();

        var argsIterator = Arrays.stream(args).iterator();

        Argument<T> last = null;
        StringBuilder sb = new StringBuilder();
        List<String> completions = new ArrayList<>();

        while (argsIterator.hasNext() && !arguments.isEmpty()) {
            String raw = argsIterator.next();
            if (last instanceof ArgumentStrings<T>) {
                sb.append(raw).append(" ");
                continue;
            }

            if (argumentIterator.hasNext()) {
                last = argumentIterator.next();
                if (!last.checkRequires(sender)) {
                    throw new CommandSyntaxError(Lang.getMessage("end-of-command"));
                    // break;
                }
                if (last instanceof ArgumentStrings<T>) {
                    sb.append(raw).append(" ");
                    continue;
                }
                completions.clear();
                if (!last.isHide()){
                    completions.addAll(last.tabCompleter(sender, raw));
                    if (completions.isEmpty()) {
                        completions.addAll(last.getExx());
                        if (completions.isEmpty()) {
                            completions.add("[" + last.getName() + "]");
                        }
                    }
                }
            } else {
                throw new CommandSyntaxError(Lang.getMessage("end-of-command"));
            }
        }
        if (last instanceof ArgumentStrings<T> && last.checkRequires(sender)) {
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            if (!last.isHide()){
                completions.addAll(last.tabCompleter(sender, sb.toString()));
                if (completions.isEmpty()) {
                    completions.addAll(last.getExx());
                    if (completions.isEmpty()) {
                        completions.add("[" + last.getName() + "]");
                    }
                }
            }
        }
        var list = subcommands.values().stream().filter(c -> c.checkReq(sender)).map(Command::getCommand).collect(Collectors.toList());
        if (args.length != 0){
            var lastWord = args[args.length - 1];
            if (!lastWord.isEmpty()){
                list.removeIf(s -> !s.startsWith(lastWord));
            }
        }
        completions.addAll(list);
        return completions;
    }

    public boolean checkReq(T sender) {
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return false;
            }
        }
        return true;
    }

    public String getCommand() {
        return this.command;
    }

    public Map<String, Command<T>> getSubcommands() {
        return this.subcommands;
    }

    public LinkedList<Argument<T>> getArguments() {
        return this.arguments;
    }

    public List<Requires<T>> getRequires() {
        return this.requires;
    }

    public HashSet<String> getAliases() {
        return this.aliases;
    }

    public CommandExecutor<T> getExecutor() {
        return this.executor;
    }

    public String toString() {
        return "Command(command=" + this.getCommand() + ", subcommands=" + this.getSubcommands() + ", arguments=" + this.getArguments() + ", requires=" + this.getRequires() + ", aliases=" + this.getAliases() + ", executor=" + this.getExecutor() + ")";
    }
}
