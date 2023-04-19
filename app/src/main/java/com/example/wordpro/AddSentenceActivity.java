package com.example.wordpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wordpro.Adapter.WordSelectRecyclerViewAdapter;
import com.example.wordpro.database.AppDatabase;
import com.example.wordpro.database.Sentence;
import com.example.wordpro.tool.Callback;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AddSentenceActivity extends AppCompatActivity {

    private String TAG = "AddSentenceActivity";

    ViewPager2 viewPager;
    AddSentenceRecyclerViewAdapter adapter;
    SpringDotsIndicator indicator;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);

        db = AppDatabase.getDBInstance(AddSentenceActivity.this);

        activityViewInitializer();
    }

    public void activityViewInitializer(){

        viewPager = findViewById(R.id.addSentenceViewPager);
        indicator = findViewById(R.id.addSentenceDotsIndicator);

        adapter = new AddSentenceRecyclerViewAdapter(this, viewPager, new Callback() {
            @Override
            public void OnCallback(Object object) {
                Sentence sentence = (Sentence) object;
                Log.e(TAG, "sentence: " + sentence.toString());
                db.sentenceDao().insertSentences(sentence);
                finish();
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected -> ");
                Log.e(TAG, "    position: " + String.valueOf(position));

                switch(position){
                    case 0:
                        viewPager.setUserInputEnabled(false);
                        adapter.getEditText1().requestFocus();
                        break;
                    case 1:
                        adapter.notifyDataSetChanged();
                        viewPager.setUserInputEnabled(true);

                        break;
                    case 2:
                        adapter.getEditText2().requestFocus();
                        break;
                    case 3:
                        adapter.getEditText4().requestFocus();
                        break;
                }
                super.onPageSelected(position);
            }
        });

        viewPager.setAdapter(adapter);
        indicator.attachTo(viewPager);
    }

    public class AddSentenceRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        ViewPager2 viewPager;

        String sentence;
        String word;
        String wordIndex;
        String cheatSheet;
        String path;

        Callback callback;

        EditText editText1;
        EditText editText2;
        EditText editText4;

        Context context;

        public AddSentenceRecyclerViewAdapter(Context context, ViewPager2 viewPager, Callback callback) {
            this.context = context;
            this.viewPager = viewPager;
            this.callback = callback;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch(viewType){
                case 0:
                    View itemView0 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_sentence_0, parent, false);
                    return new MyViewHolder0(itemView0);
                case 1:
                    View itemView1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_sentence_1, parent, false);
                    return new MyViewHolder1(itemView1);
                case 2:
                    View itemView2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_sentence_2, parent, false);
                    return new MyViewHolder2(itemView2);
                case 3:
                    View itemView3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_add_sentence_3, parent, false);
                    return new MyViewHolder3(itemView3);
                default:
                    break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            switch(holder.getAdapterPosition()){
                case 0:
                    MyViewHolder0 myViewHolder0 = (MyViewHolder0) holder;
                    editText1 = myViewHolder0.editText;

                    myViewHolder0.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sentence = myViewHolder0.editText.getText().toString();
                            viewPager.setCurrentItem(holder.getAdapterPosition() + 1);
                        }
                    });
                    break;
                case 1:
                    MyViewHolder1 myViewHolder1 = (MyViewHolder1) holder;

                    WordRecyclerView adapter = new WordRecyclerView(context, sentence);
                    myViewHolder1.recyclerView.setAdapter(adapter);
                    myViewHolder1.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            word = "";
                            wordIndex = "";
                            List<WordRecyclerView.Word> wordList = adapter.getSelected();
                            for(WordRecyclerView.Word wordElem : wordList){
                                word += wordElem.getWord() + "/";
                                wordIndex += wordElem.getStartIndex() + "," + wordElem.getEndIndex() + "/";
                            }
                            viewPager.setCurrentItem(holder.getAdapterPosition() + 1);
                        }
                    });
                    break;
                case 2:
                    MyViewHolder2 myViewHolder2 = (MyViewHolder2) holder;
                    editText2 = myViewHolder2.editText;

                    myViewHolder2.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cheatSheet = editText2.getText().toString();
                            viewPager.setCurrentItem(holder.getAdapterPosition() + 1);
                        }
                    });

                    break;
                case 3:
                    MyViewHolder3 myViewHolder3 = (MyViewHolder3) holder;
                    editText4 = myViewHolder3.editText;

                    myViewHolder3.button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // send sentence class instance

                            LocalDate today = LocalDate.now();
                            String today2string = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                            LocalDate next = today.plusDays(1);
                            String next2string = next.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

                            Sentence sentence = new Sentence();
                            sentence.path = myViewHolder3.editText.getText().toString();
                            sentence.sentence = getSentence();
                            sentence.word = getWord();
                            sentence.word_index = getWordIndex();
                            sentence.cheat_sheet = getCheatSheet();
                            sentence.history = "";
                            sentence.register_day = today2string;
                            sentence.next = next2string;
                            sentence.index = 0;

                            callback.OnCallback(sentence);
                        }
                    });
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
        public String getSentence() {
            return sentence;
        }

        public String getWord() {
            return word;
        }

        public String getWordIndex() {
            return wordIndex;
        }

        public String getCheatSheet() {
            return cheatSheet;
        }

        public String getPath() {
            return path;
        }

        public EditText getEditText1() {
            return editText1;
        }

        public EditText getEditText2() {
            return editText2;
        }

        public EditText getEditText4() {
            return editText4;
        }

        public class MyViewHolder0 extends RecyclerView.ViewHolder {
            EditText editText;
            TextView button;
            public MyViewHolder0(@NonNull View itemView) {
                super(itemView);
                editText = itemView.findViewById(R.id.pageAddSentence0EditText);
                button = itemView.findViewById(R.id.pageAddSentence0Button);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getHint());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setHint(spannableStringBuilder);

            }
        }
        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            TextView textView;
            RecyclerView recyclerView;
            TextView button;
            public MyViewHolder1(@NonNull View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.pageAddSentence1BannerTextView);
                recyclerView = itemView.findViewById(R.id.pageAddSentence1RecyclerView);
                button = itemView.findViewById(R.id.pageAddSentence1Button);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 12, 17, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableStringBuilder);

                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
                layoutManager.setFlexDirection(FlexDirection.ROW);
                layoutManager.setJustifyContent(JustifyContent.FLEX_START);

                recyclerView.setLayoutManager(layoutManager);
            }
        }
        public class MyViewHolder2 extends RecyclerView.ViewHolder {

            TextView textView;
            EditText editText;
            TextView button;

            public MyViewHolder2(@NonNull View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.pageAddSentence2BannerTextView);
                editText = itemView.findViewById(R.id.pageAddSentence2EditText);
                button = itemView.findViewById(R.id.pageAddSentence2Button);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(textView.getText());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 10, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableStringBuilder);

            }
        }
        public class MyViewHolder3 extends RecyclerView.ViewHolder {

            EditText editText;
            TextView button;
            public MyViewHolder3(@NonNull View itemView) {
                super(itemView);

                editText = itemView.findViewById(R.id.pageAddSentence3EditText);
                button = itemView.findViewById(R.id.pageAddSentence3Button);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(editText.getHint());
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6868")), 7, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                editText.setHint(spannableStringBuilder);

            }
        }

    }

    public class WordRecyclerView extends RecyclerView.Adapter<WordRecyclerView.WordViewHolder>{

        Context context;
        List<Word> dataList;
        List<Word> selected;

        public int colorRed = Color.parseColor("#FF6868");
        public int colorChar = Color.parseColor("#D8EBE4");

        public WordRecyclerView(Context context, String sentence) {
            this.context = context;
            dataList = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(sentence, " ");
            int index = 0;
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                int startIndex = sentence.indexOf(token, index);
                int endIndex = startIndex + token.length() - 1;
                index = endIndex + 1;
                Word tempWord = new Word(token, startIndex, endIndex);
                dataList.add(tempWord);
            }

            this.selected = new ArrayList<>();
        }

        @NonNull
        @Override
        public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_sentence_word, parent, false);
            return new WordViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
            Word currentWord = dataList.get(holder.getAdapterPosition());
            TextView currentTextView = holder.textView;
            CardView currentCardView = holder.cardView;
            currentTextView.setText(currentWord.word);
            currentCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTextView.getCurrentTextColor() == colorRed) {
                        currentTextView.setTextColor(colorChar);
                        currentCardView.setCardBackgroundColor(colorChar);
                        selected.remove(currentWord);

                    } else {
                        currentTextView.setTextColor(colorRed);
                        currentCardView.setCardBackgroundColor(colorRed);
                        selected.add(currentWord);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class WordViewHolder extends RecyclerView.ViewHolder{
            CardView cardView;
            TextView textView;
            public WordViewHolder(@NonNull View itemView) {
                super(itemView);
                cardView = itemView.findViewById(R.id.itemAddSentenceWordCardView);
                textView = itemView.findViewById(R.id.itemAddSentenceWordTextView);
            }
        }

        public List<Word> getSelected() {
            return selected;
        }

        public class Word {
            private String word;
            private int startIndex;
            private int endIndex;
            public Word(String word, int startIndex, int endIndex){
                this.word = word;
                this.startIndex = startIndex;
                this.endIndex = endIndex;
            }

            public String getWord() {
                return word;
            }

            public int getStartIndex() {
                return startIndex;
            }

            public int getEndIndex() {
                return endIndex;
            }

            @Override
            public boolean equals(@Nullable Object obj) {
                Word compare = (Word) obj;
                if(this.word.equals(compare.word) && this.startIndex==compare.startIndex && this.endIndex==compare.endIndex){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }
}