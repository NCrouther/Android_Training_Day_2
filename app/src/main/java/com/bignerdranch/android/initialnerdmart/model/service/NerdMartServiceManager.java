package com.bignerdranch.android.initialnerdmart.model.service;

import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;
import com.bignerdranch.android.nerdmartservice.service.payload.Cart;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;

import java.util.UUID;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class NerdMartServiceManager {
    private NerdMartServiceInterface mServiceInterface;
    private DataStore mDataStore;
    private Scheduler mScheduler;

    private final Observable.Transformer<Observable, Observable>
            mSchedulersTransformer = observable ->
            observable.subscribeOn(Schedulers.newThread())
                    .observeOn(mScheduler);

    public NerdMartServiceManager(
            NerdMartServiceInterface serviceInterface,
            DataStore dataStore,
            Scheduler scheduler) {
        mServiceInterface = serviceInterface;
        mDataStore = dataStore;
        mScheduler = scheduler;
    }

    @SuppressWarnings("unchecked")
    private <T> Observable.Transformer<T, T> applySchedulers() {
        return (Observable.Transformer<T, T>) mSchedulersTransformer;
    }

    public Observable<Boolean> authenticate(String username, String password) {
        return mServiceInterface.authenticate(username, password)
                .doOnNext(mDataStore::setCachedUser)
                .map(user -> user != null)
                .compose(applySchedulers());
    }

    private Observable<UUID> getToken() {
        return Observable.just(mDataStore.getCachedAuthToken());
    }

    public Observable<Product> getProducts() {
        return getToken().flatMap(mServiceInterface::requestProducts)
                .doOnNext(mDataStore::setCachedProducts)
                .flatMap(Observable::from)
                .compose(applySchedulers());
    }

    public Observable<Cart> getCart() {
        return getToken().flatMap(mServiceInterface::fetchUserCart)
                .doOnNext(mDataStore::setCachedCart)
                .compose(applySchedulers());
    }

    public Observable<Boolean> postProductToCart(final Product product) {
        return getToken()
                .flatMap(uuid -> mServiceInterface.addProductToCart(uuid, product))
                .compose(applySchedulers());
    }

    public Observable<Boolean> signout() {
        mDataStore.clearCache();
        return mServiceInterface.signout();
    }
}