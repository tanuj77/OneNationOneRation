package org.vsr.onenationoneration;

import android.app.Application;
import android.content.Context;

import org.vsr.onenationoneration.Helper.LocalHelper;

public class MainApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocalHelper.onAttach(base,"en"));
    }
}
