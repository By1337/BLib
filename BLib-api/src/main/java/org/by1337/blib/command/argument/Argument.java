package org.by1337.blib.command.argument;

import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.StringReader;
import org.by1337.blib.command.requires.Requires;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Abstract class representing a command argument.
 */
public abstract class Argument<T> {
    protected final String name;
    private Supplier<List<String>> exx;
    protected List<Requires<T>> requires = new ArrayList<>();
    private boolean hide;

    /**
     * Constructs an Argument with the specified name and no examples.
     *
     * @param name The name of the argument.
     */
    public Argument(String name) {
        this.name = name;
        this.exx = ArrayList::new;
    }


    public Argument(String name, Supplier<List<String>> exx) {
        this.name = name;
        this.exx = exx;
    }

    @Deprecated
    public Object process(T sender, String str) throws CommandSyntaxError{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void process(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        argumentMap.put(name, process(sender, reader.readToSpace()));
    }

    @Deprecated
    public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
        return getExx();
    }

    public List<String> tabCompleter(T sender, StringReader reader, ArgumentMap<String, Object> argumentMap) throws CommandSyntaxError {
        return tabCompleter(sender, reader.readToSpace());
    }

    public Argument<T> requires(Requires<T> requires) {
        this.requires.add(requires);
        return this;
    }

    public Argument<T> requires(Requires<T>... requires) {
        this.requires.addAll(Arrays.stream(requires).toList());
        return this;
    }

    public boolean checkRequires(T sender) {
        for (Requires<T> require : requires) {
            if (!require.check(sender)) {
                return false;
            }
        }
        return true;
    }

    public boolean isHide() {
        return hide;
    }

    public Argument<T> hide() {
        hide = true;
        return this;
    }

    public Argument<T> setHide(boolean hide) {
        this.hide = hide;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getExx() {
        return this.exx.get();
    }

    public List<Requires<T>> getRequires() {
        return this.requires;
    }

    public void setExx(Supplier<List<String>> exx) {
        this.exx = exx;
    }
    public boolean allowAsync(){
        return false;
    }
}
