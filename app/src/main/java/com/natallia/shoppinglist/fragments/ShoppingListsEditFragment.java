package com.natallia.shoppinglist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.natallia.shoppinglist.MainActivity;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.SendSMS;
import com.natallia.shoppinglist.UI.ShoppingListItemAdapterCallback;
import com.natallia.shoppinglist.adapters.ShoppingListItemRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.Item;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;
import com.natallia.shoppinglist.helper.ItemTouchHelperAdapter;
import com.natallia.shoppinglist.helper.OnStartDragListener;
import com.natallia.shoppinglist.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.Sort;

/**
 * Фрагмент редкатирования списка покупок - позволяет добвалять/изменять и удалять элементы списка.
 * Также здесь доступны возможности по отправки смс и вставки элементов из избранных списков
 */
public class ShoppingListsEditFragment extends Fragment
        implements OnStartDragListener, TextView.OnEditorActionListener,
        ShoppingListItemAdapterCallback, SelectFavoritesDialog.SelectFavoritesDialogListener {

    static final int RESULT_SPEECH_TO_TEXT = 2;
    private int mShoppingListId;
    private ShoppingList mShoppingList;
    public SendSMS sendSMS;
    private ActivityListener mActivityListener;
    private View mItemLayout;
    private LinearLayout mLayoutShoppingList;
    private TextView mTextViewName;
    private TextView mTotalChecked;
    private RecyclerView mRecyclerView;
    private ImageButton mButton_favorite;
    private ShoppingListItemRecyclerAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    public static ShoppingListsEditFragment getInstance (Intent intent){
        ShoppingListsEditFragment fragment = new ShoppingListsEditFragment();
        fragment.mShoppingListId = intent.getIntExtra("ShoppingListId",0);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_list_edit_fragment, container, false);
        if (savedInstanceState != null){
            mShoppingListId = savedInstanceState.getInt("ShoppingListId",0);
            sendSMS = (MainActivity) getActivity();
        }
        // получаем Shopping list по полученному id
        mShoppingList = DataManager.getShoppingListById(mShoppingListId);
        mTotalChecked = (TextView ) view.findViewById(R.id.total_checked);
        mLayoutShoppingList = (LinearLayout)view.findViewById(R.id.layout_shopping_list);
        refreshTotalChecked();
        mTextViewName = (TextView) view.findViewById(R.id.ed_shopping_list_name);
        mTextViewName.setText(mShoppingList.getName());
        mTextViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShoppingListRenameDialog myDialogFragment = new ShoppingListRenameDialog();
                myDialogFragment.mOldName = mTextViewName.getText().toString();
                myDialogFragment.show(getActivity().getFragmentManager(), "ShoppingListRenameDialog");
                //обработка нажатия клавиши "Сохранить" в диалоге
                myDialogFragment.mListener = new ShoppingListRenameDialog.ShoppingListRenameDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog, String text) {
                        if (!text.equals("")) {
                            mTextViewName.setText(text);
                            DataManager.setNameShoppingList(mShoppingList, mTextViewName.getText().toString());
                        }
                    }
                };
            }
        });
        mButton_favorite = (ImageButton) view.findViewById(R.id.favorite);
        // при нажатии на кнопку Favorite меняем признак favorite у Shopping List
        mButton_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.toggleFavorite(mShoppingList);
                mAdapter.notifyDataSetChanged();
                toggleFavorite();
            }
        });

        toggleFavorite();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_edit_items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<ShoppingListItem> values = mShoppingList.getItems().where().findAllSorted("position", Sort.DESCENDING);
        mAdapter = new ShoppingListItemRecyclerAdapter(values,true,this, mItemLayout, this, 0, getContext(),getActivity());
        mAdapter.setOnEditorActionListener(this);
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }

    private void refreshTotalChecked(){
        String text = " " +DataManager.shoppingListGetChecked(mShoppingList) + "/" + mShoppingList.getItems().size();
        mTotalChecked.setText(text);
        if (DataManager.shoppingListIsChecked(mShoppingList)){
            DataManager.setShoppingListIsChecked(mShoppingList, true);
            mLayoutShoppingList.getBackground().setColorFilter(getContext().getResources().getColor(R.color.itemListBackgroundChecked), PorterDuff.Mode.MULTIPLY);
        }
        else {
            DataManager.setShoppingListIsChecked(mShoppingList, false);
            mLayoutShoppingList.getBackground().setColorFilter(getContext().getResources().getColor(R.color.itemListBackground), PorterDuff.Mode.MULTIPLY);
        }
    }
    private void toggleFavorite(){
        if (mShoppingList.isFavorite()){
            mButton_favorite.setBackgroundResource(R.drawable.ic_action_favorite);
        }
        else{
            mButton_favorite.setBackgroundResource(R.drawable.ic_action_favorite_out);
        }
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

// устанавливаем видимость кнопок меню
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        MenuItem iconFavorite = menu.findItem(R.id.favorite);
        iconFavorite.setVisible(true);
        MenuItem iconSendSMS = menu.findItem(R.id.send_sms);
        iconSendSMS.setVisible(true);
        MenuItem iconMic = menu.findItem(R.id.voice_add);
        iconMic.setVisible(true);
        MenuItem iconDelete = menu.findItem(R.id.delete);
        iconDelete.setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.favorite:
                Bundle args = new Bundle();
                args.putInt("id", mShoppingListId);

                final SelectFavoritesDialog myDialogFragment = new SelectFavoritesDialog();
                myDialogFragment.setArguments(args);
                myDialogFragment.show(getActivity().getFragmentManager(), "SelectFavoritesDialog");
                myDialogFragment.mListener = new SelectFavoritesDialog.SelectFavoritesDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog) {
                        if (myDialogFragment.mSelectedItems.size() == 0) {
                            Log.d("No elements", "No elements");
                        } else {
                            for (int i = 0; i < myDialogFragment.mSelectedItems.size(); i++) {
                                RealmList<ShoppingListItem> items = DataManager.getShoppingListById((int) myDialogFragment.mSelectedItems.get(i)).getItems();
                                for (ShoppingListItem item : items) {
                                    DataManager.addShoppingListItem(mShoppingList, item);
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                            refreshTotalChecked();
                        }
                    }
                };

                break;
            case R.id.add:
                DataManager.createShoppingListItem(mShoppingList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(0);
                refreshTotalChecked();
                break;
            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
                builder.setTitle(R.string.dialog_delete_title);
                builder.setCancelable(true);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataManager.deleteShoppingList(DataManager.getShoppingListById(mShoppingListId));
                        mAdapter.notifyDataSetChanged();
                        getActivity().onBackPressed();
                    }
                });
                builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.send_sms:
                List<ShoppingListItem> items = DataManager.getShoppingListById(mShoppingListId).getItems().where().findAllSorted("position", Sort.DESCENDING);
                String txtsms = getResources().getString(R.string.txt_sms);
                int i = 0;
                while (i < items.size()) {
                    Item curItem = items.get(i).getItem();
                    if (curItem.getName() == null || curItem.getName().equals("")){
                    }
                    else{
                        String itemName = curItem.getName().toString() + " "+String.valueOf(items.get(i).getAmount())+" шт.";
                        if (i == items.size()-1)  {
                            txtsms+= itemName;
                        } else {
                            txtsms+= itemName + ", ";
                        }
                    }
                    i++;
                }
                sendSMS.sendSMS(txtsms);
                break;
            case R.id.voice_add:
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak please");
                startActivityForResult(speechIntent, RESULT_SPEECH_TO_TEXT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ShoppingListId", mShoppingListId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            DataManager.createShoppingListItem(mShoppingList);
            mAdapter.notifyDataSetChanged();
            refreshTotalChecked();
            mRecyclerView.smoothScrollToPosition(0);

            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH_TO_TEXT && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ShoppingListItem item = DataManager.createShoppingListItem(mShoppingList);
            DataManager.setNameShoppingListItem(item.getItem(), matches.get(0));
            mAdapter.notifyDataSetChanged();
            refreshTotalChecked();
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onItemChecked(int position) {
        refreshTotalChecked();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        final SelectFavoritesDialog myDialog = (SelectFavoritesDialog) dialog;
        if (myDialog.mSelectedItems.size() != 0) {
            for (int i = 0; i < myDialog.mSelectedItems.size(); i++) {
                RealmList<ShoppingListItem> items = DataManager.getShoppingListById((int) myDialog.mSelectedItems.get(i)).getItems();
                for (ShoppingListItem item : items) {
                    DataManager.addShoppingListItem(mShoppingList, item);
                }
            }
            mAdapter.notifyDataSetChanged();
            refreshTotalChecked();
            myDialog.mSelectedItems.clear();
        }
    }
}
