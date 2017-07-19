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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CarH on 01/07/2017.
 */

public class IngredientsDetailFragment extends Fragment {

    public static final String INGREDIENTS_KEY = "ingredients";

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView recyclerView;


    class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
        ArrayList<Ingredient> ingredients;

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.textview_ingredient_quantity)
            TextView quantity;

            @BindView(R.id.textview_ingredient_title)
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public IngredientsAdapter(ArrayList<Ingredient> ingredients) {
            this.ingredients = ingredients;
        }

        @Override
        public IngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
            return new IngredientsAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(IngredientsAdapter.ViewHolder holder, int position) {
            Ingredient ingredient = ingredients.get(position);
            holder.title.setText(ingredient.getIngredient());
            holder.quantity.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredients_detail_fragment, container, false);
        ButterKnife.bind(this, view);
        ArrayList<Ingredient> ingredients = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new IngredientsAdapter(ingredients));

        return view;
    }

    public static IngredientsDetailFragment instance(ArrayList<Ingredient> ingredients) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(INGREDIENTS_KEY, ingredients);
        IngredientsDetailFragment detailFragment = new IngredientsDetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }


}
