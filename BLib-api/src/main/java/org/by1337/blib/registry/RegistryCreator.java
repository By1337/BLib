package org.by1337.blib.registry;

import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;

public interface RegistryCreator {
    Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType();
}
