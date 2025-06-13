package org.by1337.blib.nms.v1_20_2.registry;

import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;
import org.by1337.blib.registry.RegistryCreator;

public class RegistryCreatorV1202AndNewer implements RegistryCreator {

    @Override
    public Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType() {
        return RegistryHelper.of(Registry.POTION_EFFECT_TYPE.iterator());
    }

    @Override
    public Registry<RegistryHelper.Holder<Particle>> createParticleType() {
        return RegistryHelper.of(Registry.PARTICLE_TYPE.iterator());
    }
}
