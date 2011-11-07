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
        private int primitiveInt;
        @InjectPreference(key = "2")
        private Integer objectInteger;
        @InjectPreference(key = "3")
        private long longTarget;
        @InjectPreference(key = "4")
        private boolean booleanTarget;
        @InjectPreference(key = "5")
        private double primitiveDouble;
        @InjectPreference(key = "6")
        private Double objectDouble;
        @InjectPreference(key = "7")
        private float primitiveFloat;
        @InjectPreference(key = "8")
        private Float objectFloat;
        @InjectPreference(key = "9")
        String objectString;
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
}
