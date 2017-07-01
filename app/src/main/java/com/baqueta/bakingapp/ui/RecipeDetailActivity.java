package com.baqueta.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CarH on 27/06/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity {
    public static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Recipe recipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra("recipe");
        getSupportActionBar().setTitle(recipe.getName());

        Log.d(LOG_TAG, "Recipe received: " + recipe.getName());

        if (savedInstanceState == null) {
            RecipeDetailFragment detailFragment = RecipeDetailFragment.instance(recipe);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_container, detailFragment)
                    .commit();
        }
    }
}