package org.by1337.blib.nms.V1_21_6.inventory;



import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.junit.Test;

import java.util.List;

public class ItemStackSerializeV1216Test {

    @Test
    public void test(){
        MiniMessage.miniMessage().deserialize("<red>123");
        GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize("<red>123"));
        long nanos = System.nanoTime();
        GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize("<red>123"));
        System.out.println((System.nanoTime()-nanos)/1_000 + "µs");
    }
    public static class MyComponent implements Component{

        @Override
        public @Unmodifiable @NotNull List<Component> children() {
            return List.of();
        }

        @Override
        public @NotNull Component children(@NotNull List<? extends ComponentLike> children) {
            return null;
        }

        @Override
        public @NotNull Style style() {
            return null;
        }

        @Override
        public @NotNull Component style(@NotNull Style style) {
            return null;
        }
    }
}