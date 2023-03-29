package ru.veryprosto.homefinance;

import android.app.Application;

import ru.veryprosto.homefinance.db.HelperFactory;

public class HomeFinance extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
