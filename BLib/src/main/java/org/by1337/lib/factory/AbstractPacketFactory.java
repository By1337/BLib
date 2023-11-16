package org.by1337.lib.factory;


import org.by1337.api.factory.PacketFactory;
import org.by1337.api.util.Version;
import org.by1337.v1_16_5.network.factory.PacketFactoryImpl165;
import org.by1337.v1_17.network.factory.PacketFactoryImpl17;

public class AbstractPacketFactory {
    public static PacketFactory create(){
        return switch (Version.VERSION){
            case V1_16_5 -> new PacketFactoryImpl165();
            case V1_17 -> new PacketFactoryImpl17();
            case V1_17_1 -> null;
            case V1_18 -> null;
            case V1_18_1 -> null;
            case V1_18_2 -> null;
            case V1_19 -> null;
            case V1_19_1 -> null;
            case V1_19_2 -> null;
            case V1_19_3 -> null;
            case V1_19_4 -> null;
            case V1_20_1 -> null;
            case UNKNOWN -> null;
        };
    }
}
