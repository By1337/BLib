package org.by1337.blib.nms.v1_20_1.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_20_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;
import org.by1337.blib.registry.RegistryCreator;

public class RegistryCreatorV1201 implements RegistryCreator {
    @Override
    public Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType() {
        return RegistryHelper.of(Registry.POTION_EFFECT_TYPE.iterator());
    }

    @Override
    public Registry<RegistryHelper.Holder<Particle>> createParticleType() {
        return RegistryHelper.of(
                BuiltInRegistries.PARTICLE_TYPE.iterator(),
                CraftParticle::toBukkit,
                particleType -> CraftNamespacedKey.fromMinecraft(BuiltInRegistries.PARTICLE_TYPE.getKey(particleType))
        );
    }
}
