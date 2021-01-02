package com.example.routecraft.features.dialogs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.routecraft.R;
import com.example.routecraft.databinding.DialogRenameRouteBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RenameRouteDialog extends MaterialAlertDialogBuilder {

    private InputMethodManager imm;

    public interface Listener {
        void onRenameRoute(String routeName, boolean cancel);
    }

    public RenameRouteDialog(@NonNull Context context, LayoutInflater inflater, String routeName) {
        super(context);

        DialogRenameRouteBinding binding = DialogRenameRouteBinding.inflate(inflater);
        Listener listener = (Listener) context;

        binding.routeNameEt.setText(routeName);
        binding.routeNameEt.setSelectAllOnFocus(true);
        binding.routeNameEt.requestFocus();

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        binding.okBtn.setOnClickListener(view -> {
            if(!binding.routeNameEt.getText().toString().trim().isEmpty()){
                listener.onRenameRoute(binding.routeNameEt.getText().toString(), false);
                imm.hideSoftInputFromWindow(binding.routeNameEt.getWindowToken(), 0);
            }
        });
        binding.cancelBtn.setOnClickListener(view -> {
            imm.hideSoftInputFromWindow(binding.routeNameEt.getWindowToken(), 0);
            listener.onRenameRoute("", true);
        });

        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.dialog_bg, null));
        this.setView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            imm.showSoftInput(binding.routeNameEt, 0);
        }, 150);
    }
}
