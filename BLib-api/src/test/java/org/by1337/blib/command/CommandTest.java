package org.by1337.blib.command;

import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import net.kyori.adventure.text.Component;
import org.by1337.blib.command.argument.ArgumentString;
import org.by1337.blib.command.argument.ArgumentStrings;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {
    private boolean flag;
    private String args;

    @Test
    public void test1() throws CommandException {
        var cmd = new Command<Object>("cmd")
                .usage(Component.text("/cmd <test/test2>"))
                .addSubCommand(
                        new Command<Object>("test")
                                .usage(Component.text("/cmd test <str>"))
                                .argument(new ArgumentString<>("str"))
                                .executor((v, args) -> {
                                    flag = true;
                                })
                )
                .addSubCommand(
                        new Command<Object>("test2")
                                .usage(Component.text("/cmd test2 <str>"))
                                .argument(new ArgumentStrings<>("str"))
                                .executor((v, args) -> {
                                    this.args = (String) args.getOrThrow("str");
                                })
                )
                .addSubCommand(
                        new Command<Object>("test3")
                                .usage(Component.text("/cmd test3 <str>"))
                                .argument(new ArgumentStrings<>("str").requires((s -> false)))
                )
                .addSubCommand(
                        new Command<Object>("test4")
                                .requires(s -> false)
                );

        assertArrayEquals(cmd.getTabCompleter(null, new String[]{"test", ""}).toArray(), new String[]{"[str]"});
        if (Arrays.equals(cmd.getTabCompleter(null, new String[]{"test3", ""}).toArray(), new String[]{"[str]"})) {
            throw new IllegalStateException("The terms on the arguments broke!");
        }
        assertArrayEquals(cmd.getTabCompleter(null, new String[]{""}).toArray(), new String[]{"test2", "test3", "test"});
        cmd.process(null, new String[]{"test", "string"});
        cmd.process(null, new String[]{"test2", "string", "string", "string"});
        assertTrue(flag);
        assertEquals(args, "string string string");
    }

    @Test
    public void test2() throws CommandException {
        var cmd = new Command<Object>("cmd")
                .addSubCommand(new Command<>("test")
                        .argument(new ArgumentString<>("str", List.of("str")))
                        .argument(new ArgumentString<>("str2", List.of("str2")))
                        .executor((v, args) -> {
                            assertNotNull(args.get("str"));
                            assertNotNull(args.get("str2"));
                        })
                )
                .addSubCommand(new Command<>("test2")
                        .argument(new ArgumentString<>("str", List.of("str")))
                        .argument(new ArgumentString<>("str2", List.of("str2")))
                        .executor((v, args) -> {
                            assertNotNull(args.get("str"));
                            assertNotNull(args.get("str2"));
                        })
                        .addSubCommand(new Command<>("sub")
                                .argument(new ArgumentString<>("str", List.of("str")))
                                .argument(new ArgumentString<>("str2", List.of("str2")))
                                .executor((v, args) -> {
                                    assertNotNull(args.get("str"));
                                    assertNotNull(args.get("str2"));
                                })
                        )
                )
                .addSubCommand(new Command<>("no_args")
                        .addSubCommand(new Command<>("sub")
                                .argument(new ArgumentString<>("str", List.of("str")))
                                .argument(new ArgumentString<>("str2", List.of("str2")))
                                .executor((v, args) -> {
                                    assertNotNull(args.get("str"));
                                    assertNotNull(args.get("str2"));
                                })
                        )
                );

        assertEquals(
                cmd.tabComplete(null, new StringReader("")).getList(),
                List.of(suggestion(0, 0, "no_args"), suggestion(0, 0, "test"), suggestion(0, 0, "test2"))
        );
        assertEquals(
                cmd.tabComplete(null, new StringReader("test ")).getList(),
                List.of(suggestion(5, 5, "str"))
        );
        assertEquals(
                cmd.tabComplete(null, new StringReader("test st")).getList(),
                List.of(suggestion(5, 7, "str"))
        );
        assertEquals(
                cmd.tabComplete(null, new StringReader("test2 ")).getList(),
                List.of(suggestion(6, 6, "str"), suggestion(6, 6, "sub"))
        );
       // cmd.process(null, new StringReader("no_args"));
    }

    private Suggestion suggestion(int start, int end, String text) {
        return new Suggestion(new StringRange(start, end), text);
    }
}