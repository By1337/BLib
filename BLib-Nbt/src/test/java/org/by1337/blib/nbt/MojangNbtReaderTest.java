package org.by1337.blib.nbt;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.*;
import net.minecraft.network.FriendlyByteBuf;
import org.by1337.blib.nbt.impl.*;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class MojangNbtReaderTest {
    private String nbt = "{Double:45.0d,Double2:45.5d,arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],b_1:1b,b_2:0b,b_3:0b,b_4:1b,byte:127b,byte_arr:[B;0B,127B,89B],float:2.0f,float2:2.5f,floatList:[1.0f,1.1f,23.5f],int:43,int_arr:[I;99,345,211],list:[123,123],list_compound_tag:[{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]},{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]},{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]}],long:123L,long_arr:[L;882883L,34213L,4322L],\"s'tring2\":'asa\"a\\'s',short:34s,'str\"in\\'g2':'asa\"a\\'s','str\"ing2':'asa\"a\\'s',strList:[\"string\",\"string1\",\"string2\"],string:\"str\",string1:\"asa'as\",string2:'asa\"a\\'s',tags:{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]}}";

    @Test
    public void readTest() throws Throwable {
        var nms = (net.minecraft.nbt.CompoundTag) TagParser.parseTag(nbt);
        CompoundTag nbt = NBTParser.parseAsCompoundTag(this.nbt);
        NbtUtil.assertEqualsTags(NbtUtil.convertFromNms(nms), nbt);

        for (String s : nbt.getTags().keySet()) {
            NBT tag = nbt.get(s);
            var nmsTag = nms.get(s);
            byte[] array = toByteArray(nmsTag::write);
            byte[] blib = toByteArray(out -> {
                MojangNbtReader.MojangNBTSerializer type = MojangNbtReader.MojangNBTSerializer.getMojangTypeByNbtType(tag.getType());
                type.write(out, tag);
            });
            Assert.assertArrayEquals(array, blib);
        }

        byte[] compressedNms = applyOutputStream(out -> NbtIo.writeCompressed(nms, out));
        byte[] compressedBLib = applyOutputStream(out -> MojangNbtReader.writeCompressed(nbt, out));
        Assert.assertArrayEquals(compressedNms, compressedBLib);

        NbtUtil.assertEqualsTags(
                MojangNbtReader.readCompressed(new ByteArrayInputStream(compressedBLib)),
                MojangNbtReader.readCompressed(new ByteArrayInputStream(compressedNms))
        );

        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
        byteBuf.writeNbt(nms);
        byteBuf.resetReaderIndex();
        NbtUtil.assertEqualsTags(
                nbt,
                MojangNbtReader.readUnnamedTag(new ByteBufInputStream(byteBuf))
        );
        byteBuf.release();

    }
    @Test
    public void readNullTest() throws Throwable {
        byte[] emptyNbt = new byte[]{0};
        Assert.assertNull(
                MojangNbtReader.readUnnamedTag(new DataInputStream(new ByteArrayInputStream(emptyNbt)))
        );
    }

    public byte[] applyOutputStream(ThrowableConsumer<OutputStream> consumer) throws Throwable {
        try (var buffer = new ByteArrayOutputStream()) {
            consumer.accept(buffer);
            return buffer.toByteArray();
        }
    }

    @Test
    public void testByteArray() throws Throwable {
        byte[] arr = new byte[]{123, 1, 2, 3, 4, 5, 6, 7, 8, 98, 9, 34};
        ByteArrayTag tag = new ByteArrayTag(arr);
        ByteArrNBT nbt = new ByteArrNBT(arr);

        byte[] nms = toByteArray(tag::write);
        byte[] blib = toByteArray(out -> {
            MojangNbtReader.MojangNBTSerializer.TAG_BYTE_ARRAY.write(out, nbt);
        });

        Assert.assertArrayEquals(nms, blib);
    }

    @Test
    public void testByte() throws Throwable {
        ByteTag tag = ByteTag.valueOf((byte) 13);
        ByteNBT nbt = ByteNBT.valueOf((byte) 13);

        byte[] nms = toByteArray(tag::write);
        byte[] blib = toByteArray(out -> {
            MojangNbtReader.MojangNBTSerializer.TAG_BYTE.write(out, nbt);
        });

        Assert.assertArrayEquals(nms, blib);
    }

    @Test
    public void testCompoundTag() throws Throwable {
        var tag = new net.minecraft.nbt.CompoundTag();
        tag.putString("s", "string");
        tag.putInt("int", 13);

        CompoundTag nbt = new CompoundTag();
        nbt.putString("s", "string");
        nbt.putInt("int", 13);

        byte[] nms = toByteArray(tag::write);
        byte[] blib = toByteArray(out -> {
            MojangNbtReader.MojangNBTSerializer.TAG_COMPOUND.write(out, nbt);
        });

        Assert.assertArrayEquals(nms, blib);
    }

    @Test
    public void testListTag() throws Throwable {
        ListTag tag = new ListTag();
        tag.add(ByteTag.valueOf((byte) 123));
        tag.add(ByteTag.valueOf((byte) 32));
        tag.add(ByteTag.valueOf((byte) 33));

        ListNBT nbt = new ListNBT();
        nbt.add(ByteNBT.valueOf((byte) 123));
        nbt.add(ByteNBT.valueOf((byte) 32));
        nbt.add(ByteNBT.valueOf((byte) 33));

        byte[] nms = toByteArray(tag::write);
        byte[] blib = toByteArray(out -> {
            MojangNbtReader.MojangNBTSerializer.TAG_LIST.write(out, nbt);
        });

        Assert.assertArrayEquals(nms, blib);
    }

    private byte[] toByteArray(ThrowableConsumer<DataOutput> consumer) throws Throwable {
        try (var buffer = new ByteArrayOutputStream();
             DataOutputStream dataOutput = new DataOutputStream(buffer)) {
            consumer.accept(dataOutput);
            return buffer.toByteArray();
        }
    }

    @FunctionalInterface
    public interface ThrowableConsumer<T> {
        void accept(T t) throws Throwable;
    }
}