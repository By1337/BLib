package org.by1337.blib.nms.v1_18_2.registry;

import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_18_R2.CraftParticle;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;
import org.by1337.blib.registry.RegistryCreator;

public class RegistryCreatorV1182 implements RegistryCreator {
    @Override
    public Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType() {
        return RegistryHelper.of(Registry.POTION_EFFECT_TYPE.iterator());
    }

    @Override
    public Registry<RegistryHelper.Holder<Particle>> createParticleType() {
        return RegistryHelper.of(
                net.minecraft.core.Registry.PARTICLE_TYPE.iterator(),
                CraftParticle::toBukkit,
                particleType -> CraftNamespacedKey.fromMinecraft(net.minecraft.core.Registry.PARTICLE_TYPE.getKey(particleType))
        );
    }
}
