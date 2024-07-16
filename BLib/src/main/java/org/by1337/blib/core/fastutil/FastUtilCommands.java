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
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.by1337.blib.BLib;
import org.by1337.blib.block.custom.CustomBlock;
import org.by1337.blib.block.custom.registry.BlockRegistry;
import org.by1337.blib.block.replacer.BlockReplaceFlags;
import org.by1337.blib.block.replacer.BlockReplaceTask;
import org.by1337.blib.block.replacer.type.ReplaceBlock;
import org.by1337.blib.block.replacer.type.impl.CustomBlockReplace;
import org.by1337.blib.block.replacer.type.impl.MaterialBlock;
import org.by1337.blib.command.Command;
import org.by1337.blib.command.CommandException;
import org.by1337.blib.command.CommandSyntaxError;
import org.by1337.blib.command.argument.*;
import org.by1337.blib.command.requires.RequiresPermission;
import org.by1337.blib.geom.IntAABB;
import org.by1337.blib.geom.Sphere;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.util.SpacedNameKey;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class FastUtilCommands {
    private static final Map<String, Integer> replaceFlags;
    public static final Command<CommandSender> SET;
    public static final Command<CommandSender> SHEM_PASTE = new Command<CommandSender>("shem")
            .requires(new RequiresPermission<>("blib.shem"))
            .requires(sender -> sender instanceof Player)
            .argument(new ArgumentSetList<>("shem", () -> {
                File file = new File(org.by1337.blib.core.BLib.getInstance().getDataFolder().getParentFile() + "/WorldEdit/schematics");
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

                File file = new File(org.by1337.blib.core.BLib.getInstance().getDataFolder().getParentFile() + "/WorldEdit/schematics/" + shem);
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
                    BLib.getApi().getMessage().error(t);
                }

            });

    static {
        Map<String, Integer> replaceFlags0 = new HashMap<>();
        replaceFlags = Collections.unmodifiableMap(replaceFlags0);

        replaceFlags0.put("UPDATE_NEIGHBORS", BlockReplaceFlags.UPDATE_NEIGHBORS);
        replaceFlags0.put("UPDATE_CLIENTS", BlockReplaceFlags.UPDATE_CLIENTS);
        replaceFlags0.put("UPDATE_INVISIBLE", BlockReplaceFlags.UPDATE_INVISIBLE);
        replaceFlags0.put("UPDATE_IMMEDIATE", BlockReplaceFlags.UPDATE_IMMEDIATE);
        replaceFlags0.put("UPDATE_KNOWN_SHAPE", BlockReplaceFlags.UPDATE_KNOWN_SHAPE);
        replaceFlags0.put("UPDATE_SUPPRESS_DROPS", BlockReplaceFlags.UPDATE_SUPPRESS_DROPS);
        replaceFlags0.put("UPDATE_MOVE_BY_PISTON", BlockReplaceFlags.UPDATE_MOVE_BY_PISTON);
        replaceFlags0.put("UPDATE_SUPPRESS_LIGHT", BlockReplaceFlags.UPDATE_SUPPRESS_LIGHT);
        replaceFlags0.put("UPDATE_NONE", BlockReplaceFlags.UPDATE_NONE);
        replaceFlags0.put("UPDATE_ALL", BlockReplaceFlags.UPDATE_ALL);
        replaceFlags0.put("UPDATE_ALL_IMMEDIATE", BlockReplaceFlags.UPDATE_ALL_IMMEDIATE);
        replaceFlags0.put("DONT_PLACE", BlockReplaceFlags.DONT_PLACE);

        SET = new Command<CommandSender>("set")
                .requires(new RequiresPermission<>("blib.set"))
                .requires(sender -> sender instanceof Player)
                .argument(new ArgumentReplaceBlockValue<>("block"))
                .argument(/*new BiArgument<>(
                        "flags",
                        new ArgumentFlagList<>("flags", replaceFlags.keySet().stream().toList()),
                        new ArgumentIntegerAllowedMath<>("flags", 0)
                )*/
                        new ArgumentIntegerAllowedMath<>("flags", 0)
                )
                .executor((sender, args) -> {
                    ReplaceBlock replaceBlock = (ReplaceBlock) args.getOrThrow("block", "use /bset <block>");
                    Player player = (Player) sender;
                    BukkitPlayer bPlayer = BukkitAdapter.adapt(player);
                    try {
                        Region region = WorldEdit.getInstance().getSessionManager().get(bPlayer).getSelection(bPlayer.getWorld());
                        BlockReplaceTask task = new BlockReplaceTask();

                        if (region instanceof EllipsoidRegion ellipsoidRegion) {
                            Sphere sphere = new Sphere(
                                    ellipsoidRegion.getCenter().getX(),
                                    ellipsoidRegion.getCenter().getY(),
                                    ellipsoidRegion.getCenter().getZ(),
                                    ellipsoidRegion.getRadius().getY()
                            );
                            IntAABB aabb = IntAABB.fromAABB(sphere.toAABB());
                            for (Vec3i vec3i : aabb.getAllPointsInAABB()) {
                                if (sphere.intersects(vec3i.toVec3d())) {
                                    task.addToReplace(vec3i, replaceBlock);
                                }
                            }
                        } else {
                            var min = region.getMinimumPoint();
                            var max = region.getMaximumPoint();
                            IntAABB aabb = new IntAABB(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
                            task.addToReplace(aabb, replaceBlock);
                        }

                        Object flags = args.getOrDefault("flags", new ArrayList<>());

                        if (flags instanceof List<?> list) {
                            if (list.isEmpty()) {
                                task.setFlag(BlockReplaceFlags.UPDATE_ALL + BlockReplaceFlags.UPDATE_SUPPRESS_DROPS);
                            } else {
                                int flag = 0;
                                for (Object s : list) {
                                    flag += replaceFlags.getOrDefault(String.valueOf(s), 0);
                                }
                                task.setFlag(flag);
                            }
                        } else if (flags instanceof Integer integer) {
                            task.setFlag(integer);
                        } else {
                            task.setFlag(BlockReplaceFlags.UPDATE_ALL + BlockReplaceFlags.UPDATE_SUPPRESS_DROPS);
                        }

                        final long time = System.currentTimeMillis();
                        task.start(player.getWorld()).whenComplete((s, t) -> {
                            BLib.getApi().getMessage().sendTranslatable(sender, "fast-util-complete", System.currentTimeMillis() - time);
                        });

                    } catch (IncompleteRegionException e) {
                        BLib.getApi().getMessage().sendTranslatable(sender, "fast-util-select-region");
                    }
                });
    }

    public static class ArgumentReplaceBlockValue<T> extends ArgumentSetList<T> {

        public ArgumentReplaceBlockValue(String name) {
            super(name, Collections.emptyList());
        }


        @Override
        public Object process(T sender, String str) throws CommandSyntaxError {
            if (str.isEmpty()) return null;
            try {
                return new MaterialBlock(Material.valueOf(str.toUpperCase(Locale.ENGLISH)));
            } catch (Throwable ignore) {
                if (str.startsWith("{cb:")) {
                    String[] params = str.substring(1, str.length() - 1).split(":");
                    // cb[0] space[1] name[2]
                    SpacedNameKey spacedNameKey = new SpacedNameKey(params[1], params[2]);
                    Optional<CustomBlock> customBlock = BlockRegistry.get().getCustomBlockOptional(spacedNameKey);
                    return customBlock.map(CustomBlockReplace::new).orElse(null);
                }
            }
            return null;
        }

        @Override
        public List<String> tabCompleter(T sender, String str) throws CommandSyntaxError {
            if (str.isEmpty())
                return blocks();
            return blocks().stream().filter(s -> s.startsWith(str)).toList();
        }

        private List<String> blocks() {
            List<String> list = Arrays.stream(Material.values()).filter(Material::isBlock).map(material -> material.name().toLowerCase(Locale.ENGLISH)).collect(Collectors.toList());
            Collection<CustomBlock> customBlocks = BlockRegistry.get().getAll();
            for (CustomBlock customBlock : customBlocks) {
                list.add("{cb:" + customBlock.getId() + "}");
            }
            return list;
        }
    }

}

