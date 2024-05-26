package org.by1337.blib.nbt;


import com.google.common.base.Joiner;
import org.by1337.blib.nbt.*;
import org.by1337.blib.nbt.impl.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NBTParserTest {
    @Test
    public void test() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag(nbt);

        DefaultNbtByteBuffer buffer = new DefaultNbtByteBuffer();

        compoundTag.write(buffer);

        DefaultNbtByteBuffer buffer1 = new DefaultNbtByteBuffer(buffer.toByteArray());

        CompoundTag compoundTag1 = (CompoundTag) NbtType.COMPOUND.read(buffer1);

        NbtUtil.assertEqualsTags(compoundTag1, compoundTag);

    }

    @Test
    public void compressDecompressTest() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag(nbt);
        CompressedNBT compressed = compoundTag.getAsCompressedNBT();
        NbtUtil.assertEqualsTags(compoundTag, compressed.decompress());
    }

    @Test
    public void compressDecompressTest2() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag(nbt);
        CompressedNBT compressed = compoundTag.getAsCompressedNBT();
        compoundTag.putTag("compressed", compressed);
        NBT decompressed = compoundTag.getAndDecompress("compressed");
        compoundTag.remove("compressed");
        NbtUtil.assertEqualsTags(compoundTag, decompressed);
    }

    @Test
    public void compressDecompressTest3() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag(nbt);
        CompressedNBT compressed = compoundTag.getAsCompressedNBT();
        compoundTag.putTag("compressed", new ByteArrNBT(compressed.getValue()));
        NBT decompressed = compoundTag.getAndDecompress("compressed");
        compoundTag.remove("compressed");
        NbtUtil.assertEqualsTags(compoundTag, decompressed);
    }

    @Test
    public void testParseAsCompoundTag() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag(nbt);
        Assert.assertEquals(compoundTag.toStringBeautifier(), nbtBeautifiered);

        NbtUtil.assertEqualsTags(compoundTag, NBTParser.parseAsCompoundTag(nbtBeautifiered));
    }

    @Test
    public void parseDecimalNumber() {
        String number = String.valueOf(Math.PI);
        NBT nbt1 = NBTParser.parseNBT(number);
        Assert.assertTrue(nbt1 instanceof DoubleNBT);
        Assert.assertEquals(nbt1.getAsObject(), Math.PI);
    }
    @Test
    public void parseLong() {
        String number = Long.toString(0x7fffffffffffffffL);
        System.out.println(number);
        NBT nbt1 = NBTParser.parseNBT(number);
        Assert.assertTrue(nbt1 instanceof LongNBT);
        Assert.assertEquals(nbt1.getAsObject(), 0x7fffffffffffffffL);
    }


    @Test
    public void parseList() {
        LongArrNBT longArrNBT = (LongArrNBT) NBTParser.parseList("""
                [\t\t\t\tL\t\t\t\t;\t\t
                 123L,
                        123L,\n\t
                   321L
                \t\t]\n
                """);
        Assert.assertEquals(longArrNBT.toString(), "[L;123L,123L,321L]");

        ListNBT listNBT = new ListNBT();
        listNBT.add(new LongArrNBT(new long[]{293891289, 231342, 5421434, 491829713, 3874328, 3274819, 483223}));

        for (int i = 0; i < 50; i++) {
            ListNBT listNBT1 = new ListNBT();
            listNBT1.add(listNBT);
            listNBT = listNBT1;
        }

        ListNBT list = (ListNBT) NBTParser.parseList(listNBT.toString());

        NbtUtil.assertEqualsTags(list, listNBT);

    }

    @Test
    public void unicodeTest() {
        CompoundTag compoundTag = NBTParser.parseAsCompoundTag("{text:\"\\u043e\\u0432\\u0430\\u043a\\u043e\\u043d\\u0435\\u043c\\u0442\\u043d\\u044b\\u043e\\u0434\\u043e\\u0413\"}");
        Assert.assertEquals(compoundTag.toString(), "{text:\"оваконемтныодоГ\"}");
    }

    public static final String nbt = "{Double:45.0d,Double2:45.5d,arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],b_1:1b,b_2:0b,b_3:0b,b_4:1b,byte:127b,byte_arr:[B;0B,127B,89B],float:2.0f,float2:2.5f,floatList:[1.0f,1.1f,23.5f],int:43,int_arr:[I;99,345,211],list:[123,123],list_compound_tag:[{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]},{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]},{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]}],long:123L,long_arr:[L;882883L,34213L,4322L],\"s'tring2\":'asa\"a\\'s',short:34s,'str\"in\\'g2':'asa\"a\\'s','str\"ing2':'asa\"a\\'s',strList:[\"string\",\"string1\",\"string2\"],string:\"str\",string1:\"asa'as\",string2:'asa\"a\\'s',tags:{Double:45.0d,Double2:45.5d,Lore:['{\"extra\":[{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"text\":\"  \"},{\"italic\":false,\"color\":\"gold\",\"text\":\"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�\"}],\"text\":\"\"}','{\"extra\":[{\"italic\":false,\"color\":\"gray\",\"text\":\"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹\"}],\"text\":\"\"}'],arr_list:[[L;123L,123L,321L],[L;4123L,123L,4231L],[L;123L,123L,321L]],float:2.0f,float2:2.5f,list_in_list:[[[[[[[[[[[[L;123L,123L,321L]]]]]]]]]]]]}}";
    public static final String nbtBeautifiered = """
            {
                long_arr: [
                    L;
                    882883L,
                    34213L,
                    4322L
                ],
                string: "str",
                "s'tring2": 'asa"a\\'s',
                float: 2.0f,
                long: 123L,
                Double2: 45.5d,
                b_2: 0b,
                b_1: 1b,
                b_4: 1b,
                string1: "asa'as",
                b_3: 0b,
                'str"in\\'g2': 'asa"a\\'s',
                string2: 'asa"a\\'s',
                byte_arr: [
                    B;
                    0B,
                    127B,
                    89B
                ],
                float2: 2.5f,
                int_arr: [
                    I;
                    99,
                    345,
                    211
                ],
                byte: 127b,
                strList: [
                    "string",
                    "string1",
                    "string2"
                ],
                list: [
                    123,
                    123
                ],
                Double: 45.0d,
                int: 43,
                tags: {
                    Double2: 45.5d,
                    Lore: [
                        '{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"text":"  "},{"italic":false,"color":"gold","text":"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´"}],"text":""}',
                        '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹"}],"text":""}',
                        '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�"}],"text":""}',
                        '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹"}],"text":""}'
                    ],
                    arr_list: [
                        [
                            L;
                            123L,
                            123L,
                            321L
                        ],
                        [
                            L;
                            4123L,
                            123L,
                            4231L
                        ],
                        [
                            L;
                            123L,
                            123L,
                            321L
                        ]
                    ],
                    float2: 2.5f,
                    list_in_list: [
                        [
                            [
                                [
                                    [
                                        [
                                            [
                                                [
                                                    [
                                                        [
                                                            [
                                                                [
                                                                    L;
                                                                    123L,
                                                                    123L,
                                                                    321L
                                                                ]
                                                            ]
                                                        ]
                                                    ]
                                                ]
                                            ]
                                        ]
                                    ]
                                ]
                            ]
                        ]
                    ],
                    float: 2.0f,
                    Double: 45.0d
                },
                arr_list: [
                    [
                        L;
                        123L,
                        123L,
                        321L
                    ],
                    [
                        L;
                        4123L,
                        123L,
                        4231L
                    ],
                    [
                        L;
                        123L,
                        123L,
                        321L
                    ]
                ],
                'str"ing2': 'asa"a\\'s',
                floatList: [
                    1.0f,
                    1.1f,
                    23.5f
                ],
                list_compound_tag: [
                    {
                        Double2: 45.5d,
                        Lore: [
                            '{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"text":"  "},{"italic":false,"color":"gold","text":"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹"}],"text":""}'
                        ],
                        arr_list: [
                            [
                                L;
                                123L,
                                123L,
                                321L
                            ],
                            [
                                L;
                                4123L,
                                123L,
                                4231L
                            ],
                            [
                                L;
                                123L,
                                123L,
                                321L
                            ]
                        ],
                        float2: 2.5f,
                        list_in_list: [
                            [
                                [
                                    [
                                        [
                                            [
                                                [
                                                    [
                                                        [
                                                            [
                                                                [
                                                                    [
                                                                        L;
                                                                        123L,
                                                                        123L,
                                                                        321L
                                                                    ]
                                                                ]
                                                            ]
                                                        ]
                                                    ]
                                                ]
                                            ]
                                        ]
                                    ]
                                ]
                            ]
                        ],
                        float: 2.0f,
                        Double: 45.0d
                    },
                    {
                        Double2: 45.5d,
                        Lore: [
                            '{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"text":"  "},{"italic":false,"color":"gold","text":"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹"}],"text":""}'
                        ],
                        arr_list: [
                            [
                                L;
                                123L,
                                123L,
                                321L
                            ],
                            [
                                L;
                                4123L,
                                123L,
                                4231L
                            ],
                            [
                                L;
                                123L,
                                123L,
                                321L
                            ]
                        ],
                        float2: 2.5f,
                        list_in_list: [
                            [
                                [
                                    [
                                        [
                                            [
                                                [
                                                    [
                                                        [
                                                            [
                                                                [
                                                                    [
                                                                        L;
                                                                        123L,
                                                                        123L,
                                                                        321L
                                                                    ]
                                                                ]
                                                            ]
                                                        ]
                                                    ]
                                                ]
                                            ]
                                        ]
                                    ]
                                ]
                            ]
                        ],
                        float: 2.0f,
                        Double: 45.0d
                    },
                    {
                        Double2: 45.5d,
                        Lore: [
                            '{"extra":[{"bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false,"text":"  "},{"italic":false,"color":"gold","text":"âœ” Ð’Ñ�Ñ‘ Ð¿Ð¾Ð´Ñ€Ñ�Ð´"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð˜Ð½Ñ�Ñ‚Ñ€ÑƒÐ¼ÐµÐ½Ñ‚Ñ‹"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð�Ð»Ñ…Ð¸Ð¼Ð¸Ñ�"}],"text":""}',
                            '{"extra":[{"italic":false,"color":"gray","text":"â�º Ð”Ð¾Ð½Ð°Ñ‚Ð½Ñ‹Ðµ Ð¿Ñ€ÐµÐ´Ð¼ÐµÑ‚Ñ‹"}],"text":""}'
                        ],
                        arr_list: [
                            [
                                L;
                                123L,
                                123L,
                                321L
                            ],
                            [
                                L;
                                4123L,
                                123L,
                                4231L
                            ],
                            [
                                L;
                                123L,
                                123L,
                                321L
                            ]
                        ],
                        float2: 2.5f,
                        list_in_list: [
                            [
                                [
                                    [
                                        [
                                            [
                                                [
                                                    [
                                                        [
                                                            [
                                                                [
                                                                    [
                                                                        L;
                                                                        123L,
                                                                        123L,
                                                                        321L
                                                                    ]
                                                                ]
                                                            ]
                                                        ]
                                                    ]
                                                ]
                                            ]
                                        ]
                                    ]
                                ]
                            ]
                        ],
                        float: 2.0f,
                        Double: 45.0d
                    }
                ],
                short: 34s
            }""";
}