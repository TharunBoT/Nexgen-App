package com.example.nexgen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexgen.Cart.CartAdapter;
import com.example.nexgen.Cart.CartViewModel;
import com.example.nexgen.ProductFetch.Product;
import com.example.nexgen.R;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnCartItemInteractionListener {

    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private TextView totalPriceTextView;

    public CartFragment() {
        //nothing
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        totalPriceTextView = view.findViewById(R.id.total_price);

        RecyclerView recyclerView = view.findViewById(R.id.cart_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        cartViewModel.getCartItems().observe(getViewLifecycleOwner(), this::updateCart);

        return view;
    }

    private void updateCart(List<Product> products) {
        cartAdapter = new CartAdapter(products, this);
        RecyclerView recyclerView = getView().findViewById(R.id.cart_recycler_view);
        recyclerView.setAdapter(cartAdapter);

        calculateTotal(products);
    }

    private void calculateTotal(List<Product> products) {
        double total = 0;
        for (Product product : products) {
            total += product.getRegularPrice() * product.getQuantity();
        }
        // Display the total
        totalPriceTextView.setText(String.format("Total: $%.2f", total));
    }

    @Override
    public void onIncreaseQuantity(Product product) {
        product.setQuantity(product.getQuantity() + 1);
        cartViewModel.updateCart();
    }

    @Override
    public void onDecreaseQuantity(Product product) {
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
            cartViewModel.updateCart();
        }
    }
}
