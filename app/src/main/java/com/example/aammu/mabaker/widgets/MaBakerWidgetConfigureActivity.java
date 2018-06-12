package com.example.aammu.mabaker.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.aammu.mabaker.R;
import com.example.aammu.mabaker.model.Recipe;
import com.example.aammu.mabaker.utils.RetrofitApi;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The configuration screen for the {@link MaBakerWidget MaBakerWidget} AppWidget.
 */
public class MaBakerWidgetConfigureActivity extends Activity {
    private static final String PREFS_NAME = "com.example.aammu.mabaker.widgets.MaBakerWidgetConfiguration";
    private static final String PREF_RECIPE = "pref_recipe";
    private static final String PREF_TITLE = "pref_title";
    @BindView(R.id.appwidget_text)
    RadioGroup group;
    public static String result;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Recipe recipe;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = MaBakerWidgetConfigureActivity.this;
            saveTitlePref(context,mAppWidgetId,result, recipe.getRecipe_name());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            MaBakerWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public MaBakerWidgetConfigureActivity() {

        super();
    }

    static void saveTitlePref(Context context, int appWidgetId, String text, String title) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_RECIPE, text);
        prefs.putString(PREF_TITLE,title);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_TITLE, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadDetailPref(Context context,int AppWidgetId){
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String ingredients = prefs.getString(PREF_RECIPE, null);
        if (ingredients != null) {
            return ingredients;
        } else {
            return context.getString(R.string.configure);
        }
    }
    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_RECIPE);
        prefs.apply();
    }
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.ma_baker_widget_configure);
        ButterKnife.bind(this);
        getData();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
        //result = loadTitlePref(MaBakerWidgetConfigureActivity.this,mAppWidgetId);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
    }

    private void getData() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<List<Recipe>> call = retrofitApi.getRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                final List<Recipe> responseList = response.body();
                for(int i=0;i<responseList.size();i++){
                    RadioButton radioButton = new RadioButton(getApplicationContext());
                    radioButton.setId(i);
                    radioButton.setTextColor(Color.BLACK);
                    radioButton.setText(responseList.get(i).getRecipe_name());
                    radioButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((RadioGroup) v.getParent()).check(v.getId());
                            recipe = responseList.get(v.getId());
                            result = parseData(recipe);
                        }
                    });
                    group.addView(radioButton);

                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(getString(R.string.error),t.getMessage());
            }
        });
    }

    private String parseData(Recipe recipe) {
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
        return stringBuilder.toString();
    }
}

