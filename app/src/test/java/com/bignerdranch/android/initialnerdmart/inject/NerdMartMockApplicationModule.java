package com.bignerdranch.android.initialnerdmart.inject;

import android.content.Context;

import com.bignerdranch.android.nerdmartservice.model.NerdDataSource;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {NerdMartMockServiceModule.class, NerdMartCommonModule.class})
public class NerdMartMockApplicationModule {
    private Context mApplicationContext;

    public NerdMartMockApplicationModule(Context applicationContext) {
        mApplicationContext = applicationContext;
    }

    @Provides
    @Singleton
    public NerdMartDataSourceInterface providesNertMartDataSourceInterface() {
        return new NerdDataSource();
    }

    @Provides
    public Context providesContext() {
        return mApplicationContext;
    }
}
