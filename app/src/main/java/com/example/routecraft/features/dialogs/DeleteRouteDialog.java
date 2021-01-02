package com.example.routecraft.features.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.routecraft.R;
import com.example.routecraft.databinding.DialogDeleteRouteBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DeleteRouteDialog extends MaterialAlertDialogBuilder {

    public interface Listener {
        void onDeleteRoute(boolean cancel);
    }

    public DeleteRouteDialog(@NonNull Context context, LayoutInflater inflater, String routeName) {
        super(context);

        DialogDeleteRouteBinding binding = DialogDeleteRouteBinding.inflate(inflater);
        Listener listener = (Listener) context;

        binding.routeNameTv.setText(routeName);

        binding.okBtn.setOnClickListener(view -> {
            listener.onDeleteRoute(false);
        });
        binding.cancelBtn.setOnClickListener(view -> {
            listener.onDeleteRoute(true);
        });

        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.dialog_bg, null));
        this.setView(binding.getRoot());
    }
}
