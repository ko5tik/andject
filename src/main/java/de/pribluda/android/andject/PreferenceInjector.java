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
    static final HashMap<Class, Ejector> ejectors = new HashMap<Class, Ejector>();
    private static final String EMPTY = "";

    static {
        primitves.put(Integer.TYPE, Integer.class);
        primitves.put(Long.TYPE, Long.class);
        primitves.put(Float.TYPE, Float.class);
        primitves.put(Double.TYPE, Double.class);
        primitves.put(Boolean.TYPE, Boolean.class);
        primitves.put(Character.TYPE, Character.class);
        primitves.put(Short.TYPE, Short.class);

        ejectors.put(Integer.class, new Ejector() {
            @Override
            public void eject(final SharedPreferences.Editor editor, final String key, final Object value) {
                editor.putInt(key, (Integer) value);
            }
        });
        ejectors.put(Long.class, new Ejector() {
            @Override
            public void eject(final SharedPreferences.Editor editor, final String key, final Object value) {
                editor.putLong(key, (Long) value);
            }
        });
        ejectors.put(Float.class, new Ejector() {
            @Override
            public void eject(final SharedPreferences.Editor editor, final String key, final Object value) {
                editor.putFloat(key, (Float) value);
            }
        });
        ejectors.put(Double.class, new Ejector() {
            @Override
            public void eject(final SharedPreferences.Editor editor, final String key, final Object value) {
                editor.putFloat(key, ((Double) value).floatValue());
            }
        });

        ejectors.put(Boolean.class, new Ejector() {
            @Override
            public void eject(final SharedPreferences.Editor editor, final String key, final Object value) {
                editor.putBoolean(key, ((Boolean) value));
            }
        });

        ejectors.put(String.class, new Ejector() {
            @Override
            public void eject(final SharedPreferences.Editor editor, final String key, final Object value) {
                editor.putString(key, ((String) value));
            }
        });
    }

    interface Ejector {
        void eject(final SharedPreferences.Editor editor, final String key, final Object value);
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
    public static void eject(Object target, SharedPreferences preferences) throws WiringException, IllegalAccessException {

        final SharedPreferences.Editor editor = preferences.edit();

        for (Field field : extractFields(target)) {
            // does field contain annotation?
            final InjectPreference injectPreference = field.getAnnotation(InjectPreference.class);
            if (null != injectPreference) {
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    type = primitves.get(type);
                }

                // have a strategy?
                if (ejectors.containsKey(type)) {
                    String key = injectPreference.key();

                    if (EMPTY.equals(key)) {
                        key = field.getName();
                    }
                    field.setAccessible(true);
                    ejectors.get(type).eject(editor, key, field.get(target));
                    field.setAccessible(false);
                }
            }
        }
        editor.commit();
    }
}
