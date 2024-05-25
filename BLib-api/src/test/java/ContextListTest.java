import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.YamlValue;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

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

        Map<String, List<Vector>> map = context.getMapList("map", Vector.class);

        Assert.assertEquals(map.size(), 2);
        Assert.assertNotNull(map.get("item-1"));
        Assert.assertEquals(map.get("item-1").get(2), new Vector(3, 3, 3));

    }

    @Test
    public void run2() throws InvalidConfigurationException {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.loadFromString(yaml);
        YamlContext context = new YamlContext(configuration);

        Map<String, List<Vector>> map = context.getAsYamlValue("map").getAsMap(v -> v.getAsList(YamlValue::getAsVector));

        Assert.assertEquals(map.size(), 2);
        Assert.assertNotNull(map.get("item-1"));
        Assert.assertEquals(map.get("item-1").get(2), new Vector(3, 3, 3));

    }
}
