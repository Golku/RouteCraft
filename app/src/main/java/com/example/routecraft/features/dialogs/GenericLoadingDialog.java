package com.example.routecraft.features.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.routecraft.R;

public class GenericLoadingDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_generic_loading, null);

        String message = "";

        if (getArguments() != null) {
            message = getArguments().getString("DIALOG_MESSAGE_KEY");
        }

        TextView loadingTv = view.findViewById(R.id.loading_tv);
        loadingTv.setText(message+"…");

        builder.setView(view);
        return builder.create();
    }

}
