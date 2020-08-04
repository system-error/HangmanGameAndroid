package com.example.hangmangame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private String uri = "https://random-word-api.herokuapp.com/word?number=1";
    Button retrieveTheWordButton,checkButton ;
    TextView retrieveWordsTextView,textViewForMessages;
    LinearLayout wrongLetterContainer,drawTheHangman;
    HangmanModel hangmanModel;
    MyCanvas drawingArea;
    EditText letters;
    int counter = 0;
    boolean playAgain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawTheHangman = findViewById(R.id.canvasLayout);
        retrieveWordsTextView = findViewById(R.id.wordsTextView);
        textViewForMessages = findViewById(R.id.textViewForMessages);
        retrieveTheWordButton = findViewById(R.id.retrieveTheWordButton);
        checkButton = findViewById(R.id.checkButton);
        letters = findViewById(R.id.lettersEditText);
        hangmanModel = new HangmanModel();
        retrieveTheWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeTheWord();
            }
        });
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheWord();
            }
        });

        drawingArea = new MyCanvas(this, "");
        drawTheHangman.addView(drawingArea);
    }

    private void takeTheWord(){
        if(playAgain){
            hangmanModel = new HangmanModel();
            playAgain = false;
            textViewForMessages.setText("");
            counter = 0;
            wrongLetterContainer.removeAllViews();
            displayTheHangman();
        }
        callTheApi();
    }

    private void checkTheWord(){
                    if(letters.length() > 1){
                        Toast.makeText(MainActivity.this,"You cannot place 2 or more characters",Toast.LENGTH_LONG).show(); ;
                        return;
                    }
                    if (letters.getText().toString().matches("")){
                        Toast.makeText(MainActivity.this,"You must have 1 character",Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        if (counter < 6) {
                            if(!hangmanModel.wrongCharacterAlreadySelected(letters.getText().charAt(0))){
                                doTheCheck();
                                if(hangmanModel.getIncorrectChar() !=0){
                                    displayTheHangman();
                                    placeTheIncorrectLetters();
                                }
                            }
                        }
                        if(counter == 6){
                            textViewForMessages.setText("LOST");
                            playAgain = true;
                            checkButton.setEnabled(false);
                            letters.setEnabled(false);
                            retrieveTheWordButton.setEnabled(true);
                            retrieveTheWordButton.setText("Play Again");
                        }
                        letters.setText("");
                    }
                    haveWinner();
    }


    private void haveWinner(){
        if (hangmanModel.lookForWinner() == false) {
            return;
        }else{
            retrieveWordsTextView.setText(hangmanModel.getWordWithDashes());
            textViewForMessages.setText("WINNERRRRRRRRR");
        }
    }

    private void doTheCheck(){
        hangmanModel.checkTheLetter(letters.getText().charAt(0));
        retrieveWordsTextView.setText(hangmanModel.getWordWithDashes());
    }

    private void placeTheIncorrectLetters(){
            counter++;
            wrongLetterContainer = findViewById(R.id.wrongLetters);
            View row = getLayoutInflater().inflate(R.layout.wrong_letters,null);
            wrongLetterContainer.addView(row);
            TextView text = row.findViewById(R.id.wrong_characters);
            String character = String.valueOf(hangmanModel.getIncorrectChar());
            text.setText(character);
    }

    private void displayTheHangman(){
            drawingArea.setPartOfBody(hangmanModel.getCurrentPartOfBody());
            drawingArea.invalidate();
    }

    private void callTheApi(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonArrayRequest request = new JsonArrayRequest(uri, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i("word",response.getString(0));
                    hangmanModel.setWord(response.getString(0));
                    retrieveWordsTextView.setText(hangmanModel.getWordWithDashes());
                    letters.setEnabled(true);
                    checkButton.setEnabled(true);
                    retrieveTheWordButton.setEnabled(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                    if(volleyError.getMessage().startsWith("java.net.UnknownHostException")){
                        Toast.makeText(MainActivity.this, "Unable to fetch data: Check your internet connection or the site is down ", Toast.LENGTH_LONG).show();
                        return;
                    }
                Toast.makeText(MainActivity.this, "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }
}