package org.by1337.blib.nbt;

public enum NBTToStringStyle {
    /**
     * {array:[I;100,100],byte:13b,text:"text"}
     */
    COMPACT,
    /**
     * <blockquote><pre>
     * {
     *     array: [
     *         I;
     *         100,
     *         100
     *     ],
     *     byte: 13b,
     *     text: "text"
     * }
     * </pre></blockquote>
     */
    BEAUTIFIER,
    /**
     * {"array":[100,100],"byte":13,"text":"text"}
     */
    JSON_STYLE_COMPACT,
    /**
     * <blockquote><pre>
     * {
     *     "array": [
     *         100,
     *         100
     *     ],
     *     "byte": 13,
     *     "text": "text"
     * }
     * </pre></blockquote>
     */
    JSON_STYLE_BEAUTIFIER;

    public boolean isCompact() {
        return this == COMPACT || this == JSON_STYLE_COMPACT;
    }

    public boolean isJson() {
        return this == JSON_STYLE_COMPACT || this == JSON_STYLE_BEAUTIFIER;
    }
}
