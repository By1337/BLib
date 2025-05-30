package org.by1337.blib.nms.v1_18_2.registry;

import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;
import org.by1337.blib.registry.RegistryCreator;

public class RegistryCreatorV1182AndNewer implements RegistryCreator {
    @Override
    public Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType() {
        return RegistryHelper.of(Registry.POTION_EFFECT_TYPE.iterator());
    }
}
