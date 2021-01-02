package com.example.routecraft.features.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.routecraft.R;
import com.example.routecraft.databinding.DialogGenericLoadingBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GenericLoadingDialog extends MaterialAlertDialogBuilder {

    public GenericLoadingDialog(@NonNull Context context, LayoutInflater inflater, String message) {
        super(context);
        DialogGenericLoadingBinding binding = DialogGenericLoadingBinding.inflate(inflater);
        binding.loadingTv.setText(message);
        this.setCancelable(false);
        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.dialog_bg, null));
        this.setView(binding.getRoot());
    }
}
