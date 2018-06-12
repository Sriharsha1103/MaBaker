package com.example.aammu.mabaker.model;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe implements Parcelable{

    @SerializedName("id")
    private int recipe_id;
    @SerializedName("name")
    private String recipe_name;
    @SerializedName("ingredients")
    private List<Ingredients> recipe_ingredientsList;
    @SerializedName("steps")
    private List<Steps> recipe_stepsList;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    protected Recipe(Parcel in) {
        recipe_id = in.readInt();
        recipe_name = in.readString();
        recipe_ingredientsList = in.createTypedArrayList(Ingredients.CREATOR);
        recipe_stepsList = in.createTypedArrayList(Steps.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    public Recipe() {
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public List<Ingredients> getRecipe_ingredientsList() {
        return recipe_ingredientsList;
    }

    public void setRecipe_ingredientsList(List<Ingredients> recipe_ingredientsList) {
        this.recipe_ingredientsList = recipe_ingredientsList;
    }

    public List<Steps> getRecipe_stepsList() {
        return recipe_stepsList;
    }

    public void setRecipe_stepsList(List<Steps> recipe_stepsList) {
        this.recipe_stepsList = recipe_stepsList;
    }

    public Recipe(int recipe_id, String recipe_name, List<Ingredients> recipe_ingredientsList, List<Steps> recipe_stepsList, int servings, String image) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.recipe_ingredientsList = recipe_ingredientsList;
        this.recipe_stepsList = recipe_stepsList;
        this.servings = servings;
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }


    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getRecipe_id());
        dest.writeString(getRecipe_name());
        dest.writeTypedList(recipe_ingredientsList);
        dest.writeTypedList(recipe_stepsList);
        dest.writeInt(getServings());
        dest.writeString(getImage());
    }
}
