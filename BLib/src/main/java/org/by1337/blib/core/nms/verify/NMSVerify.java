package org.by1337.blib.core.nms.verify;

import org.by1337.blib.util.collection.IdentityHashSet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.InputStream;
import java.util.*;

public class NMSVerify {
    private static final ClassLoader LOADER = NMSVerify.class.getClassLoader();
    private final Map<String, ClassNode> nodes = new HashMap<>();
    private final Set<String> visited = new HashSet<>();

    private void verify(ClassNode current) throws NMSVerifyException {
        if (visited.contains(current.name)) return;
        visited.add(current.name);
        for (MethodNode method : current.methods) {
            verifyMethod(current, method);
        }
        if (current.superName != null && !current.superName.equals("java/lang/Object")) {
            verify(find(current.superName));
        }
        for (String anInterface : current.interfaces) {
            if (anInterface.equals("java/lang/Object")) continue;
            verify(find(anInterface));
        }
    }

    private final Set<MethodNode> verifiedMethods = new IdentityHashSet<>();

    private void verifyMethod(ClassNode current, MethodNode currentMethod) throws NMSVerifyException {
        if (verifiedMethods.contains(currentMethod)) return;
        verifiedMethods.add(currentMethod);
        //System.out.println("Verifying method " + current.name + "#" + currentMethod.name);
        Queue<String> queue = new LinkedList<>();
        int pos = 0;
        try {
            for (AbstractInsnNode instruction : currentMethod.instructions) {
                pos++;
                if (instruction instanceof TypeInsnNode type) {
                    queue.offer(pos + ". " + OpcodeToName.toName(instruction.getOpcode()) + type.desc);
                    find(type.desc);
                } else if (instruction instanceof MethodInsnNode invoke) {
                    queue.offer(pos + ". " + OpcodeToName.toName(instruction.getOpcode()) + " " + invoke.owner + "#" + invoke.name);
                    Class<?> cl = find(invoke.owner);
                    if (!isDoSkip(cl)) {
                        ClassNode classNode = read(cl);
                        if (!isValidInvoke(classNode, invoke)) {
                            throw new NMSVerifyException("Attempted to invoke method " + invoke.owner + "#" + invoke.name + invoke.desc + ", but the method does not exist!");
                        }
                        if (classNode.name.startsWith("org/by1337/")) { //todo
                            MethodNode methodNode = findMethod(classNode, invoke);
                            if (methodNode == null)
                                throw new NMSVerifyException("Method not found: " + invoke.owner + "#" + invoke.name + invoke.desc);
                            if (methodNode != currentMethod) {
                                verifyMethod(classNode, methodNode);
                            }
                        }
                    }
                } else if (instruction instanceof FieldInsnNode field) {
                    queue.offer(pos + ". " + OpcodeToName.toName(instruction.getOpcode()) + " " + field.owner + "#" + field.name);
                    ClassNode classNode = safeRead(field.owner);
                    if (classNode != null && !isValidField(classNode, field)) {
                        throw new NMSVerifyException("Access to variable " + field.owner + "#" + field.name + " " + field.desc + " but this variable does not exist!");
                    }
                } else if (instruction instanceof LineNumberNode line) {
                    queue.offer(pos + ". line " + line.line);
                } else {
                    queue.offer(pos + ". " + OpcodeToName.toName(instruction.getOpcode()));
                }
                if (queue.size() >= 10) {
                    queue.poll();
                }

            }
        } catch (NMSVerifyException e) {
            StringBuilder sb = new StringBuilder("In: " + current.name + "#" + currentMethod.name + currentMethod.desc + " at " + pos + "\n");
            String str;
            while ((str = queue.poll()) != null) {
                sb.append("\t").append(str).append("\n");
            }
            throw new NMSVerifyException(sb.toString(), e).trim();
        }
    }

    private MethodNode findMethod(ClassNode node, MethodInsnNode invoke) throws NMSVerifyException {
        ClassNode currunt = node;
        MethodNode methodNode = null;
        while (methodNode == null) {
            methodNode = getMethod(currunt, invoke);
            if (methodNode != null) break;
            for (String anInterface : currunt.interfaces) {
                methodNode = findMethod(read(find(anInterface)), invoke);
                if (methodNode != null) break;
            }
            if (currunt.superName == null) break;
            currunt = read(find(currunt.superName));
        }
        return methodNode;
    }

    private MethodNode getMethod(ClassNode node, MethodInsnNode invoke) throws NMSVerifyException {
        MethodNode methodNode = findMethod(node, true, invoke);
        if (methodNode == null) {
            return findMethod(node, false, invoke);
        }
        return methodNode;
    }

    private MethodNode findMethod(ClassNode node, boolean descCheck, MethodInsnNode invoke) throws NMSVerifyException {
        for (MethodNode method : node.methods) {
            if (method.name.equals(invoke.name)) {
                if (descCheck && method.desc.equals(invoke.desc)) return method;
                if (descCheck) continue;
                Type type = Type.getType(invoke.desc);
                Type type2 = Type.getType(method.desc);
                if (type.getArgumentTypes().length == type2.getArgumentTypes().length) {
                    return method;
                }
            }
        }
        if (node.superName != null) {
            return findMethod(read(find(node.superName)), descCheck, invoke);
        }
        return null;
    }

    private boolean isDoSkip(Class<?> cl) {
        return cl.isPrimitive() || cl.isArray();
    }

    public void verify(Class<?> cl) throws NMSVerifyException {
        if (isDoSkip(cl)) return;
        verify(read(cl));
    }

    private Class<?> find(String name) throws NMSVerifyException {
        try {
            return Class.forName(name.replace("/", "."), false, LOADER);
        } catch (ClassNotFoundException e) {
            throw new NMSVerifyException(e);
        }
    }

    private ClassNode read(Class<?> cl) throws NMSVerifyException {
        String className = cl.getTypeName().replace(".", "/") + ".class";
        ClassNode node = nodes.get(className);
        if (node != null) return node;
        try {
            ClassLoader loader = cl.getClassLoader();
            if (loader == null && (loader = Thread.currentThread().getContextClassLoader()) == null) {
                throw new NMSVerifyException("Cannot find class " + className);
            }

            try (InputStream classStream = loader.getResourceAsStream(className)) {
                if (classStream == null) {
                    throw new NMSVerifyException("Cannot find class " + className);
                }
                ClassReader classReader = new ClassReader(classStream);
                node = new ClassNode();
                classReader.accept(node, ClassReader.SKIP_FRAMES);
                nodes.put(className, node);
                return node;
            }
        } catch (NMSVerifyException e) {
            throw e;
        } catch (Throwable e) {
            throw new NMSVerifyException(e);
        }
    }

    private boolean isValidInvoke(ClassNode node, MethodInsnNode invoke) throws NMSVerifyException {
        for (MethodNode method : node.methods) {
            if (method.name.equals(invoke.name)) {
                Type type = Type.getType(invoke.desc);
                Type type2 = Type.getType(method.desc);
                if (type.getArgumentTypes().length == type2.getArgumentTypes().length) {
                    return true;
                }
            }
        }
        if (node.interfaces != null && !node.interfaces.isEmpty()) {
            for (String anInterface : node.interfaces) {
                ClassNode anInterfaceNode = read(find(anInterface));
                if (isValidInvoke(anInterfaceNode, invoke)) return true;
            }
        }
        if (node.superName != null) {
            ClassNode superNode = read(find(node.superName));
            return isValidInvoke(superNode, invoke);
        }
        return false;
    }

    private boolean isValidField(ClassNode node, FieldInsnNode field) throws NMSVerifyException {
        for (FieldNode fieldNode : node.fields) {
            if (field.name.equals(fieldNode.name)) return true;
        }
        if (node.interfaces != null && !node.interfaces.isEmpty()) {
            for (String anInterface : node.interfaces) {
                ClassNode anInterfaceNode = read(find(anInterface));
                if (isValidField(anInterfaceNode, field)) return true;
            }
        }
        if (node.superName != null) {
            ClassNode superNode = read(find(node.superName));
            return isValidField(superNode, field);
        }
        return false;
    }

    private ClassNode safeRead(String clazz) throws NMSVerifyException {
        Class<?> cl = find(clazz);
        if (isDoSkip(cl)) return null;
        return read(cl);
    }

    @FunctionalInterface
    private interface Consumer<T> {
        void accept(T t) throws NMSVerifyException;
    }

}
