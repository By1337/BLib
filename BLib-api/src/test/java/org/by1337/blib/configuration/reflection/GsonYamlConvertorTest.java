package org.by1337.blib.configuration.reflection;

import com.google.gson.JsonObject;
import junit.framework.TestCase;
import org.by1337.blib.geom.AABB;
import org.by1337.blib.geom.Vec3i;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.by1337.blib.util.Direction;
import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GsonYamlConvertorTest extends TestCase {

    public void testConvert() {
        SuperClazz superClazz = new SuperClazz();
        var context = GsonYamlConvertor.serializeToYaml(superClazz);
        SuperClazz superClazz1 = GsonYamlConvertor.deserializeFromYaml(SuperClazz.class, context);
        Assert.assertEquals(superClazz1, superClazz);

        CompoundTag compoundTag = GsonYamlConvertor.convertJsonToNBT((JsonObject) GsonYamlConvertor.convertYamlToJson(context));
        Assert.assertEquals(GsonYamlConvertor.convertNBTToJson(compoundTag), GsonYamlConvertor.convertYamlToJson(context));
    }

    private static class SuperClazz {
        private final Vec3i v = new Vec3i(10, 0, 23);
        private final Vec3i v1 = new Vec3i(10, 0, 23);
        private final Vec3i v2 = new Vec3i(10, 0, 23);
        private final AABB aabb = new AABB(10, 20, 32, 43, 54, 122);
        private final List<AABB> list = List.of(aabb, aabb, aabb, aabb, aabb, aabb);
        private final AABB[] array = new AABB[]{aabb, aabb};
        private final Map<String, Vec3i> map = Map.of("key", v, "key2", v1);
        private final Direction direction = Direction.DOWN;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SuperClazz that = (SuperClazz) o;
            return Objects.equals(v, that.v) && Objects.equals(v1, that.v1) && Objects.equals(v2, that.v2) && Objects.equals(aabb, that.aabb) && Objects.equals(list, that.list) && Arrays.equals(array, that.array) && Objects.equals(map, that.map) && direction == that.direction;
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(v, v1, v2, aabb, list, map, direction);
            result = 31 * result + Arrays.hashCode(array);
            return result;
        }

        @Override
        public String toString() {
            return "SuperClazz{" +
                    "v=" + v +
                    ", v1=" + v1 +
                    ", v2=" + v2 +
                    ", aabb=" + aabb +
                    ", list=" + list +
                    ", array=" + Arrays.toString(array) +
                    ", map=" + map +
                    ", direction=" + direction +
                    '}';
        }
    }
}