package com.example.routecraft.features.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.routecraft.R;

public class RenameRouteDialog extends DialogFragment {

    private Listener listener;

    private InputMethodManager imm;

    public interface Listener {
        void onRenameRoute(String routeName);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rename_route, null);

        String routeName = "";

        if (getArguments() != null) {
            routeName = getArguments().getString("ROUTE_NAME");
        }

        EditText routeNameEt = view.findViewById(R.id.route_name_et);
        Button okBtn = view.findViewById(R.id.ok_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        routeNameEt.setText(routeName);
        routeNameEt.setSelectAllOnFocus(true);
        routeNameEt.requestFocus();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        okBtn.setOnClickListener(view1 -> {
            if(!routeNameEt.getText().toString().trim().isEmpty()){
                listener.onRenameRoute(routeNameEt.getText().toString());
                imm.hideSoftInputFromWindow(routeNameEt.getWindowToken(), 0);
                dismiss();
            }
        });
        cancelBtn.setOnClickListener(view12 -> {
            imm.hideSoftInputFromWindow(routeNameEt.getWindowToken(), 0);
            dismiss();
        });

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        builder.setView(view);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement Listener");
        }
    }
}
