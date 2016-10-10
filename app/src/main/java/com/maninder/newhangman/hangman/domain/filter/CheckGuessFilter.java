package com.maninder.newhangman.hangman.domain.filter;

import android.support.annotation.NonNull;

import com.maninder.newhangman.hangman.domain.model.GuessModel;

import java.util.Map;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * This class an updated GuessModel in {@link com.maninder.newhangman.hangman.domain.usecase.CheckGuess}
 */
public class CheckGuessFilter {

    private GuessModel mGuessModel;

    public CheckGuessFilter(@NonNull GuessModel guessModel) {
        mGuessModel = guessModel;
    }

    public GuessModel getNewModel(String letter) {
        filter(letter);
        getMapString();
        checkWin();
        return mGuessModel;
    }

    public GuessModel filter(String letter) {
        boolean isGuessTrue = false;
        for (int index = mGuessModel.guess.indexOf(letter); index >= 0; index = mGuessModel.guess.indexOf(letter, index + 1)) {
            if (mGuessModel.guessTrue.containsKey(index)) {
                mGuessModel.guessTrue.put(index, true);
                isGuessTrue = true;
            }
        }
        if (!isGuessTrue) {
            mGuessModel.count++;
        }
        return mGuessModel;
    }

    /**
     * This Method the the word in which we found the char Guessed
     *
     * @return String --> The Word Guessed
     */

    public void getMapString() {
        char[] st = new char[mGuessModel.guess.length()];
        for (Map.Entry<Integer, Boolean> keyValye : mGuessModel.guessTrue.entrySet()) {
            if (!keyValye.getValue()) {
                st[keyValye.getKey()] = '_';
            } else {
                st[keyValye.getKey()] = mGuessModel.guess.charAt(keyValye.getKey());
            }
        }
        mGuessModel.guessed = String.valueOf(st);
    }

    /**
     * Check If the User Win or Not
     */
    public void checkWin() {
        for (Map.Entry<Integer, Boolean> keyValye : mGuessModel.guessTrue.entrySet()) {
            if (!keyValye.getValue()) {
                return;
            }
        }
        mGuessModel.isCompleted = true;
    }

}
