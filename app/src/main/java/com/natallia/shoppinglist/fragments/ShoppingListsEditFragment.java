package com.natallia.shoppinglist.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;

import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.SendSMS;
import com.natallia.shoppinglist.adapters.ShoppingListItemRecyclerAdapter;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;
import com.natallia.shoppinglist.database.ShoppingListItem;
import com.natallia.shoppinglist.helper.ItemTouchHelperAdapter;
import com.natallia.shoppinglist.helper.OnStartDragListener;
import com.natallia.shoppinglist.helper.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.Sort;


public class ShoppingListsEditFragment extends Fragment implements OnStartDragListener, TextView.OnEditorActionListener {
    static final int RESULT_SPEECH_TO_TEXT = 2;

    private int mShoppingListId;
    private ShoppingList mShoppingList;
    public SendSMS sendSMS;
    private Activity mActivity;


    private ActivityListener mActivityListener;
    private View mItemLayout;
    private TextView mTexView;
    private RecyclerView mRecyclerView;
  // private Button mButton_add;
    private ShoppingListItemRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        // прописываем layout фрагмента
        View view = inflater.inflate(R.layout.shopping_list_edit_fragment, container, false);

        if (mShoppingListId == 0){
            mShoppingListId = savedInstanceState.getInt("ShoppingListId",0);
        }
       mShoppingList = DataManager.getShoppingListById(mShoppingListId);

        mTexView = (TextView) view.findViewById(R.id.ed_shopping_list_name);
        mTexView.setText(mShoppingList.getName());
        mTexView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShoppingListRenameDialog myDialogFragment = new ShoppingListRenameDialog();
                myDialogFragment.mOldName = mTexView.getText().toString();
                myDialogFragment.show(getActivity().getFragmentManager(), "ShoppingListRenameDialog");
                myDialogFragment.mListener = new ShoppingListRenameDialog.ShoppingListRenameDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog, String text) {
                        if (!text.equals("")) {
                            mTexView.setText(text);
                            DataManager.setNameShoppingList(mShoppingList, mTexView.getText().toString());
                        }
                    }

                    @Override
                    public void onDialogNegativeClick(DialogFragment dialog) {

                    }
                };
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_edit_items); //отображает все шопинг листы
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//TODO
        List<ShoppingListItem> values = mShoppingList.getItems().where().findAllSorted("position", Sort.DESCENDING);
        mAdapter = new ShoppingListItemRecyclerAdapter(values,true,this, mItemLayout, null, 0, getContext(),getActivity());
        mAdapter.setOnEditorActionListener(this);
        mRecyclerView.setItemAnimator(null); // // TODO: убрана анимация item
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback((ItemTouchHelperAdapter) mAdapter);

        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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
       // if(mActivityListener!=null) {mActivityListener.setTitle("Hello");
        //}
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
        Log.d("on destroy", "on destroy");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
        MenuItem iconSendSMS = menu.findItem(R.id.send_sms);
        iconSendSMS.setVisible(true);
        MenuItem iconMic = menu.findItem(R.id.voice_add);
        iconMic.setVisible(true);
    }//TODO разобраться с меню

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                DataManager.createShoppingListItem(mShoppingList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(0);
                break;
            case R.id.delete:
                DataManager.deleteShoppingList(DataManager.getShoppingListById(mShoppingListId));
                mAdapter.notifyDataSetChanged();
                getActivity().onBackPressed();
                // TODO вызвать диалог с подтверждением удаления
                break;
            case R.id.send_sms:

                RealmList<ShoppingListItem> items = DataManager.getShoppingListById(mShoppingListId).getItems();
                String txtsms = "";
                int i = 0;
                while (i < items.size()) {
                    String  itemName = items.get(i).getItem().getName() == null? "": items.get(i).getItem().getName().toString();
                    txtsms+= itemName + ", ";
                    i++;
                }
                sendSMS.sendSMS(txtsms);
                Log.d("editFragment", "send sms");

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
        outState.putInt("ShoppingListId",mShoppingListId);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            DataManager.createShoppingListItem(mShoppingList);
            mAdapter.notifyDataSetChanged();
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
            mRecyclerView.smoothScrollToPosition(0);
        }
    }


}
