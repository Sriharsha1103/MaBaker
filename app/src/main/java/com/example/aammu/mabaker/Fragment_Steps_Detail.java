package com.example.aammu.mabaker;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.aammu.mabaker.model.Recipe;
import com.example.aammu.mabaker.model.Steps;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fragment_Steps_Detail extends Fragment {
    private List<Steps> steps;
    private Adapter myAdapter;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private int position =0 ;
    Fragment fragment;
    public Fragment_Steps_Detail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(fragment==null && savedInstanceState!=null)
            fragment=this.getChildFragmentManager().getFragment(savedInstanceState,"StepsFragment");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.recipe_fragment2,container,false);
        ButterKnife.bind(this,v);
        Bundle bundle = this.getArguments();
        Recipe recipe = bundle.getParcelable("recipe");
        position = bundle.getInt("position");
        steps = recipe.getRecipe_stepsList();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewPager(viewPager);
        myAdapter = new Adapter(getFragmentManager());
        tabs.setViewPager(viewPager);
        myAdapter.notifyDataSetChanged();


    }

    private void setViewPager(ViewPager viewPager){
        myAdapter = new Adapter(getFragmentManager());
        for(int i=0;i<steps.size();i++){
            myAdapter.addFragment(new Fragment_Display(),String.valueOf(steps.get(i).getStep_id()));
        }
        viewPager.setAdapter(myAdapter);
        viewPager.setCurrentItem(position);
    }

    public class Adapter extends FragmentStatePagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment_Display.newInstance(steps.get(position));
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            int pos =steps.get(position).getStep_id();
            String title = "Step :"+String.valueOf(pos);
            return title;
        }
        public void addFragment(Fragment fragment,String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
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


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(fragment!=null)
            getChildFragmentManager().putFragment(outState,"StepsFragment",fragment);

    }


}
