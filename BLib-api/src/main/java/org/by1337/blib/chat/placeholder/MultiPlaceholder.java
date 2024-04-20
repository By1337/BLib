package org.by1337.blib.chat.placeholder;

import org.by1337.blib.chat.Placeholderable;

import java.util.ArrayList;
import java.util.List;

public class MultiPlaceholder implements Placeholderable{
    private final List<Placeholderable> arr;

    public MultiPlaceholder(Placeholderable... placeholderables) {
        this.arr = new ArrayList<>(List.of(placeholderables));
    }
    public void add(Placeholderable placeholderable){
        arr.add(placeholderable);
    }


    @Override
    public String replace(String string) {
        String s = string;
        for (Placeholderable placeholderable : arr) {
            s = placeholderable.replace(s);
        }
        return s;
    }
}
