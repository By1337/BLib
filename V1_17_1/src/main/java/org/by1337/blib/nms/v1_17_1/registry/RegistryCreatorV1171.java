package org.by1337.blib.nms.v1_17_1.registry;

import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_17_R1.CraftParticle;
import org.bukkit.craftbukkit.v1_17_R1.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftNamespacedKey;
import org.bukkit.potion.PotionEffectType;
import org.by1337.blib.RegistryHelper;
import org.by1337.blib.nms.NMSAccessor;
import org.by1337.blib.registry.RegistryCreator;
import org.by1337.blib.util.Version;

@NMSAccessor(forClazz = RegistryCreator.class, forVersions = Version.V1_17_1)
public class RegistryCreatorV1171 implements RegistryCreator {
    @Override
    public Registry<RegistryHelper.Holder<PotionEffectType>> createPotionType() {
        return RegistryHelper.of(
                net.minecraft.core.Registry.MOB_EFFECT.iterator(),
                CraftPotionEffectType::new,
                mobEffect -> CraftNamespacedKey.fromMinecraft(net.minecraft.core.Registry.MOB_EFFECT.getKey(mobEffect))
        );
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
