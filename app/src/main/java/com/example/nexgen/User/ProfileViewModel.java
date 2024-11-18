package com.example.nexgen.User;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private Bitmap profileImageBitmap;

    public Bitmap getProfileImageBitmap() {
        return profileImageBitmap;
    }

    public void setProfileImageBitmap(Bitmap profileImageBitmap) {
        this.profileImageBitmap = profileImageBitmap;
    }
}

