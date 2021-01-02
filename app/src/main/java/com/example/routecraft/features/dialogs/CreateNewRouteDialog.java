package com.example.routecraft.features.dialogs;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.routecraft.R;
import com.example.routecraft.databinding.DialogCreateNewRouteBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateNewRouteDialog extends MaterialAlertDialogBuilder {

    private InputMethodManager imm;

    public interface Listener {
        void onCreateRoute(String routeName, boolean cancel);
    }

    public CreateNewRouteDialog(@NonNull Context context, LayoutInflater inflater) {
        super(context);

        DialogCreateNewRouteBinding binding = DialogCreateNewRouteBinding.inflate(inflater);
        Listener listener = (Listener) context;

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM", Locale.UK);

        binding.routeNameEt.setText(df.format(currentTime));
        binding.routeNameEt.setSelectAllOnFocus(true);
        binding.routeNameEt.requestFocus();

        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        binding.okBtn.setOnClickListener(view -> {
            if(!binding.routeNameEt.getText().toString().trim().isEmpty()){
                listener.onCreateRoute(binding.routeNameEt.getText().toString(), false);
                imm.hideSoftInputFromWindow(binding.routeNameEt.getWindowToken(), 0);
            }
        });
        binding.cancelBtn.setOnClickListener(view -> {
            imm.hideSoftInputFromWindow(binding.routeNameEt.getWindowToken(), 0);
            listener.onCreateRoute("", true);
        });

        this.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.dialog_bg, null));
        this.setView(binding.getRoot());

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            imm.showSoftInput(binding.routeNameEt, 0);
        }, 150);
    }
}
