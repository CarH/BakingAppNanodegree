package com.baqueta.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.AppCompatTextView;
import android.test.InstrumentationTestCase;

import com.baqueta.bakingapp.ui.RecipeListActivity;
import com.baqueta.bakingapp.ui.RecipeListFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by CarH on 19/07/2017.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListTest extends InstrumentationTestCase{
    @Rule public ActivityTestRule<RecipeListActivity> mActivityTestRule =
            new ActivityTestRule<RecipeListActivity>(RecipeListActivity.class, true, false);

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        server = new MockWebServer();
        server.start();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        RecipeListFragment.BASE_URL = server.url("/").toString();
    }

    @Test
    public void testRecipeItemsDisplayed() throws InterruptedException {
        setupServerSuccessfulResponse();

        // Initializes the activity to be tested
        mActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.recyclerview_recipes)).check(matches(isDisplayed()));
    }

    @Test
    public void testNoResponse() {
        setupServerBadResponse();
        mActivityTestRule.launchActivity(new Intent());
        onView(withId(R.id.recyclerview_recipes_empty)).check(matches(isDisplayed()));
    }

    @Test
    public void recipeItemClick_DetailActivityLaunch() throws InterruptedException {
        setupServerSuccessfulResponse();
        mActivityTestRule.launchActivity(new Intent());


        final int FIRST_RECIPE_POSITION = 0;
        final String FIRST_RECIPE_NAME = "Nutella Pie Mock";
        onView(withId(R.id.recyclerview_recipes))
                .perform(actionOnItemAtPosition(FIRST_RECIPE_POSITION, click()));

            onView(allOf(isAssignableFrom(AppCompatTextView.class), withParent(isAssignableFrom(android.support.v7.widget.Toolbar.class))))
               .check(matches(withText(FIRST_RECIPE_NAME)));
    }


    private void setupServerBadResponse() {
        setupServer("", HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    private void setupServerSuccessfulResponse() {
        String successfulResponseBody = getSuccessfulResponseBody();
        setupServer(successfulResponseBody, HttpURLConnection.HTTP_OK);
    }

    private void setupServer(String responseBody, int responseCode) {
        server.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setResponseCode(responseCode)
                .setBody(responseBody));
    }

    @NonNull
    private String getSuccessfulResponseBody() {
        final String filename = "successful_response.json";
        StringBuilder builder = new StringBuilder();

        try {
            InputStream is = getInstrumentation().getContext().getResources().getAssets().open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ( (line = reader.readLine()) != null)
                builder.append(line);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    @After
    public void tearDown() throws IOException {
        if (server != null) server.shutdown();
    }
}
