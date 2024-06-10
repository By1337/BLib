package org.by1337.blib.nms.v1_16_5.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.by1337.blib.command.SummonCommand;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonCommandV1_16_5 implements SummonCommand {
    public void spawn(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        try {
            CompoundTag entityData = new CompoundTag();
            if (nbt != null) {
                entityData = TagParser.parse(nbt);
            }

            ResourceLocation entityKey = new ResourceLocation(entityType);
            entityData.setString("id", entityKey.toString());
            Location location1 = location.getLocation();
            ServerLevel world = ((CraftWorld)location1.getWorld()).getHandle();
            Entity entity = EntityType.loadEntityRecursive_(entityData, world, entity1 -> {
                entity1.setPositionRotation(location1.getX(), location1.getY(), location1.getZ(), location1.getYaw(), location1.getPitch());
                return entity1;
            });
            if (entity == null) {
                throw new SimpleCommandExceptionType(new TranslatableComponent("commands.summon.failed")).create();
            }

            if (entity instanceof Mob) {
                ((Mob)entity).prepare(world, world.getDamageScaler(entity.getChunkCoordinates()), MobSpawnType.COMMAND, null, null);
            }

            if (!world.addAllEntitiesSafely(entity, SpawnReason.COMMAND)) {
                throw new SimpleCommandExceptionType(new TranslatableComponent("commands.summon.failed.uuid")).create();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }
    }
}
