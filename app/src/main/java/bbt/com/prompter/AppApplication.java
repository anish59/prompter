package bbt.com.prompter;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by anish on 07-02-2018.
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initStetho();
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }
}
