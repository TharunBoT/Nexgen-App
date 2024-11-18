package com.example.nexgen.Cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.nexgen.ProductFetch.Product;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel {
    private final MutableLiveData<List<Product>> cartItems = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Product>> getCartItems() {
        return cartItems;
    }

    public void addProductToCart(Product product) {
        List<Product> currentCart = cartItems.getValue();
        if (currentCart != null) {
            currentCart.add(product);
            cartItems.setValue(currentCart);  // Trigger update
        }
    }

    public void updateCart() {
        cartItems.setValue(cartItems.getValue());  // Force update
    }
}

