package com.maxclub.easyweather;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class RemoveItemDialogFragment extends DialogFragment {

    public static RemoveItemDialogFragment newInstance() {
        RemoveItemDialogFragment fragment = new RemoveItemDialogFragment();

        return fragment;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove_dialog_title)
                .setMessage(R.string.remove_dialog_message)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(Activity.RESULT_CANCELED);
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setCancelable(true);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button negativeButton = (Button) dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                Button positiveButton = (Button) dialog.getButton(DialogInterface.BUTTON_POSITIVE);

                negativeButton.setTextColor(Color.GRAY);
                positiveButton.setTextColor(getResources().getColor(R.color.button_positive_text_color));

                Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                negativeButton.setTypeface(typeface);
                positiveButton.setTypeface(typeface);

                negativeButton.invalidate();
                positiveButton.invalidate();
            }
        });

        return dialog;
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
