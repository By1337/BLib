package org.by1337.blib.command;

import junit.framework.TestCase;
import net.kyori.adventure.text.Component;
import org.by1337.blib.command.argument.ArgumentMap;
import org.by1337.blib.command.argument.ArgumentString;
import org.by1337.blib.command.argument.ArgumentStrings;
import org.by1337.blib.lang.Lang;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

public class CommandTest {
    private boolean flag;
    private String args;
    @Test
    public void test1() throws CommandException {
        Lang.loadIfNull();
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
                )
                ;

        Assert.assertArrayEquals(cmd.getTabCompleter(null, new String[]{"test", ""}).toArray(), new String[]{"[str]"});
        if (Arrays.equals(cmd.getTabCompleter(null, new String[]{"test3", ""}).toArray(), new String[]{"[str]"})){
            throw new IllegalStateException("The terms on the arguments broke!");
        }
        Assert.assertArrayEquals(cmd.getTabCompleter(null, new String[]{""}).toArray(), new String[]{"test2", "test3", "test"});
        cmd.process(null, new String[]{"test", "string"});
        cmd.process(null, new String[]{"test2", "string", "string", "string"});
        Assert.assertTrue(flag);
        Assert.assertEquals(args, "string string string");
    }
}