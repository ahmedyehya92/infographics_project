package com.dev3raby.infographic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewAdapters.CategoryRecyclerViewAdapter;
import com.dev3raby.infographic.RecyclerViewModels.CategoryDataModel;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 11/06/2017.
 */

public class ExploreFragment extends Fragment {
    public static final String ARG_EXAMPLE = "this is a constant";
    private String example_data;

    private static RecyclerView catgRecyclerView;
    public static final String[] CATEGORY_NAME= {"أحداث", "رياضة", "صحة", "نمط حياة"};
    public static final String[] CATEGORY_IMAGE = {"https://pbs.twimg.com/profile_images/694449140269518848/57ZmXva0.jpg", "http://richardhusovsky.com/wp-content/uploads/2015/06/ranni-beh.jpg", "http://www.fitnesstrend.com/sites/default/files/Stetoscopio%20WEB_1.jpg", "https://blog.holidaylettings.co.uk/wp-content/uploads/2015/04/lifestyle.jpg"};
    CategoryRecyclerViewAdapter catgAdapter;


    public ExploreFragment() {

    }

    public static ExploreFragment newInstance (String example_args)
    {
        ExploreFragment homeFragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EXAMPLE, example_args);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        example_data = getArguments().getString(ARG_EXAMPLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_explore,container,false);
        catgRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.catg_recycler_view);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        StaggeredGridLayoutManager catgLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        catgRecyclerView
                .setLayoutManager(catgLayoutManager);
        populatRecyclerView();
        super.onViewCreated(view, savedInstanceState);
    }

    private void populatRecyclerView() {
        // for category recyclerView
        ArrayList<CategoryDataModel> catgArrayList = new ArrayList<>();
        for (int i = 0; i < CATEGORY_NAME.length; i++) {
            catgArrayList.add(new CategoryDataModel(CATEGORY_NAME[i], CATEGORY_IMAGE[i]));
        }
        catgAdapter = new CategoryRecyclerViewAdapter(getActivity(), catgArrayList);
        catgRecyclerView.setAdapter(catgAdapter);// set adapter on recyclerview
        catgAdapter.notifyDataSetChanged();// Notify the adapter
    }
}
