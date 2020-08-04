package com.example.hangmangame;

import java.util.ArrayList;

public class HangmanModel {

    private String[] partsOfBody = {"","head","body","leftHand","rightHand","leftFoot","rightFoot"};
    private String word;
    private String wordWithDashes;
    private ArrayList<Character> incorrectLetters = new ArrayList<>();
    private char incorrectChar;
    private int counter;
    private int[] indexes;

    public HangmanModel(){
        counter = 0;
    }

    public void setWord(String word) {
        this.word = word;
        createWordWithDashes();
    }

    public void createWordWithDashes(){
        indexes = new int[word.length()];
        wordWithDashes = "" + word.charAt(0);
        for(int i = 1; i < word.length()-1; i++) {
            wordWithDashes += "-";
        }
        wordWithDashes += word.charAt(word.length()-1);
    }

    public String getCurrentPartOfBody() {
        return partsOfBody[incorrectLetters.size()];
    }

    public String getWordWithDashes() {
        return wordWithDashes;
    }

    public ArrayList<Character> getIncorrectLetters() {
        return incorrectLetters;
    }

    public char getIncorrectChar() {
        return incorrectChar;
    }

    public void checkTheLetter(char character){
        counter = 0;
        boolean flag = false;
        for (char letter : word.toCharArray()){
            if(letter == character){
                indexes[counter] = letter;
                flag= true;
            }
            counter++;
        }
        if (flag==false){
            incorrectChar = character;
            incorrectLetters.add(character);
        }else{
            incorrectChar = 0;
            replaceTheCorrectLettersInTheWord();
        }
    }

    protected void replaceTheCorrectLettersInTheWord(){
        char[] tempWord = wordWithDashes.toCharArray();
        for (int i=0; i < tempWord.length; i++){
            if(indexes[i] > 0 ){
                tempWord[i] = Character.valueOf((char)indexes[i]);
            }
        }
        wordWithDashes = new String(tempWord);
    }

    public boolean wrongCharacterAlreadySelected(char character) {
        return incorrectLetters.contains(character);
    }

    public boolean lookForWinner(){
        return wordWithDashes.equals(word) ? true : false;
    }

}















//    protected void wordWithDashes(){
//        indexes = new int[word.length()];
//        char[] chars =  word.toCharArray();
//        char[] tempChar = chars;
//
//        for (int i=1; i < chars.length-1; i++){
//            tempChar[i] = '-';
//        }
//        wordWithDashes =  new String(tempChar);
//    }