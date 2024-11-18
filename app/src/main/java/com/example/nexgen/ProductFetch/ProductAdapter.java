package com.example.nexgen.ProductFetch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexgen.HomeActivity;
import com.example.nexgen.ProductDetailFragment;
import com.example.nexgen.R;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_product layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set product details
        holder.nameTextView.setText(product.getName());
        holder.shortDescriptionTextView.setText(product.getShortDescription());
        holder.priceTextView.setText(String.valueOf(product.getRegularPrice()));

        // Get the image name and load the image as you already have
        String imageName = product.getImage().replace(".jpg", "").replace(".png", "");
        int imageResource = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        if (imageResource != 0) {
            holder.imageView.setImageResource(imageResource);
        } else {
        }

        // Handle click event to navigate to ProductDetailFragment
        holder.itemView.setOnClickListener(v -> {
            // Make sure context is an instance of HomeActivity to avoid ClassCastException
            if (context instanceof HomeActivity) {
                HomeActivity activity = (HomeActivity) context;

                // Create an instance of ProductDetailFragment and pass the product as an argument
                ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(product);

                // Navigate to ProductDetailFragment
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, productDetailFragment)
                        .addToBackStack(null)  // Add to back stack for navigation
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, shortDescriptionTextView, priceTextView;
        ImageView imageView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_name);
            shortDescriptionTextView = itemView.findViewById(R.id.product_short_description);
            priceTextView = itemView.findViewById(R.id.product_price);
            imageView = itemView.findViewById(R.id.product_image);
        }
    }
}
