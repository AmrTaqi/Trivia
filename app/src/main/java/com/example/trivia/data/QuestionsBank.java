package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;

import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import java.util.ArrayList;
import java.util.List;



public class QuestionsBank {

   private ArrayList<Question> questionsList = new ArrayList<>();

    public List<Question> getQuestions (final AnswerListAsyncResponse callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json",
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        Question question = new Question();
                        question.setAnswer(response.getJSONArray(i).get(0).toString());
                        question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));
                        questionsList.add(question);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (null != callback) callback.processFinished(questionsList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionsList;
    }
}