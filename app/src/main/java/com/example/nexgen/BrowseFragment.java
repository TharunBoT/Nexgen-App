package com.example.nexgen;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nexgen.ProductFetch.Product;
import com.example.nexgen.ProductFetch.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BrowseFragment extends Fragment {

    private static final String TAG = "BrowseFragment";
    private static final String URL = "http://192.168.8.120/MyApi/api.php";
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, getContext());
        recyclerView.setAdapter(productAdapter);

        fetchProducts();
        return view;
    }

    private void fetchProducts() {
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "Error fetching data", e);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        String jsonData = response.body().string();
                        parseJsonData(jsonData);
                    } else {
                        Log.e(TAG, "Request not successful: " + response.code());
                    }
                }
            });
        } else {
            fetchProductsFromLocal();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchProductsFromLocal() {
        InputStream inputStream = getResources().openRawResource(R.raw.local_products);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            parseJsonData(builder.toString());
        } catch (IOException e) {
            Log.e(TAG, "Error reading local JSON file", e);
        }
    }

    private void parseJsonData(String jsonData) {
        try {
            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject product = jsonArray.getJSONObject(i);
                int id = product.getInt("id");
                String name = product.getString("name");
                String shortDescription = product.getString("short_description");
                String description = product.getString("description");
                double regularPrice = product.getDouble("regular_price");
                String image = product.getString("image");

                Product newProduct = new Product(id, name, shortDescription, description, regularPrice, image);
                getActivity().runOnUiThread(() -> {
                    productList.add(newProduct);
                    productAdapter.notifyItemInserted(productList.size() - 1);
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON data", e);
        }
    }
}
