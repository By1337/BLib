package org.by1337.blib.core.fastutil;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.argument.ArgumentSetList;
import org.by1337.blib.command.requires.RequiresPermission;
import org.by1337.blib.fastutil.FastUtilApi;
import org.by1337.blib.fastutil.block.BlockReplaceTask;
import org.by1337.blib.geom.IntAABB;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.lang.Lang;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FastUtilCommands {
    public static final Command<CommandSender> SET = new Command<CommandSender>("set")
            .requires(new RequiresPermission<>("blib.set"))
            .requires(sender -> sender instanceof Player)
            .argument(new ArgumentMaterialValue<>("material"))
            .argument(new ArgumentSetList<>("debug", List.of("debug")))
            .executor((sender, args) -> {
                Material material = (Material) args.getOrThrow("material", "use /bset <material>");
                Player player = (Player) sender;
                BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
                try {
                    Region region = WorldEdit.getInstance().getSessionManager().get(bPlayer).getSelection(bPlayer.getWorld());
                    var min = region.getMinimumPoint();
                    var max = region.getMaximumPoint();
                    IntAABB aabb = new IntAABB(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
                    BlockReplaceTask task = new BlockReplaceTask();
                    task.setApplyPhysics(false);
                    task.setDebug(args.containsKey("debug"));
                    task.addToReplace(aabb, material);
                    long l = System.nanoTime();
                    task.start(player.getWorld()).whenComplete((s, t) -> {
                        if (s != null) {
                            FastUtilApi.getMessage().sendTranslatable(sender, "fast-util-complete", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - l));
                            if (args.containsKey("debug")) {
                                FastUtilApi.getMessage().log(s.getProfiler().report());
                                FastUtilApi.getMessage().sendMsg(sender, s.getProfiler().report());
                            }
                        } else {
                            FastUtilApi.getMessage().sendTranslatable(sender, "fast-util-failed-complete");
                        }
                        if (t != null) {
                            FastUtilApi.getMessage().error(t);
                        }
                    });
                } catch (IncompleteRegionException e) {
                    FastUtilApi.getMessage().sendTranslatable(sender, "fast-util-select-region");
                }
            });
    public static final Command<CommandSender> SHEM_PASTE = new Command<CommandSender>("shem")
            .requires(new RequiresPermission<>("blib.shem"))
            .requires(sender -> sender instanceof Player)
            .argument(new ArgumentSetList<>("shem", () -> {
                File file = new File(FastUtilApi.getPlugin().getDataFolder().getParentFile() + "/WorldEdit/schematics");
                List<String> result = new ArrayList<>();
                for (File listFile : file.listFiles()) {
                    result.add(listFile.getName());
                }
                return result;
            }))
            .executor((sender, args) -> {
                Player player = (Player) sender;
                Location location = player.getLocation();
                String shem = (String) args.getOrThrow("shem", "/.shem <shem file>");

                File file = new File(FastUtilApi.getPlugin().getDataFolder().getParentFile() + "/WorldEdit/schematics/" + shem);
                if (!file.exists()) {
                    throw new CommandException("file isn't exist!");
                }
                ClipboardFormat format = ClipboardFormats.findByFile(file);
                BlockReplaceTask task = new BlockReplaceTask();
                try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
                    Clipboard clipboard = reader.read();
                    BlockVector3 minPoint = clipboard.getMinimumPoint();
                    for (int y = minPoint.getY(); y <= clipboard.getMaximumPoint().getY(); y++) {
                        for (int x = minPoint.getX(); x <= clipboard.getMaximumPoint().getX(); x++) {
                            for (int z = minPoint.getZ(); z <= clipboard.getMaximumPoint().getZ(); z++) {
                                BlockState block = clipboard.getBlock(BlockVector3.at(x, y, z));

                                int finalX = x + location.getBlockX() - minPoint.getX();
                                int finalY = y + location.getBlockY() - minPoint.getY();
                                int finalZ = z + location.getBlockZ() - minPoint.getZ();

                                task.addToReplace(new Vec3i(finalX, finalY, finalZ), block);

                            }
                        }
                    }
                    task.start(player.getWorld());
                } catch (Throwable t) {
                    FastUtilApi.getMessage().error(t);
                }

            });

    public static class ArgumentMaterialValue<T> extends ArgumentSetList<T> {


        public ArgumentMaterialValue(String name) {
            super(name, Collections.emptyList());
        }


        @Override
        public Object process(T sender, String str) throws CommandSyntaxError {
            if (str.isEmpty()) return null;
            try {
                return Material.valueOf(str.toUpperCase(Locale.ENGLISH));
            } catch (Throwable ignore) {

            }
            return null;

        }

        @Override
        public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
            if (str.isEmpty())
                return Arrays.stream(Material.values()).filter(Material::isBlock).map(material -> material.name().toLowerCase(Locale.ENGLISH)).toList();
            return Arrays.stream(Material.values()).filter(Material::isBlock).map(material -> material.name().toLowerCase(Locale.ENGLISH)).filter(s -> s.startsWith(str)).toList();
        }
    }

}

