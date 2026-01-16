package org.by1337.blib.registry;

import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;

public interface RegistryCreator {
    RegistryCreator INSTANCE = new RegistryCreator() {
        @Override
        public Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType() {
            return RegistryHelper.MOB_EFFECT;
        }

        @Override
        public Registry<RegistryHelper.Holder<Particle>> createParticleType() {
            return RegistryHelper.PARTICLE_TYPE;
        }
    };
    Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType();
    Registry<RegistryHelper.Holder<Particle>> createParticleType();
}
