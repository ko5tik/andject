package de.pribluda.android.andject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * base injection methods
 */
public class BaseInjector {
    private static Field[] fieldsType = new Field[0];
    private static Method[] methodsType = new Method[0];


    protected static Field[] extractFields(Object target) {
        ArrayList<Field> fields = new ArrayList<Field>();
        Collections.addAll(fields, target.getClass().getDeclaredFields());
        Class clazz = target.getClass().getSuperclass();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(fieldsType);
    }

    protected static Method[] extractMethods(Object target) {
        ArrayList<Method> methods = new ArrayList<Method>();
        Collections.addAll(methods, target.getClass().getDeclaredMethods());
        Class clazz = target.getClass().getSuperclass();
        while (clazz != null) {
            Collections.addAll(methods, clazz.getDeclaredMethods());
            clazz = clazz.getSuperclass();
        }
        return methods.toArray(methodsType);
    }
}
