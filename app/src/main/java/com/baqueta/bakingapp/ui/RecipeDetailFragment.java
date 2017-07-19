package com.baqueta.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baqueta.bakingapp.R;
import com.baqueta.bakingapp.entities.Ingredient;
import com.baqueta.bakingapp.entities.Recipe;
import com.baqueta.bakingapp.entities.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CarH on 26/06/2017.
 */

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.recyclerview_recipe_detail)
    RecyclerView mDetailRecyclerView;

    private DetailRecipeAdapter mAdapter;


    public interface IngredientDetailClick {
        public void onClick(ArrayList<Ingredient> ingredients);
    }

    public interface StepDetailClick {
        public void onClick(Step step);
    }

    public RecipeDetailFragment(){}

    public static RecipeDetailFragment instance(Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("recipe", recipe);
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.ViewHolder> {
        private final int INGREDIENT_TYPE = 0;
        private final int STEP_TYPE = 1;

        private Recipe recipe;

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            @BindView(R.id.textview_recipe_step_desc)
            TextView stepDesc;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (getItemViewType() == INGREDIENT_TYPE) {
                    ((IngredientDetailClick)getActivity())
                            .onClick((ArrayList<Ingredient>)recipe.getIngredients());
                } else if (getItemViewType() == STEP_TYPE){
                    ((StepDetailClick)getActivity())
                            .onClick(recipe.getSteps().get(getAdapterPosition()-1));
                }
            }
        }


        public DetailRecipeAdapter() {
            this.recipe = new Recipe();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DetailRecipeAdapter.ViewHolder holder, int position) {

            switch (holder.getItemViewType()){
                case INGREDIENT_TYPE: {
                    holder.stepDesc.setText(R.string.recipe_ingredients);
                    break;
                }
                case STEP_TYPE:
                {
                    final Step step = recipe.getSteps().get(position - 1);
                    holder.stepDesc.setText(step.getShortDescription());
                    break;
                }
                default:
                    throw new RuntimeException("View Type unknown. ViewType: " + holder.getItemViewType());
            }
        }

        @Override
        public int getItemCount() {
            return 1 + recipe.getSteps().size();
        }

        @Override
        public int getItemViewType(int position) {
            return (position < 1) ? INGREDIENT_TYPE : STEP_TYPE;
        }

        public void swap(Recipe recipe) {
            this.recipe = recipe;
            notifyDataSetChanged();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        ButterKnife.bind(this, root);

        mAdapter = new DetailRecipeAdapter();
        mDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDetailRecyclerView.setAdapter(mAdapter);

        return root;
    }

    public void setRecipe(Recipe recipe) {
        this.mAdapter.swap(recipe);
    }
}
