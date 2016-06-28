package com.bignerdranch.android.initialnerdmart;

import android.app.Application;
import android.content.Context;

import com.bignerdranch.android.initialnerdmart.inject.DaggerNerdMartComponent;
import com.bignerdranch.android.initialnerdmart.inject.NerdMartApplicationModule;
import com.bignerdranch.android.initialnerdmart.inject.NerdMartComponent;

import timber.log.Timber;

public class NerdMartApplication extends Application {
    private NerdMartComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        buildComponentAndInject();
    }

    private void buildComponentAndInject() {
        mComponent = DaggerComponentInitializer.init(this);
    }

    public static NerdMartComponent component(Context context) {
        return ((NerdMartApplication)
                context.getApplicationContext()).getComponent();
    }

    public NerdMartComponent getComponent() {
        return mComponent;
    }

    private final static class DaggerComponentInitializer {
        public static NerdMartComponent init(NerdMartApplication app) {
            return DaggerNerdMartComponent.builder()
                    .nerdMartApplicationModule(new NerdMartApplicationModule(app))
                    .build();
        }
    }
}
