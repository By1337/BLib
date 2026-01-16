package org.by1337.blib;

import org.bukkit.Bukkit;
import org.by1337.blib.block.replacer.PooledBlockReplacer;
import org.by1337.blib.chat.util.Message;
import org.by1337.blib.command.BukkitCommandRegister;
import org.by1337.blib.command.CommandUtil;
import org.by1337.blib.inventory.FakeTitleFactory;
import org.by1337.blib.inventory.InventoryUtil;
import org.by1337.blib.inventory.ItemStackSerialize;
import org.by1337.blib.nbt.ParseCompoundTag;
import org.by1337.blib.registry.RegistryCreator;
import org.by1337.blib.text.ComponentToANSI;
import org.by1337.blib.text.LegacyConvertor;
import org.by1337.blib.unsafe.BLibUnsafe;
import org.by1337.blib.util.AsyncCatcher;
import org.by1337.blib.world.BlockUtil;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

@SuppressWarnings({"removal"})
public interface Api {
   default @NotNull Logger getLogger(){
       return LoggerHolder.logger;
   }

    @NotNull
    default CommandUtil getCommandUtil() {
        return CommandUtil.INSTANCE;
    }

   default @NotNull AsyncCatcher getAsyncCatcher(){
        return AsyncCatcher.INSTANCE;
   }

   default @NotNull Message getMessage(){
      return LoggerHolder.messaage; //todo
   }

    default @NotNull ItemStackSerialize getItemStackSerialize() {
        return ItemStackSerialize.INSTANCE;
    }

    @Deprecated
    default @NotNull FakeTitleFactory getFakeTitleFactory() {
        return FakeTitleFactory.INSTANCE;
    }

    default @NotNull BukkitCommandRegister getBukkitCommandRegister() {
        return BukkitCommandRegister.INSTANCE;
    }

    default @NotNull ParseCompoundTag getParseCompoundTag() {
        return ParseCompoundTag.INSTANCE;
    }

    default @NotNull LegacyConvertor getLegacyConvertor() {
        return LegacyConvertor.INSTANCE;
    }

   default @NotNull ComponentToANSI getComponentToANSI(){
       return ComponentToANSI.INSTANCE;
   }

    @NotNull PooledBlockReplacer getPooledBlockReplacer();

   default @NotNull BLibUnsafe getUnsafe(){
       return BLibUnsafe.INSTANCE;
   }

    default @NotNull InventoryUtil getInventoryUtil() {
        return InventoryUtil.INSTANCE;
    }

    default @NotNull RegistryCreator getRegistryCreator() {
        return RegistryCreator.INSTANCE;
    }

    default @NotNull BlockUtil getBlockUtil() {
        throw new UnsupportedOperationException();
    }

    class LoggerHolder{
        static final Logger logger;
        static final Message messaage;
        static {
           var plug = Bukkit.getPluginManager().getPlugin("BDevCore");
           if (plug != null){
               logger = plug.getLogger();
           }else {
               logger = Logger.getLogger("BDevCore");
           }
            messaage = new Message(logger);
        }
    }
}
