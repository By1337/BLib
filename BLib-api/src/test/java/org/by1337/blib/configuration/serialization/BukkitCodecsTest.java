package org.by1337.blib.configuration.serialization;


import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.Material;
import org.by1337.blib.configuration.YamlOps;
import org.by1337.blib.configuration.YamlValue;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BukkitCodecsTest {

    @Test
    public void run() {
        Codec<TestClass> codec = RecordCodecBuilder.create(instance -> instance.group(
                BukkitCodecs.createEnumCodec(Material.class).fieldOf("material").forGetter(TestClass::getMaterial)
        ).apply(instance, TestClass::new));

        YamlValue val = codec.encodeStart(YamlOps.INSTANCE, new TestClass(Material.STONE)).getOrThrow();
        TestClass testClass = codec.decode(YamlOps.INSTANCE, val).getOrThrow().getFirst();
        assertEquals(Material.STONE, testClass.material);


        //System.out.println(codec.encodeStart(NbtOps.INSTANCE, new TestClass(Material.STONE)).getOrThrow());
    }

    private static class TestClass {
        private Material material;

        public TestClass(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}