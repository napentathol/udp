package us.sodiumlabs.udp.immutables;

import org.immutables.value.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
@Value.Style(
    get = {"is*", "get*"},
    init = "with*",
    visibility = Value.Style.ImplementationVisibility.PUBLIC
)
public @interface Style {}
