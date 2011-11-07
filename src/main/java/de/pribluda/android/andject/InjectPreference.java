package de.pribluda.android.andject;

import java.lang.annotation.*;

/**
 * designates field or method  as target for preference value injection.
 * fields shall be assignable from one of types provided  by shared preferences, or have
 * constructor taking single argument of such type
 *
 * @author Konstantin Pribluda
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})

public @interface InjectPreference {
    /**
     * preference key, required
     * @return
     */
    String key() default "";
  
}
