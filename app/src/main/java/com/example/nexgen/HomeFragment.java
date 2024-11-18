package com.example.nexgen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private TextView unameDisplay;
    private HorizontalScrollView horizontalScrollView;
    private Handler handler;
    private Runnable scrollRunnable;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        //nothing
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unameDisplay = view.findViewById(R.id.uname_display);

        //Animation
        horizontalScrollView = view.findViewById(R.id.home_horizontal);
        handler = new Handler();
        scrollRunnable = new Runnable() {
            @Override
            public void run() {
                int scrollX = horizontalScrollView.getScrollX();
                int maxScrollX = horizontalScrollView.getChildAt(0).getWidth() - horizontalScrollView.getWidth();

                if (scrollX < maxScrollX) {
                    horizontalScrollView.smoothScrollBy(10, 0);
                } else {
                    horizontalScrollView.smoothScrollTo(0, 0);
                }

                handler.postDelayed(this, 50);
            }
        };

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();

            unameDisplay.setText(name);
        }

        horizontalScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                horizontalScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                handler.post(scrollRunnable);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null && scrollRunnable != null) {
            handler.removeCallbacks(scrollRunnable);
        }
    }
}