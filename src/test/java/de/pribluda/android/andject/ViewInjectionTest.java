package de.pribluda.android.andject;

import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

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
                injectable.asView = textView;

                injectable.findViewById(555);
                returns(button);
                injectable.button = button;
            }
        };

        Injector.startActivity(injectable);


    }


    class WithInjectableViews extends Activity {
        // shall be injected
        @View(id = 239)
        public android.view.View asView;
        @View(id = 555)
        public Button button;

    }

    /**
     * shall throw wiring exception on attempt to inject
     * non-assignable views
     */
    @Test
    public void testNonAssignableViewsAreBombed() {
        fail("implement me");
    }


}
