package com.natallia.shoppinglist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.natallia.shoppinglist.R;

/**
 * Диалог переименовывания списка покупок
 */
public class ShoppingListRenameDialog extends DialogFragment {
    public String mOldName;
    public static String mNewName = "";
    public ShoppingListRenameDialogListener mListener;
    public Activity activity;
    public interface ShoppingListRenameDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog,String text);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_setname, null);
        ((EditText) view.findViewById(R.id.et_dialog)).setText(mOldName);
        builder.setView(view);
        builder.setTitle(R.string.dialog_rename_shopping_list_name)
                .setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newName =((EditText) view.findViewById(R.id.et_dialog)).getText().toString();
                        mListener.onDialogPositiveClick(ShoppingListRenameDialog.this,newName);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ShoppingListRenameDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mListener==null){
            dismiss();
        }
    }
}
