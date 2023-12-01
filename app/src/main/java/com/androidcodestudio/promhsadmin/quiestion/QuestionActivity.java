package com.androidcodestudio.promhsadmin.quiestion;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidcodestudio.promhsadmin.R;
import com.androidcodestudio.promhsadmin.ScoreActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    public static final  String FILE_NAME ="QUIZZER";
    public static final  String KEY_NAME ="QUESTIONS";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private int matchedQuationPosition;

    private List<Question_Pojo> bookmarkList;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Button op_one, op_two, op_three, op_four;
    private ImageButton bookmarks_btn;
    private LinearLayout option_containor;
    private TextView question;
    private TextView numberindicator;
    private Button share_btn;
    private Button next_btn;
    private int count = 0;
    private int position = 0;
    private int Score = 0;
    private String category;
    private int setNo;

    private Window _window;

    private List<Question_Pojo> question_pojos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        _window=this.getWindow();
        _window.setStatusBarColor(this.getResources().getColor(R.color.toolbar));
        _window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //this code allays mobile screen light on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this code allays mobile screen light on

        Toolbar toolbar = findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);

        op_one = findViewById(R.id.op_one);
        op_two = findViewById(R.id.op_two);
        op_three = findViewById(R.id.op_three);
        op_four = findViewById(R.id.op_four);
        bookmarks_btn = findViewById(R.id.bookmarks_btn);
        option_containor = findViewById(R.id.options_containor);
        share_btn = findViewById(R.id.share_btn);
        next_btn = findViewById(R.id.next_btn);
        question = findViewById(R.id.question);
        numberindicator = findViewById(R.id.number_indicator);

        category = getIntent().getStringExtra("category");
        setNo = getIntent().getIntExtra("setNo", 1);

        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();

        getBookmark();

        bookmarks_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelMatch()){
                    bookmarkList.remove(matchedQuationPosition);
                    bookmarks_btn.setImageDrawable(getDrawable(R.drawable.bookmark));

                }else{
                    bookmarkList.add(question_pojos.get(position));
                    bookmarks_btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_book_24));
                }
            }
        });

        final Dialog loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        question_pojos = new ArrayList<>();
        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions").orderByChild("setNo").equalTo(setNo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    question_pojos.add(dataSnapshot.getValue(Question_Pojo.class));
                    if (question_pojos.size()>0){
                        for (int i = 0; i < 4; i++) {
                            option_containor.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    checkAnswer((Button) v);
                                }
                            });
                        }
                        playAnim(question, 0, question_pojos.get(position).getQuestion());
                        next_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                next_btn.setEnabled(false);
                                next_btn.setAlpha(0.5f);
                                EnalbleOption(true);
                                position++;
                                if (position == question_pojos.size()) {
                                    Intent score_intent = new Intent(QuestionActivity.this, ScoreActivity.class);
                                    score_intent.putExtra("score",Score);
                                    score_intent.putExtra("total",question_pojos.size());
                                    startActivity(score_intent);
                                    return;
                                }
                                count = 0;
                                playAnim(question, 0, question_pojos.get(position).getQuestion());
                            }
                        });
                    }
                    else {
                        finish();
                        Toast.makeText(QuestionActivity.this, "no  question", Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(QuestionActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                finish();
            }
        });


    }
    @Override
    protected void onPause() {
        super.onPause();
        storeBookmark();
    }

    private void playAnim(final View view, final int value, final String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (value == 0 && count < 4) {
                    String option = "";
                    if (count == 0) {
                        option = question_pojos.get(position).getOp_one();
                    } else if (count == 1) {
                        option = question_pojos.get(position).getOp_two();
                    } else if (count == 2) {
                        option = question_pojos.get(position).getOp_three();
                    } else if (count == 3) {
                        option = question_pojos.get(position).getOp_four();
                    }
                    playAnim(option_containor.getChildAt(count), 0, option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (value == 0) {
                    try {
                        ((TextView) view).setText(data);
                        numberindicator.setText(position + 1 + "/" + question_pojos.size());
                        if (modelMatch()){
                            bookmarks_btn.setImageDrawable(getDrawable(R.drawable.ic_baseline_book_24));
                        }else{
                            bookmarks_btn.setImageDrawable(getDrawable(R.drawable.bookmark));
                        }

                    } catch (ClassCastException ex) {
                        ((Button) view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view, 1, data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectedop) {
        EnalbleOption(false);
        next_btn.setEnabled(true);
        next_btn.setAlpha(1);
        if (selectedop.getText().toString().equals(question_pojos.get(position).getCorrect_ans())) {
            Score++;
            selectedop.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e8b57")));
        } else {
            selectedop.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#cd5c5c")));
            Button correctOption = (Button) option_containor.findViewWithTag(question_pojos.get(position).getCorrect_ans());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#2e8b57")));
        }
    }

    private void EnalbleOption(boolean enable) {
        for (int i = 0; i < 4; i++) {
            option_containor.getChildAt(i).setEnabled(enable);
            if (enable) {
                option_containor.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e0f2f1")));
            }
        }
    }

    private void getBookmark(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<Question_Pojo>>(){}.getType();

        bookmarkList = gson.fromJson(json,type);
        if (bookmarkList == null){
            bookmarkList = new ArrayList<>();

        }
    }

    private boolean modelMatch(){
        boolean matched = false;
        int i = 0;
        for (Question_Pojo pojo : bookmarkList){
            if (pojo.getQuestion().equals(question_pojos.get(position).getQuestion())
                    && pojo.getCorrect_ans().equals(question_pojos.get(position).getCorrect_ans())
                    && pojo.getSetNo() == question_pojos.get(position).getSetNo()){
                matched = true;
                matchedQuationPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void storeBookmark(){
        String json = gson.toJson(bookmarkList);
        editor.putString(KEY_NAME,json);
        editor.commit();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}

