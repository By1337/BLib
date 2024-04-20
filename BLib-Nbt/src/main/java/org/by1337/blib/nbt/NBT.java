package org.by1337.blib.nbt;


public abstract class NBT {
    public abstract String toString();

    public abstract NbtType getType();

    public abstract String toStringBeautifier(int lvl);

    public String toStringBeautifier() {
        return toStringBeautifier(0);
    }

    public abstract Object getAsObject();

    public void write(NbtByteBuffer buffer) {
        getType().write(buffer, this);
    }

    public String quoteAndEscape(String raw) {
        StringBuilder var1 = new StringBuilder(" ");
        int var2 = 0;

        for (int var3 = 0; var3 < raw.length(); ++var3) {
            char var4 = raw.charAt(var3);
            if (var4 == '\\') {
                var1.append('\\');
            } else if (var4 == '"' || var4 == '\'') {
                if (var2 == 0) {
                    var2 = var4 == '"' ? 39 : 34;
                }

                if (var2 == var4) {
                    var1.append('\\');
                }
            }

            var1.append(var4);
        }

        if (var2 == 0) {
            var2 = 34;
        }

        var1.setCharAt(0, (char) var2);
        var1.append((char) var2);
        return var1.toString();
    }
}
