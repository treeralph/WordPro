package com.example.wordpro.tool;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.wordpro.R;

public class WordDialog extends Dialog {

    TextView textView;

    public WordDialog(@NonNull Context context, String mainText) {
        super(context);
        setContentView(R.layout.dialog_word);

        textView = findViewById(R.id.WordDialogTextView);
        textView.setText(mainText);
    }
}
