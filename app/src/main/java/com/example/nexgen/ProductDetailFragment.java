package com.example.nexgen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.nexgen.Cart.CartViewModel;
import com.example.nexgen.ProductFetch.Product;
import com.example.nexgen.R;

public class ProductDetailFragment extends Fragment {

    private static final String ARG_PRODUCT = "product";
    private Product product;
    private CartViewModel cartViewModel;

    public static ProductDetailFragment newInstance(Product product) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Retrieve the product from the arguments
        if (getArguments() != null) {
            product = (Product) getArguments().getSerializable(ARG_PRODUCT);
        }

        // Bind views
        ImageView productImageView = view.findViewById(R.id.product_image);
        TextView nameTextView = view.findViewById(R.id.product_name);
        TextView priceTextView = view.findViewById(R.id.product_price);
        TextView descriptionTextView = view.findViewById(R.id.product_description);
        Button addToCartButton = view.findViewById(R.id.button_add_to_cart);

        //Set product details to views
        if (product != null) {
            nameTextView.setText(product.getName());
            priceTextView.setText(String.format("$%.2f", product.getRegularPrice()));
            descriptionTextView.setText(product.getDescription());

            String imageName = product.getImage().replace(".jpg", "").replace(".png", "");
            int imageResource = getResources().getIdentifier(imageName, "drawable", getContext().getPackageName());

            if (imageResource != 0) {
                productImageView.setImageResource(imageResource);
            } else {

            }
        }

        cartViewModel = new ViewModelProvider(requireActivity()).get(CartViewModel.class);

        //Set up add to cart button click listener
        addToCartButton.setOnClickListener(v -> {
            if (product != null) {
                cartViewModel.addProductToCart(product);

                Toast.makeText(getActivity(), "Product added to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Product not found", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
