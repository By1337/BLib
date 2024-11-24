package org.by1337.blib.configuration.reflection;

import com.google.gson.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.by1337.blib.configuration.YamlContext;
import org.by1337.blib.configuration.YamlValue;
import org.by1337.blib.configuration.adapter.AdapterRegistry;
import org.by1337.blib.nbt.NBT;
import org.by1337.blib.nbt.impl.*;

import java.util.*;

public class GsonYamlConvertor {
    private static final Gson GSON = new Gson();

    public static <T> T deserializeFromYaml(Class<T> tClass, YamlContext context) {
        return deserializeFromYaml(tClass, context, GSON);
    }

    public static <T> T deserializeFromYaml(Class<T> tClass, YamlContext context, Gson gson) {
        return gson.fromJson(convertYamlToJson(context.getHandle()), tClass);
    }

    public static <T> T deserializeFromGson(Class<T> tClass, JsonElement jsonElement) {
        return deserializeFromGson(tClass, jsonElement, GSON);
    }

    public static <T> T deserializeFromGson(Class<T> tClass, JsonElement jsonElement, Gson gson) {
        return gson.fromJson(jsonElement, tClass);
    }

    public static <T> T deserializeFromNBT(Class<T> tClass, NBT nbt, Gson gson) {
        return deserializeFromGson(tClass, convertNBTToJson(nbt), gson);
    }

    public static <T> T deserializeFromNBT(Class<T> tClass, NBT nbt) {
        return deserializeFromGson(tClass, convertNBTToJson(nbt));
    }

    public static JsonElement convertYamlToJson(Object o) {
        if (o instanceof YamlValue yamlValue){
            return convertYamlToJson(yamlValue.unpack());
        }
        if (o instanceof MemorySection memorySection) {
            JsonObject jsonObject = new JsonObject();
            for (String key : memorySection.getKeys(false)) {
                jsonObject.add(key, convertYamlToJson(memorySection.get(key)));
            }
            return jsonObject;
        }
        if (o instanceof Map<?,?> map){
            JsonObject jsonObject = new JsonObject();
            for (Object key : map.keySet()) {
                jsonObject.add(String.valueOf(key), convertYamlToJson(map.get(key)));
            }
            return jsonObject;
        }
        if (o instanceof YamlContext context) {
            return convertYamlToJson(context.getHandle());
        }
        if (o instanceof Number number) {
            return new JsonPrimitive(number);
        }
        if (o instanceof String s) {
            return new JsonPrimitive(s);
        }
        if (o instanceof Character c) {
            return new JsonPrimitive(c);
        }
        if (o instanceof Boolean b) {
            return new JsonPrimitive(b);
        }
        if (o instanceof Collection<?> list) {
            JsonArray array = new JsonArray();
            for (Object object : list) {
                array.add(convertYamlToJson(object));
            }
            return array;
        }
        if (AdapterRegistry.hasAdapter(o.getClass()) || AdapterRegistry.hasPrimitiveAdapter(o.getClass())) {
            return convertYamlToJson(AdapterRegistry.serialize(o));
        }
        throw new IllegalStateException("Unknown type " + o.getClass().getCanonicalName() + " " + o);
    }

    public static YamlContext serializeToYaml(Object source) {
        return serializeToYaml(source, GSON);
    }

    public static YamlContext serializeToYaml(Object source, Gson gson) {
        JsonElement element = gson.toJsonTree(source);
        if (!(element instanceof JsonObject)) throw new IllegalStateException();
        return (YamlContext) convertJsonToYaml(element);
    }

    public static YamlContext convertToYamFromJson(JsonObject jsonObject) {
        return (YamlContext) convertJsonToYaml(jsonObject);
    }

    private static Object convertJsonToYaml(JsonElement o) {
        if (o instanceof JsonObject jsonObject) {
            YamlContext context = new YamlContext(new YamlConfiguration());
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                context.set(entry.getKey(), convertJsonToYaml(entry.getValue()));
            }
            return context;
        } else if (o instanceof JsonArray array) {
            List<Object> list = new ArrayList<>();
            for (JsonElement element : array) {
                list.add(convertJsonToYaml(element));
            }
            return list;
        } else if (o instanceof JsonPrimitive primitive) {
            if (primitive.isNumber()) return primitive.getAsNumber();
            if (primitive.isBoolean()) return primitive.getAsBoolean();
            return primitive.getAsString();
        } else if (o instanceof JsonNull) {
            return null;
        } else {
            throw new IllegalStateException("Unknown type " + o.getClass());
        }
    }

    public static CompoundTag convertYamlToNBT(YamlContext section) {
        return convertYamlToNBT(section.getHandle());
    }

    public static CompoundTag convertYamlToNBT(MemorySection section) {
        return (CompoundTag) convertYamlToNBT((Object) section);
    }

    private static NBT convertYamlToNBT(Object o) {
        if (o instanceof MemorySection memorySection) {
            CompoundTag compoundTag = new CompoundTag();
            for (String key : memorySection.getKeys(false)) {
                compoundTag.putTag(key, convertYamlToNBT(memorySection.get(key)));
            }
            return compoundTag;
        }
        if (o instanceof YamlContext context) {
            return convertYamlToNBT(context.getHandle());
        }
        if (o instanceof YamlValue yamlValue) {
            return convertYamlToNBT(yamlValue.unpack());
        }
        if (o instanceof Number n) {
            return convertNumberToNbt(n);
        }
        if (o instanceof String s) {
            return new StringNBT(s);
        }
        if (o instanceof Character c) {
            return new StringNBT(String.valueOf(c));
        }
        if (o instanceof Boolean b) {
            return new StringNBT(String.valueOf(b));
        }
        if (o instanceof List<?> list) {
            List<NBT> list1 = new ArrayList<>();
            for (Object object : list) {
                list1.add(convertYamlToNBT(object));
            }
            return convertNBTList(list1);
        }
        if (AdapterRegistry.hasAdapter(o.getClass()) || AdapterRegistry.hasPrimitiveAdapter(o.getClass())) {
            return convertYamlToNBT(AdapterRegistry.serialize(o));
        }
        throw new IllegalStateException("Unknown type " + o);
    }

    public static CompoundTag convertJsonToNBT(JsonObject jsonObject) {
        return (CompoundTag) jsonToNBT(jsonObject);
    }

    public static JsonObject convertCompoundTagToJson(CompoundTag compoundTag) {
        return (JsonObject) convertNBTToJson(compoundTag);
    }

    public static JsonElement convertNBTToJson(NBT nbt) {
        if (nbt instanceof CompoundTag compoundTag) {
            JsonObject object = new JsonObject();
            for (Map.Entry<String, NBT> entry : compoundTag.getTags().entrySet()) {
                object.add(entry.getKey(), convertNBTToJson(entry.getValue()));
            }
            return object;
        }
        Object value = nbt.getAsObject();
        if (value instanceof List<?> list) {
            JsonArray array = new JsonArray();
            for (Object o : list) {
                array.add(convertNBTToJson((NBT) o));
            }
            return array;
        } else if (value instanceof Number n) {
            return new JsonPrimitive(n);
        } else if (value instanceof Object[] arr) {
            JsonArray array = new JsonArray();
            for (Object o : arr) {
                array.add(convertNBTToJson((NBT) o));
            }
            return array;
        } else if (value instanceof String s) {
            return new JsonPrimitive(s);
        }
        throw new IllegalStateException("Unknown type " + value + " in " + nbt.getClass());
    }

    private static NBT jsonToNBT(JsonElement element) {
        if (element instanceof JsonPrimitive primitive) {
            if (primitive.isNumber()) {
                return convertNumberToNbt(primitive.getAsNumber());
            } else {
                return new StringNBT(primitive.getAsString());
            }
        } else if (element instanceof JsonObject object) {
            CompoundTag compoundTag = new CompoundTag();
            for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                compoundTag.putTag(entry.getKey(), jsonToNBT(entry.getValue()));
            }
            return compoundTag;
        } else if (element instanceof JsonArray array) {
            List<NBT> list = new ArrayList<>();
            for (JsonElement jsonElement : array) {
                list.add(jsonToNBT(jsonElement));
            }
            return convertNBTList(list);
        }
        throw new IllegalStateException("Unknown type " + element);
    }

    private static NBT convertNumberToNbt(Number n) {
        if (n instanceof Long l) {
            return LongNBT.valueOf(l);
        } else if (n instanceof Byte b) {
            return ByteNBT.valueOf(b);
        } else if (n instanceof Double d) {
            return new DoubleNBT(d);
        } else if (n instanceof Float f) {
            return new FloatNBT(f);
        } else if (n instanceof Integer i) {
            return IntNBT.valueOf(i);
        } else if (n instanceof Short i) {
            return ShortNBT.valueOf(i);
        }
        throw new IllegalStateException("Unknown type " + n.getClass());
    }

    private static NBT convertNBTList(List<NBT> list) {
        if (list.isEmpty()) return new ListNBT();
        NBT nbt = list.get(0);
        if (nbt instanceof ByteNBT) {
            byte[] arr = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = ((ByteNBT) list.get(i)).getValue();
            }
            return new ByteArrNBT(arr);
        } else if (nbt instanceof IntNBT) {
            int[] arr = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = ((IntNBT) list.get(i)).getValue();
            }
            return new IntArrNBT(arr);
        } else if (nbt instanceof LongNBT) {
            long[] arr = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = ((LongNBT) list.get(i)).getValue();
            }
            return new LongArrNBT(arr);
        } else {
            return new ListNBT(list);
        }
    }
}
