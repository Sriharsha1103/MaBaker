package com.example.aammu.mabaker.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aammu.mabaker.R;
import com.example.aammu.mabaker.model.Ingredients;
import com.example.aammu.mabaker.model.Recipe;
import com.example.aammu.mabaker.model.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LIST_RECIPE = 1;
    public static final int LIST_STEPS = 2;
    private List<Object> recipeList;
    private Activity activity;
    private OnItemClickListener itemClickListener;
    private Recipe recipe;
    private int viewType;

    public interface OnItemClickListener {
        void onItemClickListener(Recipe recipe);

        void onItemClickListener(Steps steps);
    }

    public RecyclerAdapter(List<Object> recipeList, Activity activity, OnItemClickListener itemClickListener, int viewType) {
        this.recipeList = recipeList;
        this.activity = activity;
        this.itemClickListener = itemClickListener;
        this.viewType = viewType;
        Log.i("RAList", recipeList.toString());
    }

    @Override
    public int getItemViewType(int position) {
        if (recipeList.get(position) instanceof Recipe)
            return LIST_RECIPE;
        else
            return LIST_STEPS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = null;
        switch (viewType) {
            case LIST_RECIPE:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe_layout, parent, false);
                ButterKnife.bind(this, v);

                return new ViewHolderRecipe(v);
            case LIST_STEPS:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_steps_layout, parent, false);
                ButterKnife.bind(this, v);

                return new ViewHolderSteps(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case LIST_RECIPE:
                ViewHolderRecipe viewHolderRecipe = (RecyclerAdapter.ViewHolderRecipe) holder;
                recipe = (Recipe) recipeList.get(position);
                viewHolderRecipe.bind(recipe, itemClickListener);
                break;
            case LIST_STEPS:
                ViewHolderSteps viewHolderSteps = (RecyclerAdapter.ViewHolderSteps) holder;
                Steps steps = (Steps) recipeList.get(position);
                viewHolderSteps.bind(steps, itemClickListener);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolderRecipe extends RecyclerView.ViewHolder {


        @BindView(R.id.id_recipe_text)
        TextView recipe_text;
        @BindView(R.id.id_recipe_servings)
        TextView recipe_servings;

        public ViewHolderRecipe(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Recipe recipe, final OnItemClickListener itemClickListener) {

            recipe_text.setText(recipe.getRecipe_name());
            recipe_servings.setText(activity.getString(R.string.servings) + String.valueOf(recipe.getServings()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(recipe);
                }
            });
        }
    }

    public class ViewHolderSteps extends RecyclerView.ViewHolder {
        @BindView(R.id.id_steps_cardview)
        CardView cardView;
        @BindView(R.id.id_steps_id)
        TextView steps_id_text;
        @BindView(R.id.id_steps_short_description)
        TextView steps_short_description;
        @BindView(R.id.id_play_icon)
        ImageView play;

        public ViewHolderSteps(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Steps steps, final OnItemClickListener itemClickListener) {

            if (steps.getVideo_URL().equals("") && steps.getThumbNail_URL().equals("")) {
                play.setContentDescription(null);
                play.setVisibility(GONE);
            } else if (steps.getVideo_URL().equals("")) {
                play.setContentDescription(steps.getThumbNail_URL());

            } else {
                play.setContentDescription(steps.getVideo_URL());

            }
            cardView.setCardBackgroundColor(Color.WHITE);
            steps_id_text.setText(String.format("%s. ", String.valueOf(steps.getStep_id())));
            steps_short_description.setText(String.format(" %s", steps.getShort_descrpition()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cardView.setCardBackgroundColor(activity.getColor(R.color.colorBackground));
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClickListener(steps);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cardView.setCardBackgroundColor(activity.getColor(R.color.colorBackground));
                    }
                }
            });
        }
    }
}
