package com.example.nexgen.Cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nexgen.ProductFetch.Product;
import com.example.nexgen.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<Product> cartItems;
    private final OnCartItemInteractionListener listener;

    public CartAdapter(List<Product> cartItems, OnCartItemInteractionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view, listener, cartItems);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$%.2f", product.getRegularPrice()));
        holder.quantityText.setText(String.valueOf(product.getQuantity()));

        // Image loading: handle drawable resources or URLs
        String imageName = product.getImage().replace(".jpg", "").replace(".png", "");
        int imageResource = holder.productImage.getContext().getResources().getIdentifier(imageName, "drawable", holder.productImage.getContext().getPackageName());

        if (imageResource != 0) {
            Glide.with(holder.productImage.getContext()).load(imageResource).into(holder.productImage);
        } else {

        }

        holder.increaseButton.setOnClickListener(v -> listener.onIncreaseQuantity(product));
        holder.decreaseButton.setOnClickListener(v -> listener.onDecreaseQuantity(product));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice, quantityText;
        public Button increaseButton, decreaseButton, removeButton;

        public CartViewHolder(@NonNull View itemView, OnCartItemInteractionListener listener, List<Product> cartItems) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            quantityText = itemView.findViewById(R.id.quantity_text);
            increaseButton = itemView.findViewById(R.id.button_increase);
            decreaseButton = itemView.findViewById(R.id.button_decrease);
            removeButton = itemView.findViewById(R.id.button_remove);

            // Attach listeners that invoke interface methods
            increaseButton.setOnClickListener(v -> listener.onIncreaseQuantity(cartItems.get(getAdapterPosition())));
            decreaseButton.setOnClickListener(v -> listener.onDecreaseQuantity(cartItems.get(getAdapterPosition())));
        }
    }

    public interface OnCartItemInteractionListener {
        void onIncreaseQuantity(Product product);
        void onDecreaseQuantity(Product product);
    }
}
