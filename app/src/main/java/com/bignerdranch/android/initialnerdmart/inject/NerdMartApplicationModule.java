package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;

import com.bignerdranch.android.nerdmartservice.service.NerdMartService;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;

import dagger.Module;
import dagger.Provides;

@Module
public class NerdMartApplicationModule {
    private Context mApplicationContext;

    public NerdMartApplicationModule(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Provides
    NerdMartServiceInterface providesNerdMartServiceInterface() {
        return new NerdMartService();
    }
}
