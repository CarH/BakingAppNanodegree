package com.baqueta.bakingapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatTextView;

import com.baqueta.bakingapp.entities.Recipe;
import com.baqueta.bakingapp.entities.Step;
import com.baqueta.bakingapp.ui.RecipeDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by CarH on 23/07/2017.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeDetailTest {
    private static final String  STEP_SHORT_DESC = "Step Short Description";
    private static final String  RECIPE_NAME     = "Mock Recipe";
    private static final int     SERVINGS        = 2;

    @Rule
    public ActivityTestRule<RecipeDetailActivity> mActivityTestRule =
            new ActivityTestRule<RecipeDetailActivity>(RecipeDetailActivity.class, true, false);

    @Before
    public void setupTest() {
        Intent intent = new Intent();
        intent.putExtra("recipe", getRecipe());
        mActivityTestRule.launchActivity(intent);
    }

    private Recipe getRecipe() {
        Recipe recipe = new Recipe();
        recipe.setName(RECIPE_NAME);
        recipe.setServings(SERVINGS);

        List<Step> steps = new ArrayList<>(1);
        Step step = new Step();
        step.setId(0);
        step.setDescription("Step Description");

        step.setShortDescription(STEP_SHORT_DESC);
        steps.add(step);

        recipe.setSteps(steps);
        return recipe;
    }

    @Test
    public void titleMatchesRecipeNameTest() {
        onView(allOf(
                isAssignableFrom(AppCompatTextView.class), withParent(isAssignableFrom(android.support.v7.widget.Toolbar.class))))
                .check(matches(withText(RECIPE_NAME)));
    }

    @Test
    public void stepTitleTest() {
        onView(withId(R.id.recyclerview_recipe_detail))
                .perform(scrollToPosition(1));

        onView(withText(STEP_SHORT_DESC))
                .check(matches(isDisplayed()));

    }
}