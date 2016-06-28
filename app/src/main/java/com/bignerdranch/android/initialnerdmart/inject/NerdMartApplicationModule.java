package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NerdMartCommonModule.class, NerdMartServiceModule.class})
public class NerdMartApplicationModule {
    private Context mApplicationContext;

    public NerdMartApplicationModule(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Provides
    public Context providesContext() {
        return mApplicationContext;
    }
}
