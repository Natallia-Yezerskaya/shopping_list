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
import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.UI.SendSMS;
import com.natallia.shoppinglist.adapters.ShoppingListRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;

import java.util.List;

import io.realm.Realm;


public class ShoppingListsFragment extends Fragment  {

    private ActivityListener mActivityListener;
    public OnShoppingListEdit onShoppingListEdit;
   // public SendSMS sendSMS;
    private RecyclerView mRecyclerView;
    public ShoppingListRecyclerAdapter mAdapter;

    public static ShoppingListsFragment getInstance (String aaa,Intent intent){
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        //intent.putExtra(KEA_AAA, aaa);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // прописываем layout фрагмента
        View view = inflater.inflate(R.layout.shopping_list_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_shopping_list); //отображает все шопинг листы
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
       // mRecyclerView.getAdapter().
        List<ShoppingList> values = DataManager.getShoppingLists();
        mAdapter = new ShoppingListRecyclerAdapter(getContext(), values);
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
        if (context instanceof ActivityListener){
            mActivityListener = (ActivityListener)context;
        }
        if (context instanceof OnShoppingListEdit) {
            onShoppingListEdit = (OnShoppingListEdit) context;
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
            case R.id.my_action:
                DataManager.createShoppingList();
                mAdapter.notifyDataSetChanged();

                Log.d("ShoppingList", "onOptionsItemSelected"); //TODO проверить работу кнопок в каждом фрагменте
                break;
            case R.id.my_action1:

               // ShoppingListsEditFragment fragment  = ShoppingListsEditFragment.getInstance("djkgfhsg",getActivity().getIntent());
               // mActivityListener.changeFragment(fragment);

                //Log.d("ShoppingList", "onOptionsItemSelected"); //TODO проверить работу кнопок в каждом фрагменте
              break;
            case R.id.send_sms:

                Log.d("ShoppingList", "send sms");
                break;

        }
        return super.onOptionsItemSelected(item);


    }



    public void setTitle(String title) {// TODO можно вставить название списка

    }


}
