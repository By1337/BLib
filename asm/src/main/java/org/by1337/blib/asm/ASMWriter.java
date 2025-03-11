package org.by1337.blib.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

public class ASMWriter {

    public static void apply(MethodNode methodNode) {
        String asm = findASM(methodNode.instructions);
        Stream<String> lines = Arrays.stream(asm.split("\n"));
        Convertor convertor = new Convertor();
        lines.forEach(convertor::apply);
        methodNode.instructions = convertor.instructions;
    }

    private static String findASM(InsnList list) {
        AbstractInsnNode node = list.getFirst();
        do {
            if (node instanceof LdcInsnNode ldc && ldc.cst instanceof String string) {
                return string;
            }
        } while ((node = node.getNext()) != null);
        throw new ASMApplyException("Не найдена строка содержащая asm код!");
    }


    private static class Convertor implements Opcodes {
        private final Map<String, LabelNode> labels = new HashMap<>();
        private final InsnList instructions = new InsnList();

        public Convertor() {
        }

        public void apply(String line) {
            if (line.isEmpty()) return;
            if (line.endsWith(":")) {
                String label = line.substring(0, line.length() - 1).trim();
                LabelNode labelNode = new LabelNode();
                labels.put(label, labelNode);
                instructions.add(labelNode);
                return;
            }
            String trimmed = line.trim();
            instructions.add(OpcodeReaders.read(trimmed, labels));
        }
    }

    private static class OpcodeReaders {
        private static Map<String, OpcodeReader> readerMap = new HashMap<>();

        public static AbstractInsnNode read(String line, Map<String, LabelNode> labels) {
            String[] args = line.split(" ");
            OpcodeReader reader = readerMap.get(args[0]);
            if (reader == null) {
                throw new ASMApplyException("Unknown opcode " + args[0] + " in line " + line);
            }
            return reader.apply(line, labels);
        }

        static {
            readerMap.put("NOP", (ignore, labels) -> new InsnNode(Opcodes.NOP));
            readerMap.put("ACONST_NULL", (ignore, labels) -> new InsnNode(Opcodes.ACONST_NULL));
            readerMap.put("ICONST_M1", (ignore, labels) -> new InsnNode(Opcodes.ICONST_M1));
            readerMap.put("ICONST_0", (ignore, labels) -> new InsnNode(Opcodes.ICONST_0));
            readerMap.put("ICONST_1", (ignore, labels) -> new InsnNode(Opcodes.ICONST_1));
            readerMap.put("ICONST_2", (ignore, labels) -> new InsnNode(Opcodes.ICONST_2));
            readerMap.put("ICONST_3", (ignore, labels) -> new InsnNode(Opcodes.ICONST_3));
            readerMap.put("ICONST_4", (ignore, labels) -> new InsnNode(Opcodes.ICONST_4));
            readerMap.put("ICONST_5", (ignore, labels) -> new InsnNode(Opcodes.ICONST_5));
            readerMap.put("LCONST_0", (ignore, labels) -> new InsnNode(Opcodes.LCONST_0));
            readerMap.put("LCONST_1", (ignore, labels) -> new InsnNode(Opcodes.LCONST_1));
            readerMap.put("FCONST_0", (ignore, labels) -> new InsnNode(Opcodes.FCONST_0));
            readerMap.put("FCONST_1", (ignore, labels) -> new InsnNode(Opcodes.FCONST_1));
            readerMap.put("FCONST_2", (ignore, labels) -> new InsnNode(Opcodes.FCONST_2));
            readerMap.put("DCONST_0", (ignore, labels) -> new InsnNode(Opcodes.DCONST_0));
            readerMap.put("DCONST_1", (ignore, labels) -> new InsnNode(Opcodes.DCONST_1));
            readerMap.put("I2L", (ignore, labels) -> new InsnNode(Opcodes.I2L));
            readerMap.put("I2F", (ignore, labels) -> new InsnNode(Opcodes.I2F));
            readerMap.put("I2D", (ignore, labels) -> new InsnNode(Opcodes.I2D));
            readerMap.put("L2I", (ignore, labels) -> new InsnNode(Opcodes.L2I));
            readerMap.put("L2F", (ignore, labels) -> new InsnNode(Opcodes.L2F));
            readerMap.put("L2D", (ignore, labels) -> new InsnNode(Opcodes.L2D));
            readerMap.put("F2I", (ignore, labels) -> new InsnNode(Opcodes.F2I));
            readerMap.put("F2L", (ignore, labels) -> new InsnNode(Opcodes.F2L));
            readerMap.put("F2D", (ignore, labels) -> new InsnNode(Opcodes.F2D));
            readerMap.put("D2I", (ignore, labels) -> new InsnNode(Opcodes.D2I));
            readerMap.put("D2L", (ignore, labels) -> new InsnNode(Opcodes.D2L));
            readerMap.put("D2F", (ignore, labels) -> new InsnNode(Opcodes.D2F));
            readerMap.put("I2B", (ignore, labels) -> new InsnNode(Opcodes.I2B));
            readerMap.put("I2C", (ignore, labels) -> new InsnNode(Opcodes.I2C));
            readerMap.put("I2S", (ignore, labels) -> new InsnNode(Opcodes.I2S));
            readerMap.put("LCMP", (ignore, labels) -> new InsnNode(Opcodes.LCMP));
            readerMap.put("FCMPL", (ignore, labels) -> new InsnNode(Opcodes.FCMPL));
            readerMap.put("FCMPG", (ignore, labels) -> new InsnNode(Opcodes.FCMPG));
            readerMap.put("DCMPL", (ignore, labels) -> new InsnNode(Opcodes.DCMPL));
            readerMap.put("DCMPG", (ignore, labels) -> new InsnNode(Opcodes.DCMPG));
            readerMap.put("IRETURN", (ignore, labels) -> new InsnNode(Opcodes.IRETURN));
            readerMap.put("LRETURN", (ignore, labels) -> new InsnNode(Opcodes.LRETURN));
            readerMap.put("FRETURN", (ignore, labels) -> new InsnNode(Opcodes.FRETURN));
            readerMap.put("DRETURN", (ignore, labels) -> new InsnNode(Opcodes.DRETURN));
            readerMap.put("ARETURN", (ignore, labels) -> new InsnNode(Opcodes.ARETURN));
            readerMap.put("RETURN", (ignore, labels) -> new InsnNode(Opcodes.RETURN));
            readerMap.put("ARRAYLENGTH", (ignore, labels) -> new InsnNode(Opcodes.ARRAYLENGTH));
            readerMap.put("ATHROW", (ignore, labels) -> new InsnNode(Opcodes.ATHROW));
            readerMap.put("MONITORENTER", (ignore, labels) -> new InsnNode(Opcodes.MONITORENTER));
            readerMap.put("MONITOREXIT", (ignore, labels) -> new InsnNode(Opcodes.MONITOREXIT));
            readerMap.put("IALOAD", (ignore, labels) -> new InsnNode(Opcodes.IALOAD));
            readerMap.put("LALOAD", (ignore, labels) -> new InsnNode(Opcodes.LALOAD));
            readerMap.put("FALOAD", (ignore, labels) -> new InsnNode(Opcodes.FALOAD));
            readerMap.put("DALOAD", (ignore, labels) -> new InsnNode(Opcodes.DALOAD));
            readerMap.put("AALOAD", (ignore, labels) -> new InsnNode(Opcodes.AALOAD));
            readerMap.put("BALOAD", (ignore, labels) -> new InsnNode(Opcodes.BALOAD));
            readerMap.put("CALOAD", (ignore, labels) -> new InsnNode(Opcodes.CALOAD));
            readerMap.put("SALOAD", (ignore, labels) -> new InsnNode(Opcodes.SALOAD));
            readerMap.put("IASTORE", (ignore, labels) -> new InsnNode(Opcodes.IASTORE));
            readerMap.put("LASTORE", (ignore, labels) -> new InsnNode(Opcodes.LASTORE));
            readerMap.put("FASTORE", (ignore, labels) -> new InsnNode(Opcodes.FASTORE));
            readerMap.put("DASTORE", (ignore, labels) -> new InsnNode(Opcodes.DASTORE));
            readerMap.put("AASTORE", (ignore, labels) -> new InsnNode(Opcodes.AASTORE));
            readerMap.put("BASTORE", (ignore, labels) -> new InsnNode(Opcodes.BASTORE));
            readerMap.put("CASTORE", (ignore, labels) -> new InsnNode(Opcodes.CASTORE));
            readerMap.put("SASTORE", (ignore, labels) -> new InsnNode(Opcodes.SASTORE));
            readerMap.put("POP", (ignore, labels) -> new InsnNode(Opcodes.POP));
            readerMap.put("POP2", (ignore, labels) -> new InsnNode(Opcodes.POP2));
            readerMap.put("DUP", (ignore, labels) -> new InsnNode(Opcodes.DUP));
            readerMap.put("DUP_X1", (ignore, labels) -> new InsnNode(Opcodes.DUP_X1));
            readerMap.put("DUP_X2", (ignore, labels) -> new InsnNode(Opcodes.DUP_X2));
            readerMap.put("DUP2", (ignore, labels) -> new InsnNode(Opcodes.DUP2));
            readerMap.put("DUP2_X1", (ignore, labels) -> new InsnNode(Opcodes.DUP2_X1));
            readerMap.put("DUP2_X2", (ignore, labels) -> new InsnNode(Opcodes.DUP2_X2));
            readerMap.put("SWAP", (ignore, labels) -> new InsnNode(Opcodes.SWAP));
            readerMap.put("IADD", (ignore, labels) -> new InsnNode(Opcodes.IADD));
            readerMap.put("LADD", (ignore, labels) -> new InsnNode(Opcodes.LADD));
            readerMap.put("FADD", (ignore, labels) -> new InsnNode(Opcodes.FADD));
            readerMap.put("DADD", (ignore, labels) -> new InsnNode(Opcodes.DADD));
            readerMap.put("ISUB", (ignore, labels) -> new InsnNode(Opcodes.ISUB));
            readerMap.put("LSUB", (ignore, labels) -> new InsnNode(Opcodes.LSUB));
            readerMap.put("FSUB", (ignore, labels) -> new InsnNode(Opcodes.FSUB));
            readerMap.put("DSUB", (ignore, labels) -> new InsnNode(Opcodes.DSUB));
            readerMap.put("IMUL", (ignore, labels) -> new InsnNode(Opcodes.IMUL));
            readerMap.put("LMUL", (ignore, labels) -> new InsnNode(Opcodes.LMUL));
            readerMap.put("FMUL", (ignore, labels) -> new InsnNode(Opcodes.FMUL));
            readerMap.put("DMUL", (ignore, labels) -> new InsnNode(Opcodes.DMUL));
            readerMap.put("IDIV", (ignore, labels) -> new InsnNode(Opcodes.IDIV));
            readerMap.put("LDIV", (ignore, labels) -> new InsnNode(Opcodes.LDIV));
            readerMap.put("FDIV", (ignore, labels) -> new InsnNode(Opcodes.FDIV));
            readerMap.put("DDIV", (ignore, labels) -> new InsnNode(Opcodes.DDIV));
            readerMap.put("IREM", (ignore, labels) -> new InsnNode(Opcodes.IREM));
            readerMap.put("LREM", (ignore, labels) -> new InsnNode(Opcodes.LREM));
            readerMap.put("FREM", (ignore, labels) -> new InsnNode(Opcodes.FREM));
            readerMap.put("DREM", (ignore, labels) -> new InsnNode(Opcodes.DREM));
            readerMap.put("INEG", (ignore, labels) -> new InsnNode(Opcodes.INEG));
            readerMap.put("LNEG", (ignore, labels) -> new InsnNode(Opcodes.LNEG));
            readerMap.put("FNEG", (ignore, labels) -> new InsnNode(Opcodes.FNEG));
            readerMap.put("DNEG", (ignore, labels) -> new InsnNode(Opcodes.DNEG));
            readerMap.put("ISHL", (ignore, labels) -> new InsnNode(Opcodes.ISHL));
            readerMap.put("LSHL", (ignore, labels) -> new InsnNode(Opcodes.LSHL));
            readerMap.put("ISHR", (ignore, labels) -> new InsnNode(Opcodes.ISHR));
            readerMap.put("LSHR", (ignore, labels) -> new InsnNode(Opcodes.LSHR));
            readerMap.put("IUSHR", (ignore, labels) -> new InsnNode(Opcodes.IUSHR));
            readerMap.put("LUSHR", (ignore, labels) -> new InsnNode(Opcodes.LUSHR));
            readerMap.put("IAND", (ignore, labels) -> new InsnNode(Opcodes.IAND));
            readerMap.put("LAND", (ignore, labels) -> new InsnNode(Opcodes.LAND));
            readerMap.put("IOR", (ignore, labels) -> new InsnNode(Opcodes.IOR));
            readerMap.put("LOR", (ignore, labels) -> new InsnNode(Opcodes.LOR));
            readerMap.put("IXOR", (ignore, labels) -> new InsnNode(Opcodes.IXOR));
            readerMap.put("LXOR", (ignore, labels) -> new InsnNode(Opcodes.LXOR));
            readerMap.put("ILOAD", new VarInsnReader(Opcodes.ILOAD));
            readerMap.put("LLOAD", new VarInsnReader(Opcodes.LLOAD));
            readerMap.put("FLOAD", new VarInsnReader(Opcodes.FLOAD));
            readerMap.put("DLOAD", new VarInsnReader(Opcodes.DLOAD));
            readerMap.put("ALOAD", new VarInsnReader(Opcodes.ALOAD));
            readerMap.put("ISTORE", new VarInsnReader(Opcodes.ISTORE));
            readerMap.put("LSTORE", new VarInsnReader(Opcodes.LSTORE));
            readerMap.put("FSTORE", new VarInsnReader(Opcodes.FSTORE));
            readerMap.put("DSTORE", new VarInsnReader(Opcodes.DSTORE));
            readerMap.put("ASTORE", new VarInsnReader(Opcodes.ASTORE));
            readerMap.put("RET", new VarInsnReader(Opcodes.RET));
            readerMap.put("BIPUSH", new IntInsnReader(Opcodes.BIPUSH));
            readerMap.put("SIPUSH", new IntInsnReader(Opcodes.SIPUSH));
            readerMap.put("NEWARRAY", new IntInsnReader(Opcodes.NEWARRAY));
            readerMap.put("LDC", (s, labels) -> {
                String data = s.substring(4);
                if (data.contains(" ")) return new LdcInsnNode(data);
                try {
                    Integer x = Integer.parseInt(data);
                    return new LdcInsnNode(x);
                } catch (NumberFormatException ignored) {
                }
                try {
                    Long l = Long.parseLong(data);
                    return new LdcInsnNode(l);
                } catch (NumberFormatException ignored) {
                }
                try {
                    return new LdcInsnNode(Type.getType(data));
                } catch (IllegalArgumentException ignored) {
                    return new LdcInsnNode(data);
                }
            });
            readerMap.put("IINC", (s, labels) -> {
                String[] args = s.split(" ");
                try {
                    int var = Integer.parseInt(args[1]);
                    int incr = Integer.parseInt(args[2]);
                    return new IincInsnNode(var, incr);
                } catch (NumberFormatException e) {
                    throw new ASMApplyException("Ожидалось два числа " + s, e);
                }
            });
            readerMap.put("IFEQ", new JumpInsnReader(Opcodes.IFEQ));
            readerMap.put("IFNE", new JumpInsnReader(Opcodes.IFNE));
            readerMap.put("IFLT", new JumpInsnReader(Opcodes.IFLT));
            readerMap.put("IFGE", new JumpInsnReader(Opcodes.IFGE));
            readerMap.put("IFGT", new JumpInsnReader(Opcodes.IFGT));
            readerMap.put("IFLE", new JumpInsnReader(Opcodes.IFLE));
            readerMap.put("IF_ICMPEQ", new JumpInsnReader(Opcodes.IF_ICMPEQ));
            readerMap.put("IF_ICMPNE", new JumpInsnReader(Opcodes.IF_ICMPNE));
            readerMap.put("IF_ICMPLT", new JumpInsnReader(Opcodes.IF_ICMPLT));
            readerMap.put("IF_ICMPGE", new JumpInsnReader(Opcodes.IF_ICMPGE));
            readerMap.put("IF_ICMPGT", new JumpInsnReader(Opcodes.IF_ICMPGT));
            readerMap.put("IF_ICMPLE", new JumpInsnReader(Opcodes.IF_ICMPLE));
            readerMap.put("IF_ACMPEQ", new JumpInsnReader(Opcodes.IF_ACMPEQ));
            readerMap.put("IF_ACMPNE", new JumpInsnReader(Opcodes.IF_ACMPNE));
            readerMap.put("GOTO", new JumpInsnReader(Opcodes.GOTO));
            readerMap.put("JSR", new JumpInsnReader(Opcodes.JSR));
            readerMap.put("IFNULL", new JumpInsnReader(Opcodes.IFNULL));
            readerMap.put("IFNONNULL", new JumpInsnReader(Opcodes.IFNONNULL));
            readerMap.put("NEW", new TypeInsnReader(Opcodes.NEW));
            readerMap.put("ANEWARRAY", new TypeInsnReader(Opcodes.ANEWARRAY));
            readerMap.put("CHECKCAST", new TypeInsnReader(Opcodes.CHECKCAST));
            readerMap.put("INSTANCEOF", new TypeInsnReader(Opcodes.INSTANCEOF));
            readerMap.put("TABLESWITCH", (s, labels) -> { // todo
                throw new ASMApplyException("В данный момент не поддерживается использование " + s);
            });
            readerMap.put("LOOKUPSWITCH", (s, labels) -> { // todo
                throw new ASMApplyException("В данный момент не поддерживается использование " + s);
            });
            readerMap.put("INVOKEDYNAMIC", (s, labels) -> { // todo
                throw new ASMApplyException("В данный момент не поддерживается использование " + s);
            });
            readerMap.put("MULTIANEWARRAY", (s, labels) -> { // todo
                throw new ASMApplyException("В данный момент не поддерживается использование " + s);
            });
            readerMap.put("GETSTATIC", new FieldInsnReader(Opcodes.GETSTATIC));
            readerMap.put("PUTSTATIC", new FieldInsnReader(Opcodes.PUTSTATIC));
            readerMap.put("GETFIELD", new FieldInsnReader(Opcodes.GETFIELD));
            readerMap.put("PUTFIELD", new FieldInsnReader(Opcodes.PUTFIELD));

            readerMap.put("INVOKEVIRTUAL", new MethodInsnReader(Opcodes.INVOKEVIRTUAL));
            readerMap.put("INVOKESPECIAL", new MethodInsnReader(Opcodes.INVOKESPECIAL));
            readerMap.put("INVOKESTATIC", new MethodInsnReader(Opcodes.INVOKESTATIC));
            readerMap.put("INVOKEINTERFACE", new MethodInsnReader(Opcodes.INVOKEINTERFACE));

            for (String s : readerMap.keySet().toArray(new String[0])) {
                readerMap.put(s.toLowerCase(Locale.ENGLISH), readerMap.get(s));
            }
        }
    }

    @FunctionalInterface
    private interface OpcodeReader {
        AbstractInsnNode apply(String s, Map<String, LabelNode> labels);
    }

    private static abstract class IntParam implements OpcodeReader {
        protected int readInt(String s) {
            String[] args = s.split(" ");
            int x;
            try {
                x = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                throw new ASMApplyException("Ожидалось число на строчке " + s, e);
            }
            return x;
        }
    }

    private static class VarInsnReader extends IntParam {
        private final int opcode;

        private VarInsnReader(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public AbstractInsnNode apply(String s, Map<String, LabelNode> ignore) {
            return new VarInsnNode(opcode, readInt(s));
        }
    }

    private static class IntInsnReader extends IntParam {
        private final int opcode;

        private IntInsnReader(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public AbstractInsnNode apply(String s, Map<String, LabelNode> ignore) {
            return new IntInsnNode(opcode, readInt(s));
        }
    }

    private static class JumpInsnReader implements OpcodeReader {
        private final int opcode;

        private JumpInsnReader(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public AbstractInsnNode apply(String s, Map<String, LabelNode> labels) {
            String[] args = s.split(" ");
            String label = args[1];
            LabelNode labelNode = labels.get(label);
            if (labelNode == null) {
                throw new ASMApplyException("Прыжок на неизвестный label! " + s);
            }
            return new JumpInsnNode(opcode, labelNode);
        }
    }

    private static class TypeInsnReader implements OpcodeReader {
        private final int opcode;

        private TypeInsnReader(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public AbstractInsnNode apply(String s, Map<String, LabelNode> labels) {
            String[] args = s.split(" ");
            return new TypeInsnNode(opcode, args[1]);
        }
    }

    private static class FieldInsnReader implements OpcodeReader {
        private final int opcode;

        private FieldInsnReader(int opcode) {
            this.opcode = opcode;
        }

        // type name desc
        @Override
        public AbstractInsnNode apply(String s, Map<String, LabelNode> labels) {
            String[] args = s.split(" ");
            String type = args[1];
            String name = args[2];
            String desc = args[3];
            return new FieldInsnNode(opcode, type, name, desc);
        }
    }

    private static class MethodInsnReader implements OpcodeReader {
        private final int opcode;

        private MethodInsnReader(int opcode) {
            this.opcode = opcode;
        }

        // type name desc
        @Override
        public AbstractInsnNode apply(String s, Map<String, LabelNode> labels) {
            String[] args = s.split(" ");
            String type = args[1];
            String name = args[2];
            String desc = args[3];
            boolean isInterface = Boolean.parseBoolean(args.length > 4 ? args[4] : "false") || opcode == Opcodes.INVOKEINTERFACE;
            return new MethodInsnNode(opcode, type, name, desc, isInterface);
        }
    }


}
