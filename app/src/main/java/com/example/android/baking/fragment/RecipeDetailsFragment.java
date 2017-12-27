package com.example.android.baking.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking.R;
import com.example.android.baking.adapter.IngredientAdapter;
import com.example.android.baking.adapter.StepAdapter;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeDetailsFragment.RecipeDetailsOnClickListener} interface
 * to handle interaction events.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment implements StepAdapter.StepOnClickHandler {

    @BindView(R.id.recycler_view_ingredients)
    RecyclerView recyclerViewIngredients;

    @BindView(R.id.recycler_view_steps)
    RecyclerView recyclerViewSteps;

    @BindView(R.id.nested_scroll_view_recipe_details)
    NestedScrollView nestedScrollViewRecipeDetails;

    private RecipeDetailsOnClickListener listener;
    private StepAdapter stepAdapter;

    public RecipeDetailsFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment RecipeDetailsFragment.
     */
    public static RecipeDetailsFragment newInstance(Bundle bundle) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setArguments(bundle);
        return recipeDetailsFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        List<Ingredient> ingredients = null;
        List<Step> steps = null;
        if (bundle != null) {
            Recipe recipe = bundle.getParcelable(getString(R.string.recipe));
            if (recipe != null) {
                ingredients = recipe.getIngredients();
                steps = recipe.getSteps();
            }
        }

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        stepAdapter = new StepAdapter(getContext(), steps, this);
        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSteps.setAdapter(stepAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDetailsOnClickListener) {
            listener = (RecipeDetailsOnClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.must_implement)
                    + RecipeDetailsOnClickListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(getString(R.string.scroll_position),
                new int[]{nestedScrollViewRecipeDetails.getScrollX(), nestedScrollViewRecipeDetails.getScrollY()});
        outState.putInt(getString(R.string.selected_row_index), stepAdapter.getSelectedRowIndex());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray(getString(R.string.scroll_position));
            if(position != null) {
                nestedScrollViewRecipeDetails.post(new Runnable() {
                    public void run() {
                        nestedScrollViewRecipeDetails.scrollTo(position[0], position[1]);
                    }
                });
            }
            int selectedRowIndex = savedInstanceState.getInt(getString(R.string.selected_row_index));
            stepAdapter.setSelectedRowIndex(selectedRowIndex);
            stepAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(int position) {
        listener.onStepSelected(position);
    }

    public StepAdapter getStepAdapter() {
        return stepAdapter;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface RecipeDetailsOnClickListener {
        void onStepSelected(int position);
    }
}
