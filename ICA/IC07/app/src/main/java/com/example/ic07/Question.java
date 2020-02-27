package com.example.ic07;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class Question {
    private int id;
    private String text;
    private String imageURL;
    private int correctOption;
    private ArrayList<String> choices;

    public Question(JSONObject json) {
        choices = new ArrayList<>();
        try {
            this.id = json.getInt("id");
            this.text = json.getString("text");

            JSONObject optionData = json.getJSONObject("choices");

            this.correctOption = optionData.getInt("answer");

            JSONArray options = optionData.getJSONArray("choice");

            for(int i = 0; i < options.length(); i++){
                String s = options.getString(i);
                choices.add(s);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            this.imageURL = json.getString("image");
        } catch (JSONException e) {
            e.printStackTrace();
            this.imageURL = null;
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }
}
