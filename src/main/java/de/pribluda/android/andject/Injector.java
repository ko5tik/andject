package de.pribluda.android.andject;

import android.app.Activity;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Performs  dependency injection
 *
 * @author Konstantin Pribluda - konstantin@pribluda.de
 */
public class Injector {

    static Field[] fieldsType = new Field[0];

    /**
     * injects dependencies into activity,  this scope does not need specific stop method
     *
     * @throws WiringException in case something went wrong
     */
    public static void startActivity(Activity target) throws WiringException {

        // wal through fields
        for (Field field : extractFields(target)) {

            final InjectView annotation = field.getAnnotation(InjectView.class);
            if (annotation != null) {
                final android.view.View view = target.findViewById(annotation.id());
                if (view != null) {
                    if (!field.getType().isAssignableFrom(view.getClass())) {
                        throw new WiringException(field.toString() + " is not assignable from view id " + annotation.id() + " (" + view.getClass().getName() + ")");
                    }

                    try {
                        final boolean accessible = field.isAccessible();
                        field.setAccessible(true);
                        field.set(target, view);
                        field.setAccessible(accessible);
                    } catch (IllegalAccessException e) {
                        throw new WiringException(e);
                    }
                }

            }
        }
    }

    private static Field[] extractFields(Activity target) {
        ArrayList<Field> fields = new ArrayList<Field>();
        Collections.addAll(fields, target.getClass().getDeclaredFields());
        Class clazz = target.getClass().getSuperclass();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return (Field[]) fields.toArray(fieldsType);
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
