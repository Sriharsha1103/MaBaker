package com.example.aammu.mabaker;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.aammu.mabaker.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class DetailActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private boolean mTwoPaneMode;
    @Nullable
    @BindView(R.id.id_fragment_steps_detail)
    FrameLayout frameLayout;
    private Bundle bundle;
    private Recipe recipe;
    private Fragment_Recipe_Detail fragmentRecipeDetail;
    private Fragment_Steps_Detail fragmentStepsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_detail);
        bundle = getIntent().getExtras();
        recipe = bundle.getParcelable(getString(R.string.recipe));
        setTitle(recipe.getRecipe_name());
        ButterKnife.bind(this);
        if(savedInstanceState==null) {
            if (frameLayout != null) {
                mTwoPaneMode = true;
                fragmentRecipeDetail = new Fragment_Recipe_Detail();
                fragmentRecipeDetail.setArguments(bundle);
                bundle.putBoolean(getString(R.string.isTablet), mTwoPaneMode);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.id_frag_ingredients, fragmentRecipeDetail)
                        .commit();
                fragmentStepsDetail = new Fragment_Steps_Detail();
                fragmentStepsDetail.setArguments(bundle);
                manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.id_fragment_steps_detail, fragmentStepsDetail)
                        .commit();
            } else {
                mTwoPaneMode = false;
                Fragment_Recipe_Detail fragmentRecipeDetail = new Fragment_Recipe_Detail();
                fragmentRecipeDetail.setArguments(bundle);
                bundle.putBoolean(getString(R.string.isTablet), mTwoPaneMode);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.id_frag_ingredients, fragmentRecipeDetail)
                        .commit();

            }
        }
        else{
            mTwoPaneMode = savedInstanceState.getBoolean(getString(R.string.isTablet));
            if(mTwoPaneMode){
                fragmentRecipeDetail = (Fragment_Recipe_Detail) getSupportFragmentManager().getFragment(savedInstanceState,"IngredientFragment");
                fragmentStepsDetail = (Fragment_Steps_Detail) getSupportFragmentManager().getFragment(savedInstanceState,"StepsFragment");
            }

            else{
                fragmentRecipeDetail = (Fragment_Recipe_Detail) getSupportFragmentManager().getFragment(savedInstanceState,"StepsFragment");
            }

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipe),bundle.getParcelable(getString(R.string.recipe)));
        outState.putBoolean("isTablet",mTwoPaneMode);
        if(mTwoPaneMode) {
            if(fragmentRecipeDetail!=null && fragmentRecipeDetail.isAdded())
                getSupportFragmentManager().putFragment(outState, getString(R.string.fragment_ingredient), fragmentRecipeDetail);
            if(fragmentStepsDetail!=null && fragmentStepsDetail.isAdded())
                getSupportFragmentManager().putFragment(outState,getString(R.string.fragment_steps),fragmentStepsDetail);
        }
        else{
            if(fragmentRecipeDetail!=null && fragmentRecipeDetail.isAdded())
                getSupportFragmentManager().putFragment(outState, getString(R.string.fragment_ingredient), fragmentRecipeDetail);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable(getString(R.string.recipe));
        mTwoPaneMode = savedInstanceState.getBoolean(getString(R.string.isTablet));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
