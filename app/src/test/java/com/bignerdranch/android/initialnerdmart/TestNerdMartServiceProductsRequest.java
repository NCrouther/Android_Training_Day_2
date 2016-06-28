package com.bignerdranch.android.initialnerdmart;

import com.bignerdranch.android.initialnerdmart.inject.NerdMartMockApplicationModule;
import com.bignerdranch.android.initialnerdmart.model.DataStore;
import com.bignerdranch.android.initialnerdmart.model.service.NerdMartServiceManager;
import com.bignerdranch.android.nerdmartservice.model.NerdMartDataSourceInterface;
import com.bignerdranch.android.nerdmartservice.service.payload.Cart;
import com.bignerdranch.android.nerdmartservice.service.payload.Product;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class TestNerdMartServiceProductsRequest {
    @Inject
    NerdMartServiceManager mNerdMartServiceManager;
    @Inject
    DataStore mDataStore;
    @Inject
    NerdMartDataSourceInterface mNerdMartDataSourceInterface;

    @Singleton
    @Component(modules = NerdMartMockApplicationModule.class)
    public interface TestNerdMartServiceProductsComponent {
        TestNerdMartServiceProductsRequest inject(
                TestNerdMartServiceProductsRequest testNerdMartServiceManager);
    }

    @Before
    public void setup() {
        NerdMartMockApplicationModule nerdMartMockApplicationModule
                = new NerdMartMockApplicationModule(RuntimeEnvironment.application);
        DaggerTestNerdMartServiceProductsRequest_TestNerdMartServiceProductsComponent
                .builder()
                .nerdMartMockApplicationModule(nerdMartMockApplicationModule)
                .build()
                .inject(this);
        mDataStore.setCachedUser(mNerdMartDataSourceInterface.getUser());
    }

    @Test
    public void testGetProductsReturnsExpectedProductListings() {
        TestSubscriber<List<Product>> subscriber = new TestSubscriber<>();
        mNerdMartServiceManager.getProducts().toList().subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        assertThat(subscriber.getOnNextEvents()
                .get(0)).containsAll(mNerdMartDataSourceInterface.getProducts());
    }

    @Test
    public void testGetCartReturnsCartAndCachesCartInDataStore() {
        TestSubscriber<Cart> subscriber = new TestSubscriber<>();
        mNerdMartServiceManager.getCart().subscribe(subscriber);
        subscriber.awaitTerminalEvent();
        Cart actual = subscriber.getOnNextEvents().get(0);
        assertThat(actual).isNotEqualTo(null);
        assertThat(mDataStore.getCachedCart()).isEqualTo(actual);
        assertThat(mDataStore.getCachedCart().getProducts()).hasSize(0);
    }

    @Test
    public void testPostProductToCartAddsProductsToUserCart() {
        ArrayList<Product> products = Lists.newArrayList();
        TestSubscriber<Boolean> postProductsSubscriber = new TestSubscriber<>();
        products.addAll(mNerdMartDataSourceInterface.getProducts());
        mNerdMartServiceManager.postProductToCart(products.get(0))
                .subscribe(postProductsSubscriber);
        postProductsSubscriber.awaitTerminalEvent();
        assertThat(postProductsSubscriber.getOnNextEvents().get(0)).isEqualTo(true);
        TestSubscriber<Cart> cartSubscriber = new TestSubscriber<>();
        mNerdMartServiceManager.getCart().subscribe(cartSubscriber);
        cartSubscriber.awaitTerminalEvent();
        Cart cart = cartSubscriber.getOnNextEvents().get(0);
        assertThat(cart.getProducts()).hasSize(1);
    }
}