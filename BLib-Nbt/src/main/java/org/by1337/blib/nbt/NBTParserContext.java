package org.by1337.blib.nbt;

/**
 * Context for NBT parsing.
 */
public class NBTParserContext {
    private static final NBTParserContext EMPTY = new NBTParserContext();
    private boolean allowMultipleTypeInList;
    private boolean doNotConvertListToArray;

    /**
     * Checks if multiple types are allowed in a single NBT list.
     * @return true if multiple types are allowed, otherwise false.
     */
    public boolean isAllowMultipleTypeInList() {
        return allowMultipleTypeInList;
    }

    /**
     * Sets whether multiple types are allowed in a single NBT list.
     * @param allowMultipleTypeInList true to allow multiple types, false otherwise.
     * @return the updated NBTParserContext object.
     */
    public NBTParserContext setAllowMultipleTypeInList(boolean allowMultipleTypeInList) {
        this.allowMultipleTypeInList = allowMultipleTypeInList;
        return this;
    }

    /**
     * Checks if lists are not converted to arrays during parsing.
     * @return true if lists are not converted to arrays, otherwise false.
     */
    public boolean isDoNotConvertListToArray() {
        return doNotConvertListToArray;
    }

    /**
     * Sets whether lists are not converted to arrays during parsing.
     * @param doNotConvertListToArray true to prevent conversion to arrays, false otherwise.
     *                               If true, the parser will not convert ListNBT to corresponding array types.
     *                               For example, if it's a list of bytes, by default, the parser converts ListNBT to ByteArrNBT.
     *                               This parameter disables this conversion.
     * @return the updated NBTParserContext object.
     */
    public NBTParserContext setDoNotConvertListToArray(boolean doNotConvertListToArray) {
        this.doNotConvertListToArray = doNotConvertListToArray;
        return this;
    }

    /**
     * Retrieves an empty NBTParserContext instance.
     * @return an empty NBTParserContext object.
     */
    public static NBTParserContext empty(){
        return EMPTY;
    }
}

