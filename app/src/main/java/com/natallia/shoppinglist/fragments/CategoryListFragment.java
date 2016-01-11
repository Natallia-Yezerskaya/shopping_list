package com.natallia.shoppinglist.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.adapters.MyCategoryRecyclerAdapter;
import com.natallia.shoppinglist.database.Category;
import com.natallia.shoppinglist.database.DataManager;

import java.util.List;

import io.realm.Realm;





public class CategoryListFragment extends Fragment {


    private static String KEA_AAA = "sfafsafa";
    private static int KEY_number ;
    private int keyNumber ;
    public TextView mTextView;

    private Realm realm;

    private String aaa;

    private ActivityListener mActivityListener;


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public CategoryListFragment() {
        this.aaa = aaa;

    }

    public static CategoryListFragment getInstance (String aaa,Intent intent){
        CategoryListFragment fragment = new CategoryListFragment();
        intent.putExtra(KEA_AAA,aaa);
        return fragment;

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        aaa = getActivity().getIntent().getStringExtra(KEA_AAA);

        // if (KEY_number == 1){
        View view = inflater.inflate(R.layout.fragment_list,container,false);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        List<Category> values = DataManager.getCategories();




        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv);

        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyCategoryRecyclerAdapter(values);
        mRecyclerView.setAdapter(mAdapter);

       // ArrayAdapter<String> adapter = new RecyclerView.Adapter<String>(this, values);
      //  listview.setAdapter(adapter);
        // else if (KEY_number == 2){
        //     View view = inflater.inflate(R.layout.fragment_details,container,false);}
        // else
        //  {View view = inflater.inflate(R.layout.fragment_details2,container,false);}


        // mTextView = (TextView) .findViewById(R.id.textView);
        //    mTextView.setText(aaa);


        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivityListener){
            mActivityListener = (ActivityListener)context;
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(mActivityListener!=null) {mActivityListener.setTitle("Hello");}
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu, menu);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:

                Log.d("Category", "onOptionsItemSelected");
                break;




            //basicCRUD(realm);


        }
        return super.onOptionsItemSelected(item);


    }


}
