package org.by1337.blib.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMModifier {
    public static void modify(MethodNode methodNode, ClassNode classNode){

    }
    public static void modify(ClassNode classNode){
        classNode.version = Opcodes.V16;
    }
}
