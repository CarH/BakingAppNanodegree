package com.baqueta.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Ingredient;
import com.baqueta.bakingapp.entities.Recipe;
import com.baqueta.bakingapp.entities.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by CarH on 27/06/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.IngredientDetailClick, RecipeDetailFragment.StepDetailClick{
    public static final String LOG_TAG = RecipeDetailActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Nullable @BindView(R.id.recipe_detail_container)
    FrameLayout detailContainer;

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mRecipe = intent.getParcelableExtra("recipe");
        getSupportActionBar().setTitle(mRecipe.getName());

        mTwoPane = detailContainer != null;

        Timber.d("Is Two Pane Mode? " + mTwoPane);
    }

    @Override
    public void onStart() {
        super.onStart();
        RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.recipe_detail_fragment);
        recipeDetailFragment.setRecipe(mRecipe);
    }

    @Override
    public void onClick(ArrayList<Ingredient> ingredients) {
        if (!mTwoPane) {
            Intent intent = new Intent(getApplicationContext(), IngredientsDetailActivity.class);
            intent.putParcelableArrayListExtra("ingredients", ingredients);
            startActivity(intent);
        } else {
            IngredientsDetailFragment detailFragment = IngredientsDetailFragment.instance(ingredients);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, detailFragment)
                    .commit();
        }
    }

    @Override
    public void onClick(Step step) {
        if (!mTwoPane) {
            Intent intent = new Intent(getApplicationContext(), StepDetailActivity.class);
            intent.putParcelableArrayListExtra("mSteps", (ArrayList<Step>)mRecipe.getSteps());
            intent.putExtra("stepId", step.getId());
            startActivity(intent);
        } else {
            StepDetailFragment detailFragment = StepDetailFragment
                    .instance((ArrayList<Step>)mRecipe.getSteps(), step.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, detailFragment)
                    .commit();
        }
    }
}