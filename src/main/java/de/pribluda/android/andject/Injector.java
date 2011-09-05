package de.pribluda.android.andject;

import android.app.Activity;


import java.lang.reflect.Field;

/**
 * Performs  dependency injection
 *
 * @author Konstantin Pribluda - konstantin@pribluda.de
 */
public class Injector {

    /**
     * injects dependencies into activity,  this scope does not need specific stop method
     *
     * @throws WiringException in case something went wrong
     */
    public static void startActivity(Activity target) throws WiringException {

        // wal through fields
        for (Field field : target.getClass().getFields()) {
            final View annotation = field.getAnnotation(View.class);
            if (annotation != null) {
                final android.view.View view = target.findViewById(annotation.id());
                if (view != null && field.getClass().isAssignableFrom(view.getClass())) {
                    try {
                        field.set(target, view);
                    } catch (IllegalAccessException e) {
                        throw new WiringException(e);
                    }
                }
            }
        }
    }

    /**
     * inject and start foreground scope. shall be called from onResume()
     *
     * @param target
     * @throws WiringException
     */
    public static void startForeground(Activity target) throws WiringException {

    }

    /**
     * stop foreground scope. shall be called from onPause()
     *
     * @param target
     * @throws WiringException
     */
    public static void stopForeground(Activity target) throws WiringException {

    }
}
