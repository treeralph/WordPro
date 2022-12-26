package com.example.wordpro.tool;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordpro.Adapter.SelectDialogRecyclerViewAdapter;
import com.example.wordpro.R;

import java.util.List;

public class SelectCategoryDialog extends Dialog {

    public static int AddContentActivity_CategorySelect_CODE = 0;
    public static int AddContentActivity_LanguageSelect_CODE = 1;

    RecyclerView recyclerView;
    SelectDialogRecyclerViewAdapter adapter;

    public SelectCategoryDialog(@NonNull Context context, int flag, Callback callback) {
        super(context);
        setContentView(R.layout.dialog_select);

        recyclerView = findViewById(R.id.dialogSelectRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        switch (flag){
            case 0:
                String[] categories = context.getResources().getStringArray(R.array.category);
                adapter = new SelectDialogRecyclerViewAdapter(categories, callback, this);
                break;
            case 1:
                String[] languages = context.getResources().getStringArray(R.array.language);
                adapter = new SelectDialogRecyclerViewAdapter(languages, callback, this);
                break;
            default:
                break;
        }

        recyclerView.setAdapter(adapter);
    }


}
