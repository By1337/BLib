package org.by1337.blib.core.fastutil;

import org.bukkit.plugin.Plugin;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.fastutil.FastUtilApi;
import org.by1337.blib.fastutil.FastUtilSetting;
import org.by1337.blib.nms.v1_16_5.fastutil.BlockReplacerManagerv165;
import org.by1337.blib.util.Version;

public class InitFastUtil {
    public static void setup(FastUtilApi fastUtil){
        switch (Version.VERSION){
            case V1_16_5 -> {
                fastUtil.setBlockReplacerManager(new BlockReplacerManagerv165(
                        FastUtilApi.getSetting(),
                        FastUtilApi.getMessage(),
                        FastUtilApi.getPlugin()
                ));
            }
        }
    }
}
