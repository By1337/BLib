package org.by1337.blib.command;

import com.google.common.base.Joiner;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.by1337.blib.BLib;
import org.by1337.blib.command.argument.Argument;
import org.by1337.blib.command.argument.ArgumentMap;
import org.by1337.blib.command.argument.ArgumentStrings;
import org.by1337.blib.command.requires.Requires;
import org.by1337.blib.lang.Lang;
import org.by1337.blib.text.MessageFormatter;
import org.jetbrains.annotations.NotNull;
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
        process(sender, new StringReader(Joiner.on(" ").join(args)));
    }

    public void process(T sender, StringReader reader) throws CommandException {
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return;
            }
        }

        if (reader.hasNext()) {
            int point = reader.getCursor();
            Command<T> subCommand = findSubCommand(reader.readToSpace());
            if (subCommand != null) {
                reader.pop();
                subCommand.process(sender, reader);
                return;
            }
            reader.setCursor(point);
        }


        Iterator<Argument<T>> argumentIterator = arguments.iterator();
        ArgumentMap<String, Object> argumentMap = new ArgumentMap<>();

        Argument<T> argument;
        while (argumentIterator.hasNext()) {
            argument = argumentIterator.next();
            if (!argument.checkRequires(sender)) {
                break;
            }
            argument.process(sender, reader, argumentMap);
            if (!reader.hasNext()) break;
            if (reader.peek() == ' '){
                reader.pop();
            }
        }
        if (reader.hasNext()) {
            if (usage != null && sender instanceof CommandSender commandSender) {
                commandSender.sendMessage(usage);
                return;
            } else {
                throw new CommandSyntaxError(MessageFormatter.apply(Lang.getMessage("too-many-arguments"), reader.getExtra()));
            }
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
     * @deprecated use {@link Command#tabComplete}
     */
    @Deprecated
    public List<String> getTabCompleter(T sender, String[] args) {
        return tabComplete(sender, args);
    }

    public List<String> tabComplete(T sender, String[] args) {
        Suggestions suggestions = tabComplete(sender, new StringReader(Joiner.on(" ").join(args)));
        return suggestions.getList().stream().map(Suggestion::getText).toList();
    }

    public @NotNull Suggestions tabComplete(T sender, StringReader reader) {
        try {
           // if (!reader.hasNext()) new Suggestions(new StringRange(0, 0), Collections.emptyList());
            Suggestions suggestions = getTabCompleter0(sender, reader);
            return suggestions == null ? new Suggestions(new StringRange(0, 0), Collections.emptyList()) : suggestions;
        } catch (CommandSyntaxError e) {
            SuggestionsBuilder builder = new SuggestionsBuilder(reader.getString(), reader.getCursor());
            builder.suggest(e.getMessage());
            return builder.build();
        }
    }

    private @Nullable Suggestions getTabCompleter0(T sender, StringReader reader) throws CommandSyntaxError {
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return null;
            }
        }
        if (reader.hasNext()) {
            int point = reader.getCursor();
            Command<T> subCommand = findSubCommand(reader.readToSpace());
            if (subCommand != null && reader.hasNext()) {
                reader.pop();
                return subCommand.getTabCompleter0(sender, reader);
            }
            reader.setCursor(point);
        }

        Iterator<Argument<T>> argumentIterator = arguments.iterator();
        ArgumentMap<String, Object> argumentMap = new ArgumentMap<>();

        List<String> completions = new ArrayList<>();
        Argument<T> argument;
        int start = reader.getCursor();
        while (argumentIterator.hasNext()) {
            start = reader.getCursor();
            argument = argumentIterator.next();
            if (!argument.checkRequires(sender)) {
                break;
                //throw new CommandSyntaxError(Lang.getMessage("end-of-command"));
            }
            completions.clear();
            if (!argument.isHide()) {
                completions.addAll(argument.tabCompleter(sender, reader, argumentMap));
                if (completions.isEmpty()) {
                    completions.addAll(argument.getExx());
                    if (completions.isEmpty()) {
                        completions.add("[" + argument.getName() + "]");
                    }
                }
            }
            if (!reader.hasNext()) break;
            if (reader.peek() == ' '){
                reader.pop();
            }
        }


        var list = subcommands.values().stream().filter(c -> c.checkReq(sender)).map(Command::getCommand).collect(Collectors.toList());
        if (reader.hasNext()) {
            var lastWord = reader.readToSpace();
            if (!lastWord.isEmpty()) {
                list.removeIf(s -> !s.startsWith(lastWord));
            }
        }
        completions.addAll(list);

        String s = reader.getString();
        SuggestionsBuilder builder = new SuggestionsBuilder(s, Math.min(start, s.length()));
        for (String completion : completions) {
            builder.suggest(completion);
        }
        return builder.build();
    }

    public boolean checkReq(T sender) {
        for (Requires<T> requirement : requires) {
            if (!requirement.check(sender)) {
                return false;
            }
        }
        return true;
    }

    public boolean allowAsync(){
        for (Argument<T> argument : arguments) {
            if (!argument.allowAsync()) return false;
        }
        for (Command<T> value : subcommands.values()) {
            if (!value.allowAsync()) return false;
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
