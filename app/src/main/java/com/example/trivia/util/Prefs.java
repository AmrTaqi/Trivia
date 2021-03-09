package com.example.trivia.util;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class Prefs {
    @Inject
    SharedPreferences preferences;
    private List<String> clickedButtonsList = new ArrayList<>();

    @Inject
    public Prefs() {
    }

    public void saveHighScore(int score) {
        int highScore = this.preferences.getInt("high_score", 0);
        if (score > highScore) {
            this.preferences.edit().putInt("high_score", score).apply();
        }
    }

    public int getHighScore() {
        return this.preferences.getInt("high_score", 0);
    }


    public int saveClickedButton(int buttonIndex) {
        int clickedButton = this.preferences.getInt("clicked_button_" + buttonIndex, -1);
        if (clickedButton < 0) {
            this.preferences.edit().putInt("clicked_button_" + buttonIndex, buttonIndex).apply();
            clickedButtonsList.add("clicked_button_" + buttonIndex);
            return 0;
        }
        return 1;
    }

    public void removeClickedButtons() {
        for (String clickedButton : clickedButtonsList)
        this.preferences.edit().remove(clickedButton).apply();
    }
}
