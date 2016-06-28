package com.bignerdranch.android.initialnerdmart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.nerdmartservice.service.NerdMartServiceInterface;

import javax.inject.Inject;

public abstract class NerdMartAbstractFragment extends Fragment {
    @Inject
    NerdMartServiceInterface mNerdMartServiceInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NerdMartApplication.component(getContext()).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
