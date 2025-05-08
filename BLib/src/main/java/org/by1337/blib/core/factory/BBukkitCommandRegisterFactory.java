package org.by1337.blib.core.factory;

import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.factory.BukkitCommandRegisterFactory;
import org.by1337.blib.nms.V1_20_6.command.BukkitCommandRegisterV1_20_6;
import org.by1337.blib.nms.V1_21.command.BukkitCommandRegisterV1_21;
import org.by1337.blib.nms.V1_21.command.BukkitCommandRegisterV1_21_1;
import org.by1337.blib.nms.V1_21_3.command.BukkitCommandRegisterV1_21_3;
import org.by1337.blib.nms.V1_21_4.command.BukkitCommandRegisterV1_21_4;
import org.by1337.blib.nms.V1_21_5.command.BukkitCommandRegisterV1_21_5;
import org.by1337.blib.nms.v1_16_5.command.BukkitCommandRegisterV1_16_5;
import org.by1337.blib.nms.v1_17_1.command.BukkitCommandRegisterV1_17_1;
import org.by1337.blib.nms.v1_18_2.command.BukkitCommandRegisterV1_18_2;
import org.by1337.blib.nms.v1_19_4.command.BukkitCommandRegisterV1_19_4;
import org.by1337.blib.nms.v1_20_1.command.BukkitCommandRegisterV1_20_1;
import org.by1337.blib.nms.v1_20_2.command.BukkitCommandRegisterV1_20_2;
import org.by1337.blib.nms.v1_20_4.command.BukkitCommandRegisterV1_20_4;
import org.by1337.blib.util.Version;

public class BBukkitCommandRegisterFactory implements BukkitCommandRegisterFactory {
    private final BukkitCommandRegister register = switch (Version.VERSION){
        case UNKNOWN -> null;
        case V1_16_5 -> new BukkitCommandRegisterV1_16_5();
        case V1_17_1 -> new BukkitCommandRegisterV1_17_1();
        case V1_18_2 -> new BukkitCommandRegisterV1_18_2();
        case V1_19_4 -> new BukkitCommandRegisterV1_19_4();
        case V1_20_1 -> new BukkitCommandRegisterV1_20_1();
        case V1_20_2 -> new BukkitCommandRegisterV1_20_2();
        case V1_20_4, V1_20_3 -> new BukkitCommandRegisterV1_20_4();
        case V1_20_5, V1_20_6 -> new BukkitCommandRegisterV1_20_6();
        case V1_21 -> new BukkitCommandRegisterV1_21();
        case V1_21_1 -> new BukkitCommandRegisterV1_21_1();
        case V1_21_3 -> new BukkitCommandRegisterV1_21_3();
        case V1_21_4 -> new BukkitCommandRegisterV1_21_4();
        case V1_21_5 -> new BukkitCommandRegisterV1_21_5();
        default -> throw new IllegalStateException("Unsupported version! use 1.16.5, 1.17.1, 1.18.2, 1.19.4, 1.20.x, 1.21.x. Version: '" + Version.VERSION.getVer() + "', GameVersion: '" + Version.getGameVersion() + "'");
    };
    @Override
    public BukkitCommandRegister create() {
        return register;
    }
}
