package org.by1337.blib.command.argument;

import com.google.common.base.Joiner;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.lang.Lang;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArgumentLookingAtBlock<T extends CommandSender> extends Argument<T> {
    private static final List<String> ZERO = List.of("0");
    private static final ArgumentIntegerAllowedMath<CommandSender> MATH = new ArgumentIntegerAllowedMath<>("123", List.of("0"));

    public ArgumentLookingAtBlock(String name) {
        super(name);
    }

    @Override
    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        List<String> read = read(reader);
        if (read.size() != 3) {
            throw new CommandSyntaxError(Lang.getMessage("arg-looking-at-block-expected-3-pos"), Joiner.on(" ").join(read));
        }
        Integer x = (Integer) MATH.process(sender, read.get(0));
        Integer y = (Integer) MATH.process(sender, read.get(1));
        Integer z = (Integer) MATH.process(sender, read.get(2));
        argumentMap.put(name, new Vec3i(x, y, z));
    }

    @Override
    public void tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap, SuggestionsBuilder builder) throws CommandSyntaxError {
        Vec3i block = Objects.requireNonNullElse(getBlock(sender), Vec3i.ZERO);
        reader.setCursor(builder.getStart());
        SuggestionsBuilder builder0 = builder.restart();

        MATH.tabCompleter(sender, reader, new ArgumentMap<>(), builder0);
        builder0.suggest(Integer.toString(block.x));

        if (reader.hasNext()) {
            reader.skipSpace();
            builder0 = builder.createOffset(reader.getCursor());
            MATH.tabCompleter(sender, reader, new ArgumentMap<>(), builder0);
            builder0.suggest(Integer.toString(block.y));
        }
        if (reader.hasNext()) {
            reader.skipSpace();
            builder0 = builder.createOffset(reader.getCursor());
            MATH.tabCompleter(sender, reader, new ArgumentMap<>(), builder0);
            builder0.suggest(Integer.toString(block.z));
        }
        builder.add(builder0);
    }

    private List<String> read(StringReader reader) {
        List<String> list = new ArrayList<>();
        while (reader.hasNext()) {
            String str = ArgumentUtils.readString(reader);
            if (str.isBlank()) break;
            list.add(str);
            if (list.size() == 3) break;
            reader.skip();
        }
        return list;
    }

    @Nullable
    private Vec3i getBlock(CommandSender sender) {
        if (sender instanceof Player player) {
            RayTraceResult result = player.rayTraceBlocks(10);
            if (result == null) return null;
            return result.getHitBlock() == null ? null : new Vec3i(result.getHitBlock());
        }
        return null;
    }

    @Override
    public boolean allowAsync() {
        return false;
    }
}
