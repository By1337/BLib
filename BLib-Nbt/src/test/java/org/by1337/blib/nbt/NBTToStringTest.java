package org.by1337.blib.nbt;

import junit.framework.TestCase;
import org.by1337.blib.nbt.impl.ByteArrNBT;
import org.by1337.blib.nbt.impl.CompoundTag;
import org.junit.Assert;

public class NBTToStringTest extends TestCase {

    public void test() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag(NBTParserTest.nbt, new NBTParserContext().setDoNotConvertListToArray(true));
        NbtUtil.assertEqualsTags(
                compoundTag,
                NBTParser.parseAsCompoundTag(NBTToString.appendNBT(compoundTag, NBTToStringStyle.COMPACT, new StringBuilder(), 0).toString(), new NBTParserContext().setDoNotConvertListToArray(true))
        );
        NbtUtil.assertEqualsTags(
                compoundTag,
                NBTParser.parseAsCompoundTag(NBTToString.appendNBT(compoundTag, NBTToStringStyle.BEAUTIFIER, new StringBuilder(), 0).toString(), new NBTParserContext().setDoNotConvertListToArray(true))
        );
        NbtUtil.assertEqualsTags(
                compoundTag,
                NBTParser.parseAsCompoundTag(NBTToString.appendNBT(compoundTag, NBTToStringStyle.JSON_STYLE_COMPACT, new StringBuilder(), 0).toString())
        );
        NbtUtil.assertEqualsTags(
                compoundTag,
                NBTParser.parseAsCompoundTag(NBTToString.appendNBT(compoundTag, NBTToStringStyle.JSON_STYLE_BEAUTIFIER, new StringBuilder(), 0).toString())
        );
    }
    public void testAppendArray() {
        ByteArrNBT arrNBT = new ByteArrNBT(new byte[]{100, 127, 32, 43});
        StringBuilder sb = new StringBuilder();
        NBTToString.appendNBT(arrNBT, NBTToStringStyle.COMPACT, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(arrNBT, NBTToStringStyle.BEAUTIFIER, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(arrNBT, NBTToStringStyle.JSON_STYLE_COMPACT, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(arrNBT, NBTToStringStyle.JSON_STYLE_BEAUTIFIER, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(new ByteArrNBT(new byte[]{}), NBTToStringStyle.COMPACT, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(new ByteArrNBT(new byte[]{}), NBTToStringStyle.BEAUTIFIER, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(new ByteArrNBT(new byte[]{}), NBTToStringStyle.JSON_STYLE_COMPACT, sb, 0);
        sb.append("\n");
        NBTToString.appendNBT(new ByteArrNBT(new byte[]{}), NBTToStringStyle.JSON_STYLE_BEAUTIFIER, sb, 0);
        sb.append("\n");
        Assert.assertEquals(sb.toString(), """
                [B;100B,127B,32B,43B]
                [
                    B;
                    100B,
                    127B,
                    32B,
                    43B
                ]
                [100,127,32,43]
                [
                    100,
                    127,
                    32,
                    43
                ]
                [B;]
                [
                    B;
                ]
                []
                [
                ]
                """);

    }
}