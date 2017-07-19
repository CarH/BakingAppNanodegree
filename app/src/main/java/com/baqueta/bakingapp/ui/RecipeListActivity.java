package com.baqueta.bakingapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.baqueta.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list_activity);
        ButterKnife.bind(this);
        ButterKnife.setDebug(true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.baking_time);
    }
}
