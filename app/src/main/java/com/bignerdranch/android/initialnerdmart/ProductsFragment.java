package com.bignerdranch.android.initialnerdmart;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.initialnerdmart.databinding.FragmentProductsBinding;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;

import java.util.Collections;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class ProductsFragment extends NerdMartAbstractFragment {
    private ProductRecyclerViewAdapter mAdapter;
    private FragmentProductsBinding mFragmentProductsBinding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragmentProductsBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_products, container, false);
        ProductRecyclerViewAdapter.AddProductClickEvent addProductClickEvent
                = this::postProductToCart;
        mAdapter = new ProductRecyclerViewAdapter(Collections.EMPTY_LIST,
                getActivity(), addProductClickEvent);
        setupAdapter();
        updateUI();
        return mFragmentProductsBinding.getRoot();
    }

    private void setupAdapter() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(getActivity());
        mFragmentProductsBinding.fragmentProductsRecyclerView
                .setLayoutManager(linearLayoutManager);
        mFragmentProductsBinding.fragmentProductsRecyclerView
                .setAdapter(mAdapter);
    }

    private void updateUI() {
        addSubscription(mNerdMartServiceManager
                .getProducts()
                .toList()
                .compose(loadingTransformer())
                .subscribe(products -> {
                    Timber.i("received products: " + products);
                    mAdapter.setProducts(products);
                    mAdapter.notifyDataSetChanged();
                }));
    }

    private void postProductToCart(Product product) {
        Observable<Boolean> cartSuccessObservable = mNerdMartServiceManager
                .postProductToCart(product)
                .compose(loadingTransformer())
                .cache();
        Subscription cartUpdateNotificationObservable = cartSuccessObservable
                .subscribe(aBoolean -> {
                    int message = aBoolean ? R.string.product_add_success_message :
                            R.string.product_add_failure_message;
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                });
        addSubscription(cartUpdateNotificationObservable);
        addSubscription(cartSuccessObservable.filter(aBoolean -> aBoolean)
                .subscribeOn(Schedulers.newThread())
                .flatMap(aBoolean -> mNerdMartServiceManager.getCart())
                .subscribe(cart -> {
                    ((NerdMartAbstractActivity) getActivity()).updateCartStatus(cart);
                    updateUI();
                }));
    }
}
