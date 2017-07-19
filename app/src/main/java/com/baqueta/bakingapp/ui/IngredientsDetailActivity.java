package com.baqueta.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by CarH on 01/07/2017.
 */

public class IngredientsDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_detail_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.recipe_ingredients));

        if (savedInstanceState == null) {
            ArrayList<Ingredient> ingredients = getIntent()
                    .getParcelableArrayListExtra(IngredientsDetailFragment.INGREDIENTS_KEY);

            Timber.d("Number of ingredients: " + ingredients.size());

            IngredientsDetailFragment detailFragment = IngredientsDetailFragment.instance(ingredients);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, detailFragment)
                    .commit();
        }
    }
}
