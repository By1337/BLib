package org.by1337.blib.configuration;

import org.bukkit.Color;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.by1337.blib.geom.Vec3d;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContextListTest {
    private static String yaml = """
            map:
              item-1:
                - x: 1
                  y: 1
                  z: 1
                - x: 2
                  y: 2
                  z: 2
                - x: 3
                  y: 3
                  z: 3
              item-3:
                - x: 1
                  y: 1
                  z: 1
                - x: 2
                  y: 2
                  z: 2
                - x: 3
                  y: 3
                  z: 3
            """;

    @Test
    public void run() throws InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.loadFromString(yaml);
        YamlContext context = new YamlContext(configuration);

        Map<String, List<Vec3d>> map = context.getMapList("map", Vec3d.class);

        assertEquals(map.size(), 2);
        assertNotNull(map.get("item-1"));
        assertEquals(map.get("item-1").get(2), new Vec3d(3, 3, 3));

    }

    @Test
    public void run2() throws InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.loadFromString(yaml);
        YamlContext context = new YamlContext(configuration);

        Map<String, List<Vec3d>> map = context.getAsYamlValue("map").getAsMap(v -> v.getAsList(YamlValue::getAsVec3d));

        assertEquals(map.size(), 2);
        assertNotNull(map.get("item-1"));
        assertEquals(map.get("item-1").get(2), new Vec3d(3, 3, 3));

        YamlContext ctx = new YamlContext(new YamlConfiguration());
        ctx.set("test", new Vec3d(3, 3, 3));
        ctx.set("test2", Color.AQUA);

        assertEquals(ctx.getAs("test", Vec3d.class), new Vec3d(3, 3, 3));
        assertEquals(ctx.getAs("test2", Color.class), Color.AQUA);
    }
}
