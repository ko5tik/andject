package de.pribluda.android.andject;

import android.content.SharedPreferences;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Konstantin Pribluda - pribluda@synyx.de
 */
public class PreferenceInjectionTest {


    /**
     * all primitive types provided by shared preferences shall be injectable
     */
    @Test
    public void testPrimitiveTypesInjection(@Mocked final SharedPreferences preferences) throws IllegalAccessException {
        final Map<String, Object> allMap = new HashMap<String, Object>();

        allMap.put("1", 1);
        allMap.put("2", 2);
        allMap.put("3", 3l);
        allMap.put("4", true);
        allMap.put("5", 5.0d);
        allMap.put("6", 6.0d);
        allMap.put("7", 7.0f);
        allMap.put("8", 8.0f);
        allMap.put("9", "number nine");

        new Expectations() {
            {
                preferences.getAll();
                returns(allMap);

            }
        };
        final PrimitiveInjectable primitiveInjectable = new PrimitiveInjectable();

        PreferenceInjector.inject(primitiveInjectable, preferences);

        assertEquals(1, Deencapsulation.getField(primitiveInjectable, "primitiveInt"));
        assertEquals(2, Deencapsulation.getField(primitiveInjectable, "objectInteger"));
        assertEquals(3l, Deencapsulation.getField(primitiveInjectable, "longTarget"));
        assertEquals(true, Deencapsulation.getField(primitiveInjectable, "booleanTarget"));
        assertEquals(5.0, Deencapsulation.getField(primitiveInjectable, "primitiveDouble"));
        assertEquals(6.0, Deencapsulation.getField(primitiveInjectable, "objectDouble"));
        assertEquals(7.0f, Deencapsulation.getField(primitiveInjectable, "primitiveFloat"));
        assertEquals(8.0f, Deencapsulation.getField(primitiveInjectable, "objectFloat"));
        assertEquals("number nine", Deencapsulation.getField(primitiveInjectable, "objectString"));


        // nothing else shall be called
        new FullVerifications() {
            {
            }
        };
    }


    /**
     * field injection target
     */
    class PrimitiveInjectable {

        @InjectPreference(key = "1")
        private int primitiveInt = 7;
        @InjectPreference(key = "2")
        private Integer objectInteger = new Integer(23);
        @InjectPreference(key = "3")
        private long longTarget = 77l;
        @InjectPreference(key = "4")
        private boolean booleanTarget = false;
        @InjectPreference(key = "5")
        private double primitiveDouble = 234.3d;
        @InjectPreference(key = "6")
        private Double objectDouble = new Double(423.23d);
        @InjectPreference(key = "7")
        private float primitiveFloat = 12323.22f;
        @InjectPreference(key = "8")
        private Float objectFloat = new Float(2343.4f);
        @InjectPreference(key = "9")
        String objectString = "glurge glam";
    }

    /**
     * in case no key is specified, property name shall be used
     */
    @Test
    public void testInjectionByPropertyName(@Mocked final SharedPreferences preferences) throws IllegalAccessException {

        final Map<String, Object> allMap = new HashMap<String, Object>();
        allMap.put("justAnIntWithName", 239);

        new Expectations() {
            {
                preferences.getAll();
                returns(allMap);
            }
        };


        final PrimitiveByPropertyInjectable primitiveByProeprtyInjectable = new PrimitiveByPropertyInjectable();

        PreferenceInjector.inject(primitiveByProeprtyInjectable, preferences);

        assertEquals(239, Deencapsulation.getField(primitiveByProeprtyInjectable, "justAnIntWithName"));


        // nothing else shall be called
        new FullVerifications() {
            {
            }
        };
    }

    class PrimitiveByPropertyInjectable {
        @InjectPreference()
        private int justAnIntWithName;
    }


    /**
     * shall eject and save properties to shared preferences
     */
    @Test
    public void testEjection(@Mocked final SharedPreferences preferences,
                             @Mocked final SharedPreferences.Editor editor) throws IllegalAccessException {

        //
        new Expectations() {
            {
                preferences.edit();
                returns(editor);

                editor.putInt("1", 7);
                editor.putInt("2", new Integer(23));
                editor.putLong("3", 77l);
                editor.putBoolean("4", false);
                editor.putFloat("5", 234.3f);
                editor.putFloat("6", 423.23f);
                editor.putFloat("7", 12323.22f);
                editor.putFloat("8", 2343.4f);
                editor.putString("9", "glurge glam");

                editor.commit();

            }
        };

        PreferenceInjector.eject(new PrimitiveInjectable(), preferences);


        // nothing else shall be called
        new FullVerifications() {
            {
            }
        };
    }
}
