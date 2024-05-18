package org.by1337.blib.nbt;

public enum NBTToStringStyle {
    /**
     * {text:"text",array:[I;100I,100I],byte:13b}
     */
    COMPACT,
    /**
     * {
     *   text: "text",
     *   array: [
     *     I;
     *     100I,
     *     100I
     *   ],
     *   byte: 13b
     * }
     */
    BEAUTIFIER,
    /**
     * {"text":"text","array":[100,100],"byte":13}
     */
    JSON_STYLE_COMPACT,
    /**
     * {
     *   "text": "text",
     *   "array": [
     *     100,
     *     100
     *   ],
     *   "byte": 13
     * }
     */
    JSON_STYLE_BEAUTIFIER;

    public boolean isCompact(){
        return this == COMPACT || this == JSON_STYLE_COMPACT;
    }
    public boolean isJson(){
        return this == JSON_STYLE_COMPACT || this == JSON_STYLE_BEAUTIFIER;
    }
}
