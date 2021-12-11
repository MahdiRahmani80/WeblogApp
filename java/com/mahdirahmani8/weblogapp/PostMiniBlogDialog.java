package com.mahdirahmani8.weblogapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textview.MaterialTextView;


public class PostMiniBlogDialog extends DialogFragment {

    static AlertDialog.Builder builder;
    View view;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.miniblog_dialog,null,false);
        builder.setView(view);
        builder.setCancelable(false);

        MaterialTextView tv;
        Button success = view.findViewById(R.id.btn_dialog_success);
        Button cancel = view.findViewById(R.id.btn_dialog_cancel);

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // todo : post mini blog
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PostMiniBlogDialog.this.getDialog().cancel();
            }
        });

        builder.create().show();

        return null;
    }

    public static void show (){

        builder.create().show();
    }

}
