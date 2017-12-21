package com.sma.yamba;

import android.app.Application;

/**
 * Created by octavian.salcianu on 12/21/2017.
 */

public class YambaApplicationHolder {
    private static YambaApplication application = null;

    public static Application getApplication() {
        return (application != null ? YambaApplicationHolder.application.getApplication() : null);
    }

    public static YambaApplication getInstance() {
        return (application != null ? (YambaApplication)YambaApplicationHolder.application.getApplication() : null);
    }

    public static void setApplication(YambaApplication application) {
        YambaApplicationHolder.application = application;
    }

}
