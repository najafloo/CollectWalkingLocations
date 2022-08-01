package com.shahpar.locationrecorder.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;

import com.shahpar.locationrecorder.R;

public class CustomMessageBox extends DialogFragment {
    AppCompatButton btn_custom_cancel;
    AppCompatButton btn_custom_confirm;
    AppCompatTextView txt_messagebox_text;

    // Parameter
    MessageBoxListener listener;
    MessageDialogType messageDialogType;
    Context context;

    String messageText;
    String confirmText;
    String cancelText;

    public enum MessageDialogType {
        MDP_YES_OR_NO,
        MDP_WARRNING
    }

    public interface MessageBoxListener {
        void onConfirm();

        void onCancel();
    }

    public CustomMessageBox(Builder builder) {
        this.messageText = builder.message;
        this.confirmText = builder.confirmText;
        this.cancelText = builder.cancelText;
        this.messageDialogType = builder.messageDialogType;
        this.listener = builder.listener;
        this.context = builder.context;
    }

    public void show() {
        setCancelable(false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MessageDialogTheme);
        show(((AppCompatActivity) context).getSupportFragmentManager(), "");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.message_box_custom, null);
        dialog.setContentView(view);

        btn_custom_cancel = view.findViewById(R.id.btn_custom_cancel);
        btn_custom_confirm = view.findViewById(R.id.btn_custom_confirm);
        txt_messagebox_text = view.findViewById(R.id.txt_messagebox_text);

        txt_messagebox_text.setText(messageText);
        btn_custom_confirm.setText(confirmText);
        btn_custom_cancel.setText(cancelText);

        if (cancelText == null || cancelText.isEmpty())
            btn_custom_cancel.setVisibility(View.GONE);

        if (messageDialogType == MessageDialogType.MDP_WARRNING)
            txt_messagebox_text.setTextColor(getContext().getResources().getColor(R.color.NeccessaryColor));

        btn_custom_confirm.setOnClickListener(view1 -> {
            Log.d("SANDBADCELL", "dismiss dialog on confirm");
            dialog.dismiss();
            if (listener != null)
                listener.onConfirm();
        });

        btn_custom_cancel.setOnClickListener(view2 -> {
            Log.d("SANDBADCELL","dismiss dialog cancel");
            dialog.dismiss();
            if (listener != null)
                listener.onCancel();
        });

        return dialog;
    }

    public static class Builder {
        private String message;
        private String confirmText;
        private String cancelText;
        private MessageDialogType messageDialogType;
        MessageBoxListener listener;
        Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setListener(MessageBoxListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder messageDialogType(MessageDialogType messageDialogType) {
            this.messageDialogType = messageDialogType;
            return this;
        }

        public Builder confirmText(String confirmText) {
            this.confirmText = confirmText;
            return this;
        }

        public Builder cancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public CustomMessageBox build() {
            CustomMessageBox messageBox = new CustomMessageBox(this);
            return messageBox;
        }
    }
}
