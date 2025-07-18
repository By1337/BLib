package org.by1337.blib.core.nms.verify;

import org.objectweb.asm.Opcodes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

class OpcodeToName {
    private static final Map<Integer, String> opcodeToName = new HashMap<>();

    public static String toName(int opcode) {
        return opcodeToName.getOrDefault(opcode, "unknown#" + opcode);
    }
    static {
        boolean start = false;
        for (Field field : Opcodes.class.getFields()) {
            if (start || (start = field.getName().equals("NOP"))) {
                try {
                    opcodeToName.put((Integer) field.get(null), field.getName().toLowerCase());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
