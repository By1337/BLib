package org.by1337.blib.text;

import net.kyori.adventure.text.Component;
import org.by1337.blib.BLib;

public interface ComponentToANSI {
    static ComponentToANSI get() {
        return BLib.getApi().getComponentToANSI();
    }
    String convert(Component component);
    String buildAndConvert(String legacy);

}
