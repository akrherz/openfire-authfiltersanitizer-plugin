package org.igniterealtime.openfire.plugin;

import org.jivesoftware.admin.AuthCheckFilter;
import org.jivesoftware.util.JiveGlobals;

import java.lang.reflect.Field;
import java.util.*;

public class SanitizeTask extends TimerTask
{
    @Override
    public void run()
    {
        // Known susceptible entry.
        AuthCheckFilter.removeExclude("setup/setup-*");

        // Intentionally use the older JiveGlobals API, and not the SystemProperties API, to allow for backwards compatibility.
        final boolean disableSearch = JiveGlobals.getBooleanProperty("plugin.authfiltersanitizer.sanitizetask.disableSearch", false);
        if (!disableSearch) {
            try {
                final String[] susceptibleEntries = searchForSusceptibleEntries();
                for (int i = 0; i < susceptibleEntries.length; i++) {
                    final String susceptibleEntry = susceptibleEntries[i];
                    AuthCheckFilter.removeExclude(susceptibleEntry);
                }
            } catch (Throwable t) {
                // Intentionally not using a Logger, to facilitate backwards compatibility with a pre-SLF4j era.
                t.printStackTrace();
            }
        }
    }

    protected String[] searchForSusceptibleEntries() throws NoSuchFieldException, IllegalAccessException
    {
        final Set results = new HashSet();
        final Field field = AuthCheckFilter.class.getDeclaredField("excludes");
        final boolean oldAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            final Object fieldValue = field.get(null);
            Object[] containedValues;
            if (fieldValue instanceof Collection) {
                containedValues = ((Collection) fieldValue).toArray();
                for (int i = 0; i < containedValues.length; i++) {
                    final Object containedValue = containedValues[i];
                    if (containedValue.toString().contains("*")) {
                        results.add(containedValue.toString());
                    }
                }
            }
        } finally {
            field.setAccessible(oldAccessible);
        }

        return (String[]) results.toArray(new String[results.size()]);
    }
}
