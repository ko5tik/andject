package de.pribluda.android.andject;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * test capabilities of view injection
 *
 * @author Konstantin Pribluda - konstantin@pribluda.de
 */
public class ViewInjectionTest {

    /**
     * shall inject assignable views   into
     */
    @Test
    public void testSimpleInjection(@Mocked final WithInjectableViews injectable,
                                    @Mocked final TextView textView,
                                    @Mocked final Button button) {

        new Expectations() {
            {
                injectable.findViewById(239);
                returns(textView);


                injectable.findViewById(555);
                returns(button);


            }
        };

        Injector.startActivity(injectable);

        assertEquals(textView, Deencapsulation.getField(injectable, "asView"));
        assertEquals(button, Deencapsulation.getField(injectable, "button"));
        assertNull(Deencapsulation.getField(injectable, "notInjected"));

    }


    class WithInjectableViews extends Activity {
        // shall be injected
        @InjectView(id = 239)
        private android.view.View asView;
        @InjectView(id = 555)
        private Button button;
        // shall be left alone
        private View notInjected = null;

    }

    /**
     * shall throw wiring exception on attempt to inject
     * non-assignable views
     */
    @Test
    public void testNonAssignableViewsAreBombed(@Mocked final WithInjectableViews injectable,
                                                @Mocked final TextView textView,
                                                @Mocked final Button button) {
        new Expectations() {
            {
                injectable.findViewById(239);
                returns(button);

                injectable.findViewById(555);
                returns(textView);
            }
        };
        try {
            Injector.startActivity(injectable);
            fail("was expecting exception to be thrown");
        } catch (WiringException ex) {
            // anticipated, assure proper text
            assertEquals("private android.widget.Button de.pribluda.android.andject.ViewInjectionTest$WithInjectableViews.button is not assignable from view id 555 (android.widget.TextView)", ex.getMessage());
        }

    }


    /**
     * shall inject view into setter
     */
    @Test
    public void testThatSetterInjectionWorksProperly(@Mocked final WithInjectableMethods injectable,
                                                     @Mocked final TextView textView,
                                                     @Mocked final Button button) {
          new Expectations() {
            {
                injectable.findViewById(239);
                returns(textView);


                injectable.findViewById(555);
                returns(button);


            }
        };

        Injector.startActivity(injectable);

        assertEquals(textView, Deencapsulation.getField(injectable, "asView"));
        assertEquals(button, Deencapsulation.getField(injectable, "button"));
        assertNull(Deencapsulation.getField(injectable, "notInjected"));
    }


    /**
     * shall produce wiring exception
     */
    @Test
    public void testThatSetterInjectionIsBombedProperlyOnNonAssignability(@Mocked final WithInjectableMethods injectable,
                                                                          @Mocked final TextView textView,
                                                                          @Mocked final Button button) {
          new Expectations() {
            {
                injectable.findViewById(239);
                returns(button);

                injectable.findViewById(555);
                returns(textView);
            }
        };
        try {
            Injector.startActivity(injectable);
            fail("was expecting exception to be thrown");
        } catch (WiringException ex) {
            // anticipated, assure proper text
            assertEquals("private android.widget.Button de.pribluda.android.andject.ViewInjectionTest$WithInjectableViews.button is not assignable from view id 555 (android.widget.TextView)", ex.getMessage());
        }
    }


    class WithInjectableMethods extends Activity {

        private android.view.View asView;

        private Button button;

        // shall be left alone
        private View notInjected = null;
        // shall be injected

        @InjectView(id = 239)
        public void setAsView(View asView) {
            this.asView = asView;
        }

        @InjectView(id = 555)
        public void setButton(Button button) {
            this.button = button;
        }

        public void setNotInjected(View notInjected) {
            this.notInjected = notInjected;
        }
    }
}
