package de.pribluda.android.andject;

import java.lang.annotation.*;

/**
 * designates field or method  as target for preference value injection.
 * fields shall be assignable from one of types provided  by shared preferences
 *
 * @author Konstantin Pribluda
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})

public @interface InjectPreference {
    /**
     * preference key defautls to field name
     * @return
     */
    String key() default "";
  
}
