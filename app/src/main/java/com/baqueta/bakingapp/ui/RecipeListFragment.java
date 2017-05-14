package com.baqueta.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baqueta.bakingapp.R;

/**
 * Created by CarH on 04/05/2017.
 */

public class RecipeListFragment extends Fragment {

    public RecipeListFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_list, container, false);
        return root;
    }
}
