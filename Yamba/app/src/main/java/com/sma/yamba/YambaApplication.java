package com.sma.yamba;

import android.app.Application;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by octavian.salcianu on 12/21/2017.
 */

public interface YambaApplication {
    Application getApplication();

    YambaClient getYambaClient();

    //void setYambaClient(YambaClient yambaClient);

    void updateYambaClient();
}
