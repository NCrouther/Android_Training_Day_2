package com.bignerdranch.android.initialnerdmart.model;

import com.bignerdranch.android.nerdmartservice.service.payload.Product;
import com.bignerdranch.android.nerdmartservice.service.payload.User;

import java.util.List;
import java.util.UUID;

public class DataStore {
    private User mCachedUser;
    private List<Product> mCachedProducts;

    public UUID getCachedAuthToken() {
        return mCachedUser.getAuthToken();
    }

    public void setCachedUser(User user) {
        mCachedUser = user;
    }

    public User getCachedUser() {
        return mCachedUser;
    }

    public void setCachedProducts(List<Product> products) {
        mCachedProducts = products;
    }
}

