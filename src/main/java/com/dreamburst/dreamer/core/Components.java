package com.dreamburst.dreamer.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Supplies an array of {@link Component} that every {@link Entity} in an Engine should have in order for the
 * {@link EntitySystem} with this annotation to operate. Any {@link Entity} in an Engine that does not
 * contain every {@link Component} listed is subsequently ignored.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Components {

    Class<? extends Component>[] value() default {};
}
