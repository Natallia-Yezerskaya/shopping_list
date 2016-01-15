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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.UI.ShoppingListItemAdapterCallback;
import com.natallia.shoppinglist.adapters.ShoppingListRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import java.util.List;

/**
 * Главный фрагмент - отображает все имеющиеся списки покупок. Отсюда происходит переход в режим
 * редактирования отдельного списка.
 */
public class ShoppingListsFragment extends Fragment implements ShoppingListItemAdapterCallback {

    public OnShoppingListEdit onShoppingListEdit;
    private ActivityListener mActivityListener;
    private RecyclerView mRecyclerView;
    private ShoppingListRecyclerAdapter mAdapter;

    public static ShoppingListsFragment getInstance (Intent intent){
        ShoppingListsFragment fragment = new ShoppingListsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_shopping_list); //отображает все шопинг листы
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<ShoppingList> values = DataManager.getShoppingLists();
        mAdapter = new ShoppingListRecyclerAdapter(this, getContext(), values);
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

// Заполняем верхнее меню
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        MenuItem iconSort = menu.findItem(R.id.sort);
        iconSort.setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
    Обработка нажатий кнопок меню
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            // Добавление нового списка
            case R.id.add:
                ShoppingList shoppingList = DataManager.createShoppingList();
                mAdapter.notifyDataSetChanged();
                DataManager.createShoppingListItem(shoppingList);
                onShoppingListEdit.onShoppingListEdit(shoppingList.getId());
            break;
            // Сортировка всех списков по заполнености и в хронологическом порядке
            case R.id.sort:
                DataManager.sortShoppingLists();
                mAdapter.notifyDataSetChanged();
              break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Обновляем UI списка покупок - сколько вычеркнуто и стили заголовка
    @Override
    public void onItemChecked(int position) {

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int childIndex = position - layoutManager.findFirstVisibleItemPosition();
        TextView tvTotal = (TextView)mRecyclerView.getChildAt(childIndex).findViewById(R.id.total_checked);
        TextView tvName = (TextView)mRecyclerView.getChildAt(childIndex).findViewById(R.id.tv_shopping_list_name);
        ShoppingListRecyclerAdapter adapter = (ShoppingListRecyclerAdapter) mRecyclerView.getAdapter();
        LinearLayout layout = (LinearLayout) mRecyclerView.getChildAt(childIndex).findViewById(R.id.linear_layout_shopping_list);
        adapter.RefreshTotalChecked(tvTotal,tvName,layout, position);
    }
}
