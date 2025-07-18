package org.by1337.blib.nms;

import org.by1337.blib.util.Version;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiStatus.Internal
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NMSAccessor {
    Class<?> forClazz();

    Version[] forVersions() default {};
    Version from() default Version.UNKNOWN;
    Version to() default Version.UNKNOWN;
}
