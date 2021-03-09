package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.QuestionsBank;
import com.example.trivia.model.Question;
import com.example.trivia.util.Prefs;


import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private List<Question> questionsList;
    private Button trueButton;
    private Button falseButton;
    private ImageView prevButton;
    private ImageView nextButton;
    private TextView questionText;
    private TextView counterText;
    private TextView scoreField;
    private CardView answerCardView;
    private int currentIndex = 0;
    private int score = 0;
    @Inject
    Prefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController.getInstance().getAppComponent().inject(this);

        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        questionText = findViewById(R.id.question_field_textview);
        counterText = findViewById(R.id.counter_textview);
        answerCardView = findViewById(R.id.question_cardview);
        scoreField = findViewById(R.id.score_field_textview);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        questionsList = new QuestionsBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(List<Question> questionsArrayList) {
               updateView();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.saveHighScore(score);
        prefs.removeClickedButtons();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.prev_button:
                currentIndex = (currentIndex == 0) ? 0 : (currentIndex - 1);
                updateView();
                break;
            case  R.id.next_button:
                currentIndex = (currentIndex + 1) % questionsList.size();
                updateView();
                break;
            case  R.id.true_button:
                if (prefs.saveClickedButton(currentIndex) == 0) {
                    checkAnswer(true);
                    updateView();
                }
                break;
            case R.id.false_button:
                if (prefs.saveClickedButton(currentIndex) == 0) {
                    checkAnswer(false);
                    updateView();
                }
                break;
        }
    }

    private void fadingAnimation() {
        AlphaAnimation fading = new AlphaAnimation(1.0f, 0.0f);
        fading.setDuration(300);
        fading.setRepeatCount(1);
        fading.setRepeatMode(Animation.REVERSE);
        answerCardView.setAnimation(fading);
        fading.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                answerCardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                answerCardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shake() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        answerCardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                answerCardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                answerCardView.setCardBackgroundColor(Color.WHITE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void checkAnswer(boolean userAnswer) {
        int toastMessage;
        if (userAnswer == questionsList.get(currentIndex).isAnswerTrue()) {
            toastMessage = R.string.correct_answer;
            fadingAnimation();
        } else {
            toastMessage = R.string.wrong_answer;
            shake();
        }

        Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        scoreUpdate(toastMessage);
    }

    private void scoreUpdate(int toastMessage) {
        if (toastMessage == R.string.correct_answer) {
            score+= 10;
        } else {
            score = (score == 0) ? 0 : (score - 10);
        }
    }

    public void updateView() {
        counterText.setText(MessageFormat.format("{0} / {1}", currentIndex + 1, questionsList.size() - 1));
        questionText.setText(questionsList.get(currentIndex).getAnswer());
        scoreField.setText(MessageFormat.format("Highest Score: {0}\nCurrent Score: {1}", prefs.getHighScore(), score));
    }
}
