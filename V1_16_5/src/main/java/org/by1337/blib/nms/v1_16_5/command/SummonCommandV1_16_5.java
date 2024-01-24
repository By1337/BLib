package org.by1337.blib.nms.v1_16_5.command;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.by1337.blib.command.SummonCommand;
import org.by1337.blib.world.BLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummonCommandV1_16_5 implements SummonCommand {

    @Override
    public void spawn(@NotNull String entityType, @NotNull BLocation location, @Nullable String nbt) {
        try {
            NBTTagCompound entityData = new NBTTagCompound();
            if (nbt != null){
                entityData = MojangsonParser.parse(nbt);
            }
            MinecraftKey entityKey = new MinecraftKey(entityType);
            entityData.setString("id", entityKey.toString());
            Location location1 = location.getLocation();
            WorldServer world = ((CraftWorld) location1.getWorld()).getHandle();
            Entity entity = EntityTypes.a(entityData, world, (entity1) -> {
                entity1.setPositionRotation(location1.getX(), location1.getY(), location1.getZ(), location1.getYaw(), location1.getPitch());
                return entity1;
            });
            if (entity == null) {
                throw new SimpleCommandExceptionType(new ChatMessage("commands.summon.failed")).create();
            } else {
                if (entity instanceof EntityInsentient) {
                    ((EntityInsentient) entity).prepare(world, world.getDamageScaler(entity.getChunkCoordinates()), EnumMobSpawn.COMMAND,  null,  null);
                }

                if (!world.addAllEntitiesSafely(entity, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.COMMAND)) {
                    throw new SimpleCommandExceptionType(new ChatMessage("commands.summon.failed.uuid")).create();
                } else {
                    //System.out.println(new ChatMessage("commands.summon.success", new Object[]{entity.getScoreboardDisplayName()}));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}