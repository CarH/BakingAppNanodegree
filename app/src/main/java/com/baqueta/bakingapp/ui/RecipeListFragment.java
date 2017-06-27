package com.baqueta.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by CarH on 04/05/2017.
 */

public class RecipeListFragment extends Fragment {
    private static final String LOG_TAG = RecipeListFragment.class.getSimpleName();

    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerView;

    @BindView(R.id.recyclerview_recipes_empty)
    TextView mEmptyView;

    RecipesAdapter mAdapter;

    public RecipeListFragment(){}

    public interface RecipeService {
        @GET("android-baking-app-json")
        Call<List<Recipe>> listRecipes();
    }

    public class RecipesCallback implements Callback<List<Recipe>> {
        @Override
        public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
            mEmptyView.setVisibility(View.GONE);
            mAdapter.swap(response.body());
        }

        @Override
        public void onFailure(Call<List<Recipe>> call, Throwable t) {
            mEmptyView.setText(R.string.could_not_fetch_recipes);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }


    public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

        private List<Recipe> recipes;


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            @BindView(R.id.list_item_text)
            TextView title;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                this.title.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), ((TextView)view).getText(), Toast.LENGTH_SHORT).show();
                int selectedPosition = getAdapterPosition();
               
            }
        }

        public RecipesAdapter() {
            this.recipes = new ArrayList<Recipe>();
        }

        @Override
        public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
            return new RecipesAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecipesAdapter.ViewHolder holder, int position) {
            Recipe recipe = this.recipes.get(position);
            holder.title.setText(recipe.getName());
        }

        @Override
        public int getItemCount() {
            return this.recipes.size();
        }

        public void swap(List<Recipe> recipes) {
            this.recipes = recipes;
            notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_list_fragment, container, false);
        ButterKnife.bind(this, root);

        try {
            // To improve performance
            this.mRecyclerView.setHasFixedSize(true);

            // Set layout Manager
            this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            // Set the data source
            this.mAdapter = new RecipesAdapter();
            this.mRecyclerView.setAdapter(this.mAdapter);

            fetchRecipes();
        }catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return root;
    }

    private void fetchRecipes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://go.udacity.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RecipeService recipeService = retrofit.create(RecipeService.class);
        Call<List<Recipe>> call = recipeService.listRecipes();
        call.enqueue(new RecipesCallback());
    }
}
