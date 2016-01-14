package com.natallia.shoppinglist.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.adapters.ShoppingListRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;

import java.util.ArrayList;
import java.util.List;


/*public class SelectFavoritesFragment extends Fragment {

    static final int PICK_CONTACT_REQUEST = 1;
    static final int RESULT_SPEECH_TO_TEXT = 2;

    private ActivityListener mActivityListener;
    public OnShoppingListEdit onShoppingListEdit;
    private RecyclerView mRecyclerView;
    private ShoppingListRecyclerAdapter mAdapter;


    public static SelectFavoritesFragment getInstance( Intent intent) {
        SelectFavoritesFragment fragment = new SelectFavoritesFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // прописываем layout фрагмента
        View view = inflater.inflate(R.layout.favorites, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_shopping_list); //отображает все шопинг листы
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<ShoppingList> values = DataManager.getShoppingLists();
        mAdapter = new ShoppingListRecyclerAdapter(null,getContext(),values);
        mAdapter.onShoppingListEdit = onShoppingListEdit;
        mRecyclerView.setAdapter(mAdapter);
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
        if (context instanceof ActivityListener) {
            mActivityListener = (ActivityListener) context;
        }
        if (context instanceof OnShoppingListEdit) {
            onShoppingListEdit = (OnShoppingListEdit) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mActivityListener != null) {
          // mActivityListener.setTitle("Hello");
        }
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

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu, menu);

        inflater.inflate(R.menu.menu, menu);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SPEECH_TO_TEXT && resultCode == Activity.RESULT_OK) {
        }

    }

}
*/

