package com.example.android.baking.contract;

import android.net.Uri;

public class RecipeContract {

    private static final String AUTHORITY = "com.example.android.baking";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
}
