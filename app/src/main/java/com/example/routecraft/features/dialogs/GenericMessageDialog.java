package com.example.routecraft.features.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.routecraft.R;
import com.example.routecraft.databinding.DialogGenericMessageBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GenericMessageDialog extends MaterialAlertDialogBuilder {

    public interface Listener{
        void genericMessageDialogOkBtnClick();
    }

    public GenericMessageDialog(@NonNull Context context, LayoutInflater inflater, String message) {
        super(context);
        DialogGenericMessageBinding binding = DialogGenericMessageBinding.inflate(inflater);
        Listener listener = (Listener) context;
        binding.messageTv.setText(message);
        binding.okBtn.setOnClickListener(view -> {
            listener.genericMessageDialogOkBtnClick();
        });
        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.dialog_bg, null));
        this.setView(binding.getRoot());
    }
}
