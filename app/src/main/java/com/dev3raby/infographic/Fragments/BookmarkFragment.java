package com.dev3raby.infographic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev3raby.infographic.R;
import com.dev3raby.infographic.RecyclerViewAdapters.BookMarkRecyclerViewAdapter;
import com.dev3raby.infographic.RecyclerViewModels.MainDataModel;

import java.util.ArrayList;

/**
 * Created by Ahmed Yehya on 11/06/2017.
 */

public class BookmarkFragment extends Fragment {
    public static final String ARG_EXAMPLE = "this is a constant";
    private String example_data;

    private static RecyclerView mainRecyclerView;
    public static final String[] SOURCE_NAME= {"Omar Yehya", "أحمد العربي", "seha.com", "Ali.Kindy", "arinfographic","arinfographic"};
    public static final String[] INFOGRAPHIC_NAME= {"مراحل عملية التسويق", "التخزين السحابي", "كيف يمكن تحفيز الناس", "8 مؤشرات مرتبطة بمنفذي العمليات الانتحارية", "احصائيات العاب الفيديو حول العالم", "القادة والمدراء التربويين"};
    public static final String[] INFOGRAPHIC_IMAGE = {"https://shamssona.files.wordpress.com/2015/08/img_20150814_162450.jpg", "http://everyleader.net/sites/default/files/Infographic-every-leader-confrontations.jpg.png", "http://www.alarabyanews.com/upload/photo/gallery/9/7/960.jpg", "https://www.graphic-academy.com/wp-content/uploads/2016/05/image-11.jpeg", "https://s-media-cache-ak0.pinimg.com/736x/71/63/64/71636447376825c06dba6426110a2d91.jpg","https://mir-s3-cdn-cf.behance.net/project_modules/disp/741fe716989131.562b415c27a27.jpg"};
    public static final String[] SOURSE_ICON = {"https://www.iconfinder.com/data/icons/logotypes/32/twitter-128.png", "https://vignette2.wikia.nocookie.net/drunken-peasants-podcast/images/e/e0/Tumblr_icon.png", "http://beadage.net/wp-content/themes/beadage/images/icon-url.png", "aboveblue.gr/wp-content/uploads/2017/03/instagram_yeni_logo_app_2-300x300.png", "http://market.designmodo.com/wp-content/uploads/2015/06/behance-logo.png","http://diylogodesigns.com/blog/wp-content/uploads/2016/05/Instagram-logo-png-icon.png"};


    public BookmarkFragment() {

    }

    public static BookmarkFragment newInstance (String example_args)
    {
        BookmarkFragment homeFragment = new BookmarkFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_bookmarks,container,false);
        mainRecyclerView = (RecyclerView)
                rootView.findViewById(R.id.main_recycler_view);

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayoutManager testLayoutManager = new LinearLayoutManager(getContext());
        mainRecyclerView
                .setLayoutManager(testLayoutManager);
        populatRecyclerView();


        super.onViewCreated(view, savedInstanceState);
    }

    private void populatRecyclerView() {
        ArrayList<MainDataModel> arrayList = new ArrayList<>();
        for (int i = 0; i < SOURCE_NAME.length; i++) {
            arrayList.add(new MainDataModel(INFOGRAPHIC_NAME[i], SOURCE_NAME[i], SOURSE_ICON[i], INFOGRAPHIC_IMAGE[i]));
        }
        BookMarkRecyclerViewAdapter adapter = new BookMarkRecyclerViewAdapter(getActivity(), arrayList);
        mainRecyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();
    }
}
