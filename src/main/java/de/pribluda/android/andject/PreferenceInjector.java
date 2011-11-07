package de.pribluda.android.andject;

import android.content.SharedPreferences;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * performs preference injection in both directions - shall be called from onResume() / onPause()
 *
 * @author Konstantin Pribluda - pribluda@synyx.de
 */
public class PreferenceInjector extends BaseInjector {
    static final HashMap<Class, Class> primitves = new HashMap<Class, Class>();
    private static final String EMPTY = "";

    static {
        primitves.put(Integer.TYPE, Integer.class);
        primitves.put(Long.TYPE, Long.class);
        primitves.put(Float.TYPE, Float.class);
        primitves.put(Double.TYPE, Double.class);
        primitves.put(Boolean.TYPE, Boolean.class);
        primitves.put(Character.TYPE, Character.class);
        primitves.put(Short.TYPE, Short.class);
    }

    /**
     * load values from preference manager and inject them into target object
     *
     * @param target
     * @param preferences
     */
    public static void inject(Object target, SharedPreferences preferences) throws WiringException, IllegalAccessException {
        final Map<String, ?> allPreferences = preferences.getAll();
        for (Field field : extractFields(target)) {

            // does field contain annotation?
            final InjectPreference injectPreference = field.getAnnotation(InjectPreference.class);
            if (injectPreference != null) {

                String key = injectPreference.key();
                
                if (EMPTY.equals(key)) {
                    key = field.getName();
                }

                if (allPreferences.containsKey(key)) {

                    final Object preference = allPreferences.get(key);

                    Class<?> fieldType = field.getType();
                    if (fieldType.isPrimitive())
                        fieldType = primitves.get(fieldType);

                    if (fieldType.isAssignableFrom(preference.getClass())) {
                        field.setAccessible(true);
                        field.set(target, preference);
                        field.setAccessible(false);
                    }
                }
            }
        }

    }

    /**
     * extract values from target object and save them in preference manager
     *
     * @param target
     * @param preferences
     */
    static void eject(Object target, SharedPreferences preferences) throws WiringException {
    }
}
