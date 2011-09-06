package de.pribluda.android.andject;

import java.lang.annotation.*;

/**
 * Annotation designating field or setter to inject view by id.
 * Target element shall be assignable from returned view type, otherwise
 * WiringException will be thrown/
 *
 * @author Konstantin Pribluda - konstantin@pribluda.de
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface InjectView {
    int id() default -1;    
}
