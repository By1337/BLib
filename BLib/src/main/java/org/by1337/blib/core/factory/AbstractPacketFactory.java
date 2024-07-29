package org.by1337.blib.core.factory;


import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.util.Version;
import org.by1337.blib.factory.PacketFactory;
import org.by1337.blib.nms.v1_16_5.network.factory.PacketFactoryImpl165;

public class AbstractPacketFactory {
    public static PacketFactory create() {
        return switch (Version.VERSION) {
            case V1_16_5 -> new PacketFactoryImpl165();
            default -> null;
        };
    }
}
