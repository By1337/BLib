package org.by1337.blib.asm.mojo;


import com.google.common.base.Joiner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.by1337.blib.asm.ASMModifier;
import org.by1337.blib.asm.ASMWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

@Mojo(name = "apply", defaultPhase = LifecyclePhase.PACKAGE)
public class MojoApply extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        File input = this.project.getArtifact().getFile();
        File outFile = new File(input.getParentFile(), input.getName() + "-temp.jar");

        AtomicBoolean atomicBoolean = new AtomicBoolean();
        try (
                JarFile jarFile = new JarFile(input);
                JarOutputStream out = new JarOutputStream(new FileOutputStream(outFile))
        ) {
            StringBuilder nmsClasses = new StringBuilder();
            jarFile.stream().forEach(entry -> {
                try {
                    if (entry.getName().endsWith(".class")) {
                        var classReader = new ClassReader(
                                jarFile.getInputStream(entry));
                        final ClassNode classNode = new ClassNode();
                        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);


                        boolean edited = false;
                        atomicBoolean.set(true);
                        if (classNode.version != Opcodes.V16) {
                            ASMModifier.modify(classNode);
                            edited = true;
                        }
                        for (MethodNode method : classNode.methods) {
                            if (method.invisibleAnnotations != null && method.invisibleAnnotations.stream().anyMatch(a -> a.desc.contains("ASM"))) {
                                getLog().info("Modify method " + classNode.name + "#" + method.name);
                                ASMWriter.apply(method, classNode);
                                edited = true;
                                atomicBoolean.set(true);
                            }
                        }
                        AnnotationNode nms;
                        if (classNode.visibleAnnotations != null && (nms = classNode.visibleAnnotations.stream().filter(a -> a.desc.contains("NMSAccessor")).findFirst().orElse(null)) != null) {
                            System.out.println(classNode.name);
                            nmsClasses.append(classNode.name.replace("/", ".")).append(" ");
                            String clazz = ((Type)nms.values.get(1)).getClassName();
                            nmsClasses.append(((Type)nms.values.get(1)).getClassName());
                            nmsClasses.append("\n");
                            if (classNode.interfaces == null || !classNode.interfaces.contains(clazz.replace(".", "/"))){
                                throw new RuntimeException("Class " + classNode.name + " is not impl class " + clazz);
                            }
                        }

                        if (edited) {
                            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                            classNode.accept(writer);
                            out.putNextEntry(entry);
                            out.write(writer.toByteArray());
                        } else {
                            out.putNextEntry(entry);
                            try (var in = jarFile.getInputStream(entry)) {
                                out.write(in.readAllBytes());
                            }
                        }

                    } else {
                        if (entry.getName().equals("nms-path.txt")) {
                            try (var in = jarFile.getInputStream(entry)) {
                                nmsClasses.append(new String(in.readAllBytes(), StandardCharsets.UTF_8));
                            }
                        } else {
                            out.putNextEntry(entry);
                            try (var in = jarFile.getInputStream(entry)) {
                                out.write(in.readAllBytes());
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            JarEntry entry = new JarEntry("nms-path.txt");
            out.putNextEntry(entry);
            out.write(nmsClasses.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!atomicBoolean.get()) {
            outFile.delete();
        } else {
            try {
                Files.move(input.toPath(), new File(input.getParent(), input.getName() + "-original-asm.jar").toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.move(outFile.toPath(), input.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
