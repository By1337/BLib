package org.by1337.blib.hook.papi;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Placeholder {
    private final String name;
    private final Map<String, Placeholder> subPlaceholders = new HashMap<>();
    @Nullable
    private PlaceholderExecutor executor;

    public Placeholder(String name) {
        this.name = name;
    }

    public Placeholder addSubPlaceholder(Placeholder subPlaceholder) {
        subPlaceholders.put(subPlaceholder.name, subPlaceholder);
        return this;
    }

    public Placeholder executor(PlaceholderExecutor executor) {
        this.executor = executor;
        return this;
    }

    @Nullable
    public String process(Player player, String[] args) {
        if (args.length >= 1) {
            String subcommandName = args[0];

            if (subPlaceholders.containsKey(subcommandName)) {
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                Placeholder subcommand = subPlaceholders.get(subcommandName);
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
        return executor.run(player, args);
    }

    public void build() {
        var map = new HashMap<>(subPlaceholders);
        subPlaceholders.clear();
        for (Map.Entry<String, Placeholder> entry : map.entrySet()) {
            String key = entry.getKey();
            Placeholder inner = entry.getValue();
            String[] arr = key.split("_");
            Placeholder last = this;
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i];
                var pl = last.subPlaceholders.get(s);
                if (pl == null){
                    pl = new Placeholder(s);
                    last.subPlaceholders.put(s, pl);
                }
                last = pl;
                if (i == arr.length -1){
                    last.executor = inner.executor;
                }
            }
            if (!inner.subPlaceholders.isEmpty()){
                inner.build();
                last.subPlaceholders.putAll(inner.subPlaceholders);
            }
        }
    }

    public List<String> getAllPlaceHolders() {
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

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Placeholder{" +
                "name='" + name + '\'' +
                ", subPlaceholders=" + subPlaceholders +
                ", executor=" + executor +
                '}';
    }
}
