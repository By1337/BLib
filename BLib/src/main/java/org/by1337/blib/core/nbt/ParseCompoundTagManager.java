package org.by1337.blib.core.nbt;

import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.nms.V1_20_6.nbt.ParseCompoundTagV206;
import org.by1337.blib.nms.v1_16_5.nbt.ParseCompoundTagV165;
import org.by1337.blib.nms.v1_17_1.nbt.ParseCompoundTagV171;
import org.by1337.blib.nms.v1_18_2.nbt.ParseCompoundTagV182;
import org.by1337.blib.nms.v1_19_4.nbt.ParseCompoundTagV194;
import org.by1337.blib.nms.v1_20_1.nbt.ParseCompoundTagV201;
import org.by1337.blib.nms.v1_20_2.nbt.ParseCompoundTagV202;
import org.by1337.blib.nms.v1_20_4.nbt.ParseCompoundTagV204;
import org.by1337.blib.util.Version;

public class ParseCompoundTagManager {
    private final static ParseCompoundTag parseCompoundTag = switch (Version.VERSION){
        case V1_16_5 -> new ParseCompoundTagV165();
        case V1_17_1 -> new ParseCompoundTagV171();
        case V1_18_2 -> new ParseCompoundTagV182();
        case V1_19_4 -> new ParseCompoundTagV194();
        case V1_20_1 -> new ParseCompoundTagV201();
        case V1_20_2 -> new ParseCompoundTagV202();
        case V1_20_4, V1_20_3 -> new ParseCompoundTagV204();
        case V1_20_5, V1_20_6 -> new ParseCompoundTagV206();
        default -> throw new UnsupportedOperationException("Unsupported version! " + Version.getGameVersion());
    };

    public static ParseCompoundTag get(){
        return parseCompoundTag;
    }
}
