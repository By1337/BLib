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
            default  -> null;
        };
    }
}
