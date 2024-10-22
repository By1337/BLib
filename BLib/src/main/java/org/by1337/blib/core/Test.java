package org.by1337.blib.core;

import blib.com.mojang.serialization.Codec;
import blib.com.mojang.serialization.codecs.RecordCodecBuilder;
import org.bukkit.configuration.file.YamlConfiguration;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.YamlOps;
import org.by1337.blib.configuration.YamlValue;
import org.by1337.blib.nbt.NbtOps;

public class Test {

    public static void main(String[] args) {
        Codec<TestClass> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("test").forGetter(TestClass::getTest),
                Codec.INT.fieldOf("test1").forGetter(TestClass::getTest1),
                Codec.INT.fieldOf("test2").forGetter(TestClass::getTest2)
        ).apply(instance, TestClass::new));

        YamlValue val = CODEC.encodeStart(YamlOps.INSTANCE, new TestClass(13, 13, 13)).getOrThrow();
        YamlConfiguration root = new YamlConfiguration();
        YamlContext.getMemorySection(val, root);
        System.out.println(root.saveToString());
        System.out.println(CODEC.encodeStart(NbtOps.INSTANCE, new TestClass(13, 13, 13)).getOrThrow());
    }

    private static class TestClass{
        private int test;
        private int test1;
        private int test2;

        public TestClass(int test, int test1, int test2) {
            this.test = test;
            this.test1 = test1;
            this.test2 = test2;
        }

        public int getTest() {
            return test;
        }

        public int getTest1() {
            return test1;
        }

        public int getTest2() {
            return test2;
        }
    }
}
