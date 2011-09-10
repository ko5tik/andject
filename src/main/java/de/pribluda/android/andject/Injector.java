package de.pribluda.android.andject;

import android.app.Activity;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Performs  dependency injection
 *
 * @author Konstantin Pribluda - konstantin@pribluda.de
 */
public class Injector {

    private static Field[] fieldsType = new Field[0];
    private static Method[] methodsType = new Method[0];

    /**
     * injects dependencies into activity,  this scope does not need specific stop method
     *
     * @throws WiringException in case something went wrong
     */
    public static void startActivity(Activity target) throws WiringException {

        // walk through fields
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
                } else {
                    throw new WiringException(field.toString() + " view for injection not found: " + annotation.id());
                }
            }
        }

        // walk through methods
        for (Method method : extractMethods(target)) {
            //System.err.println("processing method " + method);
            //System.err.println("annotations:" + method.getDeclaredAnnotations().length);
            final InjectView annotation = method.getAnnotation(InjectView.class);
            if (annotation != null) {
                //System.err.println("annotation "  + annotation);
                final android.view.View view = target.findViewById(annotation.id());

                if (view != null) {
                    if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(view.getClass())) {
                        // so, it can be injected
                        try {
                            final boolean accessible = method.isAccessible();
                            method.setAccessible(true);
                            method.invoke(target, view);
                            method.setAccessible(accessible);
                        } catch (Exception e) {
                            throw new WiringException(e);
                        }
                    } else {
                        throw new WiringException(method.toString() + " is not injectable with " + view.getClass());
                    }
                } else {
                    throw new WiringException(method.toString() + " view for injection not found " + annotation.id());
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
        return fields.toArray(fieldsType);
    }


    private static Method[] extractMethods(Activity target) {
        ArrayList<Method> methods = new ArrayList<Method>();
        Collections.addAll(methods, target.getClass().getDeclaredMethods());
        Class clazz = target.getClass().getSuperclass();
        while (clazz != null) {
            Collections.addAll(methods, clazz.getDeclaredMethods());
            clazz = clazz.getSuperclass();
        }
        return methods.toArray(methodsType);
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
