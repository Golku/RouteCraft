package com.example.routecraft.features.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.routecraft.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateNewRouteDialog extends DialogFragment {

    private final String DEBUG_TAG = "DEBUG_TAG";

    private Listener listener;

    private InputMethodManager imm;

    public interface Listener {
        void onNewRouteName(String routeName);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM", Locale.UK);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_new_route, null);

        EditText routeNameEt = view.findViewById(R.id.route_name_et);
        Button okBtn = view.findViewById(R.id.ok_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        routeNameEt.setText(df.format(currentTime));
        routeNameEt.setSelectAllOnFocus(true);
        routeNameEt.requestFocus();

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        okBtn.setOnClickListener(view1 -> {
            if(!routeNameEt.getText().toString().trim().isEmpty()){
                listener.onNewRouteName(routeNameEt.getText().toString());
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
