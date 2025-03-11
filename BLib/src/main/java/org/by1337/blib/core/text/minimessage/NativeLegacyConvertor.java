package org.by1337.blib.core.text.minimessage;

import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.PluginClassLoader;
import org.by1337.blib.core.annotations.ASM;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.text.LegacyFormattingConvertor;

import java.util.function.Function;

public class NativeLegacyConvertor implements LegacyConvertor {

    private final Function<String, Component> minimessage;

    public NativeLegacyConvertor() {
        Function<String, Component> fun;
        try {
            Class<?> miniMsg = Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            try {
                miniMsg.getMethod("miniMessage");
                fun = NativeLegacyConvertor::miniMessageMethod;
            } catch (NoSuchMethodException e) {
                miniMsg.getMethod("get");
                fun = NativeLegacyConvertor::getMethod;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        minimessage = fun;
    }

    @Override
    public Component convert(String legacy) {
        return minimessage.apply(LegacyFormattingConvertor.convert(legacy));
    }

    public static boolean isAvailable() {
        try {
            Class<?> miniMsg = Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            if (miniMsg.getClassLoader() instanceof PluginClassLoader) return false;

            try {
                miniMsg.getMethod("miniMessage");
            } catch (NoSuchMethodException e) {
                miniMsg.getMethod("get");
            }
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @ASM
    private static Component getMethod(String input) {
        String asm = """
                A:
                    invokestatic net/kyori/adventure/text/minimessage/MiniMessage get ()Lnet/kyori/adventure/text/minimessage/MiniMessage; true
                    aload 0
                    invokeinterface net/kyori/adventure/text/minimessage/MiniMessage deserialize (Ljava/lang/Object;)Lnet/kyori/adventure/text/Component; true
                    areturn
                """;
        throw new IllegalStateException("ASM did not apply! " + asm);
    }

    @ASM
    private static Component miniMessageMethod(String input) {
        String asm = """
                A:
                    invokestatic net/kyori/adventure/text/minimessage/MiniMessage miniMessage ()Lnet/kyori/adventure/text/minimessage/MiniMessage; true
                    aload 0
                    invokeinterface net/kyori/adventure/text/minimessage/MiniMessage deserialize (Ljava/lang/Object;)Lnet/kyori/adventure/text/Component; true
                    areturn
                """;
        throw new IllegalStateException("ASM did not apply! " + asm);
    }
}
