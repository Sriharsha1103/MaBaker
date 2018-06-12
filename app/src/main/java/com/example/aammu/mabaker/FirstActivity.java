package com.example.aammu.mabaker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.aammu.mabaker.model.Recipe;
import com.example.aammu.mabaker.model.Steps;
import com.example.aammu.mabaker.utils.RecyclerAdapter;
import com.example.aammu.mabaker.utils.RetrofitApi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstActivity extends AppCompatActivity {

    RecyclerAdapter adapter;
    @BindView(R.id.id_recipe_recycle)
    RecyclerView list_recipe_view;
    @BindView(R.id.id_first_activity_layout)
    LinearLayout layout;
    Bundle bundle= new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        if (checkNetwork()) {
            if(savedInstanceState==null) {
                getData();
            }
            else{
                List<Recipe> recipeList = savedInstanceState.getParcelableArrayList(getString(R.string.downloadedList));
                addToAdapter(recipeList);
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                list_recipe_view.setLayoutManager(new GridLayoutManager(this,1));
            else
                list_recipe_view.setLayoutManager(new GridLayoutManager(this,2));

        }
        else{
            Snackbar snackbar = Snackbar
                    .make(layout, R.string.no_internet, Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirstActivity.this.finish();
                            startActivity(new Intent(FirstActivity.this,FirstActivity.class));
                        }
                    });
            snackbar.show();
        }
    }
    public boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if(connectivityManager!=null){
            info = connectivityManager.getActiveNetworkInfo();
        }
        if(info == null){
            return false;
        }
        return true;

    }

    private void getData() {
        Snackbar.make(layout, R.string.loading_internet,Snackbar.LENGTH_SHORT).show();;
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<List<Recipe>> call = retrofitApi.getRecipe();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.isSuccessful()) {

                    List<Recipe> responseList = response.body();
                    bundle.putParcelableArrayList(getString(R.string.downloadedList), (ArrayList<? extends Parcelable>) responseList);
                    addToAdapter(responseList);
                    onSaveInstanceState(bundle);
                }
                else
                {
                    Converter<ResponseBody, Recipe> errorConverter = retrofit.responseBodyConverter(Recipe.class, new Annotation[0]);
                    Recipe error = null;
                    try {
                        error = errorConverter.convert(response.errorBody());
                        Log.e(getString(R.string.error),error.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(getString(R.string.error),t.getMessage());
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(getString(R.string.downloadedList), bundle.getParcelableArrayList(getString(R.string.downloadedList)));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        bundle = savedInstanceState;
    }

    private void addToAdapter(List<Recipe> recipeList){
        List<Object> objectList= new ArrayList<>();
        objectList.addAll(recipeList);
        adapter = new RecyclerAdapter(objectList,FirstActivity.this, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(Recipe recipe) {
                Intent intent = new Intent(FirstActivity.this,DetailActivity.class);
                intent.putExtra(getString(R.string.recipe),recipe);
                startActivity(intent);
            }

            @Override
            public void onItemClickListener(Steps steps) {

            }
        },RecyclerAdapter.LIST_RECIPE);
        list_recipe_view.setAdapter(adapter);
    }
}
