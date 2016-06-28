package com.bignerdranch.android.initialnerdmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import timber.log.Timber;

public class ProductsFragment extends NerdMartAbstractFragment {
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        updateUI();
        return view;
    }

    private void updateUI() {
        addSubscription(mNerdMartServiceManager
                .getProducts()
                .toList()
                .compose(loadingTransformer())
                .subscribe(products -> {
                    Timber.i("received products: " + products);
                }));
    }
}
