package org.by1337.blib.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a placeholder that can be used to process dynamic values for a player.
 */
public class Placeholder {
    /**
     * The name of the placeholder.
     */
    private final String name;
    /**
     * A map of subplaceholders associated with their names.
     */
    private final Map<String, Placeholder> subPlaceholders = new HashMap<>();
    /**
     * The executor responsible for processing the placeholder's value.
     */
    @Nullable
    private PlaceholderExecutor executor;


    /**
     * Constructs a new Placeholder with the specified name.
     *
     * @param name The name of the placeholder.
     */
    public Placeholder(String name) {
        this.name = name;
    }

    /**
     * Adds a subplaceholder to the current placeholder.
     *
     * @param subPlaceholder The subplaceholder to be added.
     * @return The current placeholder instance.
     */
    public Placeholder addSubPlaceholder(Placeholder subPlaceholder) {
        subPlaceholders.put(subPlaceholder.name, subPlaceholder);
        return this;
    }

    /**
     * Sets the executor for the placeholder.
     *
     * @param executor The executor responsible for processing the placeholder's value.
     * @return The current placeholder instance.
     */
    public Placeholder executor(PlaceholderExecutor executor) {
        this.executor = executor;
        return this;
    }

    /**
     * Processes the placeholder for the specified player and arguments.
     *
     * @param player The player for whom the placeholder is processed.
     * @param args   The arguments provided for processing.
     * @return The processed placeholder value, or null if no value is available.
     */
    @Nullable
    public String process(Player player, String[] args){
        if (args.length >= 1) {
            String subcommandName = args[0];

            if (subPlaceholders.containsKey(subcommandName)) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                Placeholder subcommand = subPlaceholders.getOrDefault(subcommandName, null);
                if (subcommand == null) {
                    for (Placeholder cmd : subPlaceholders.values()) {
                        if (cmd.name.equals(subcommandName)) {
                            subcommand = cmd;
                            break;
                        }
                    }
                }
                if (subcommand != null) {
                    return subcommand.process(player, subArgs);
                }
            }
        }
        if (executor == null) return null;
        return executor.run(player);
    }

    /**
     * Retrieves a list of all placeholders associated with this placeholder.
     *
     * @return A list of all placeholders.
     */
    public List<String> getAllPlaceHolders(){
        List<String> list = new ArrayList<>();
        if (executor != null && name != null)
            list.add(name);

        for (Placeholder placeholder : subPlaceholders.values()) {
            for (String s : placeholder.getAllPlaceHolders()) {
                if (name != null)
                    list.add(name + "_" + s);
                else
                    list.add(s);
            }
        }
        return list;
    }

}
