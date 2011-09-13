package de.pribluda.android.andject;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * performs preference injection in both directions - shall be called from onResume() / onPause()
 *
 * @author Konstantin Pribluda - pribluda@synyx.de
 */
public class PreferenceInjector {

    /**
     * load values from preference manager and inject them into target object
     * @param target
     * @param preferences
     */
    static void inject(Object target, SharedPreferences preferences)  throws WiringException {
    }

    /**
     * extract values from target object and save them in preference manager
     * @param target
     * @param preferences
     */
    static void eject(Object target, SharedPreferences preferences) throws WiringException {
    }
}
