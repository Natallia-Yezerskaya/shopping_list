package com.natallia.shoppinglist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.natallia.shoppinglist.MainActivity;
import com.natallia.shoppinglist.R;
import com.natallia.shoppinglist.UI.ActivityListener;
import com.natallia.shoppinglist.UI.OnShoppingListEdit;
import com.natallia.shoppinglist.database.DataManager;
import com.natallia.shoppinglist.database.ShoppingList;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Диалог для добавления элементов существующих избранных списков в текущий список покупок
 */
public class SelectFavoritesDialog extends DialogFragment {
    public ArrayList<Integer> mSelectedItems;
    private int mIdShoppingList;
    public SelectFavoritesDialogListener mListener;

    public interface SelectFavoritesDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        onAttach(getActivity());
        mIdShoppingList = getArguments().getInt("id",0);
        final List<ShoppingList> mFavoritesLists = DataManager.getFavoriteShoppingLists(mIdShoppingList);
        mSelectedItems = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        builder.setTitle(R.string.dialog_select_favorites);
        final MatrixCursor cursor = new MatrixCursor(new String[] {"_id","name","isChecked"});
        for (ShoppingList list: mFavoritesLists) {
                cursor.addRow(new Object[]{list.getId(), list.getName(), 0});
        }
        builder.setMultiChoiceItems(cursor, "isChecked","name",
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        Integer id = mFavoritesLists.get(which).getId();
                        if (isChecked) {
                            mSelectedItems.add(id);
                        } else if (mSelectedItems.contains(id)) {
                            mSelectedItems.remove(id);
                        }
                    }
                })
                .setPositiveButton(R.string.dialog_select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogPositiveClick(SelectFavoritesDialog.this);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mListener == null) {
            dismiss();
        }
    }
}