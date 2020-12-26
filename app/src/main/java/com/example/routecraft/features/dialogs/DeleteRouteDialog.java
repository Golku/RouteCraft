package com.example.routecraft.features.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.routecraft.R;

public class DeleteRouteDialog extends DialogFragment {

    private Listener listener;

    public interface Listener {
        void onDeleteRoute();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_route, null);

        String routeName = "";

        if (getArguments() != null) {
            routeName = getArguments().getString("ROUTE_NAME");
        }

        TextView routeNameTv = view.findViewById(R.id.route_name_tv);
        Button okBtn = view.findViewById(R.id.ok_btn);
        Button cancelBtn = view.findViewById(R.id.cancel_btn);

        routeNameTv.setText(routeName);

        okBtn.setOnClickListener(view12 -> {
            listener.onDeleteRoute();
            dismiss();
        });
        cancelBtn.setOnClickListener(view1 -> dismiss());

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
