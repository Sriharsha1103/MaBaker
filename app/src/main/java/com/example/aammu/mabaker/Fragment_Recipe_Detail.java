package com.example.aammu.mabaker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aammu.mabaker.model.Recipe;
import com.example.aammu.mabaker.model.Steps;
import com.example.aammu.mabaker.utils.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment_Recipe_Detail extends Fragment {

    public static final String SAVED_LAYOUT_MANAGER = "layout-manager-state";
    RecyclerAdapter adapter;
    @BindView(R.id.id_steps_recycle)
    RecyclerView list_recipe_view;
    @BindView(R.id.id_ingredient_text)
    TextView ingredients;
    private Bundle getBundle;
    private boolean twoPaneMode;
    Fragment fragment;

    public Fragment_Recipe_Detail() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(fragment==null && savedInstanceState!=null)
            fragment=this.getChildFragmentManager().getFragment(savedInstanceState,"RecipeFragment");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe_fragment1,container,false);
        ButterKnife.bind(this,v);
        getBundle = this.getArguments();
        twoPaneMode = getBundle.getBoolean("isTablet");
            Recipe recipe = getBundle.getParcelable("recipe");
            setIngredients(recipe);
            setDataToAdapter(recipe);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          }

    private void setIngredients(Recipe recipe) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" * ");
        stringBuilder.append(recipe.getRecipe_ingredientsList().get(0).getIngredient());
        stringBuilder.append(" ");
        stringBuilder.append("(");
        float d = recipe.getRecipe_ingredientsList().get(0).getQuantity();
        if((d%1)!=0)
            stringBuilder.append(d);
        else
            stringBuilder.append((int)d);
        stringBuilder.append(" ");
        stringBuilder.append(recipe.getRecipe_ingredientsList().get(0).getMeasure());
        stringBuilder.append(")");
        for(int i=1;i<recipe.getRecipe_ingredientsList().size();i++){
            stringBuilder.append("\n");
            stringBuilder.append(" * ");
            stringBuilder.append(recipe.getRecipe_ingredientsList().get(i).getIngredient());
            stringBuilder.append(" ");
            stringBuilder.append("(");
            d = recipe.getRecipe_ingredientsList().get(i).getQuantity();
            if((d%1)!=0)
                stringBuilder.append(d);
            else
                stringBuilder.append((int)d);
            stringBuilder.append(" ");
            stringBuilder.append(recipe.getRecipe_ingredientsList().get(i).getMeasure());
            stringBuilder.append(")");

        }
        ingredients.setText(stringBuilder);
    }

    private void setDataToAdapter(final Recipe recipe) {
        final List<Steps> stepsList;
        stepsList = recipe.getRecipe_stepsList();
        List<Object> stepsObject = new ArrayList<>();
        stepsObject.addAll(stepsList);
        adapter = new RecyclerAdapter(stepsObject, getActivity(), new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(Recipe recipe) {

            }

            @Override
            public void onItemClickListener(Steps steps) {
                Fragment_Steps_Detail fragment_steps_detail = new Fragment_Steps_Detail();
                getBundle.putParcelable("steps",recipe);
                getBundle.putInt("position",steps.getStep_id());
                fragment_steps_detail.setArguments(getBundle);
                FragmentManager manager = getFragmentManager();
                if(twoPaneMode) {
                    manager.beginTransaction()
                            .replace(R.id.id_fragment_steps_detail, fragment_steps_detail)
                            .addToBackStack(null)
                            .commit();
                }
                else
                {
                    manager.beginTransaction()
                            .replace(R.id.id_frag_ingredients, fragment_steps_detail)
                            .addToBackStack(null)
                            .commit();

                }
            }
        },RecyclerAdapter.LIST_STEPS);
        list_recipe_view.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        list_recipe_view.setAdapter(adapter);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe",getBundle.getParcelable("recipe"));
        outState.putInt("position",getBundle.getInt("position"));
        if(fragment!=null)
            getChildFragmentManager().putFragment(outState,"RecipeFragment",fragment);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, list_recipe_view.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
               getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
