package org.by1337.blib.nms.V1_21.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.by1337.blib.command.SummonCommand;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonCommandV1_21 implements SummonCommand {
    @Override
    public void spawn(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        try {
            CompoundTag entityData = new CompoundTag();
            if (nbt != null) {
                entityData = TagParser.parseTag(nbt);
            }
            ResourceLocation entityKey = ResourceLocation.parse(entityType);
            entityData.putString("id", entityKey.toString());
            Location location1 = location.getLocation();
            ServerLevel world = ((CraftWorld) location1.getWorld()).getHandle();
            Entity entity = EntityType.loadEntityRecursive(entityData, world, (entity1) -> {
                entity1.moveTo(location1.getX(), location1.getY(), location1.getZ(), location1.getYaw(), location1.getPitch());
                return entity1;
            });
            if (entity == null) {
                throw new SimpleCommandExceptionType(Component.translatable("commands.summon.failed")).create();
            } else {
                if (entity instanceof Mob) {
                    ((Mob) entity).finalizeSpawn(world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.COMMAND, null);
                }

                if (!world.tryAddFreshEntityWithPassengers(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.COMMAND)) {
                    throw new SimpleCommandExceptionType(Component.translatable("commands.summon.failed.uuid")).create();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
