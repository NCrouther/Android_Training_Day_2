package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.service.NerdMartService;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NerdMartApplicationModule {
    private Context mApplicationContext;

    public NerdMartApplicationModule(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Provides
    @Singleton
    DataStore providesDataStore() {
        return new DataStore();
    }

    @Provides
    NerdMartServiceInterface providesNerdMartServiceInterface() {
        return new NerdMartService();
    }

    @Provides
    @Singleton
    NerdMartServiceManager providesNerdMartServiceManager(
            NerdMartServiceInterface serviceInterface, DataStore dataStore) {
        return new NerdMartServiceManager(serviceInterface, dataStore);
    }
}
