package com.maninder.newhangman.hangman.domain.filter;

import com.maninder.newhangman.hangman.domain.model.GuessModel;

import java.util.Map;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * This class return an updated GuessModel in {@link com.maninder.newhangman.hangman.domain.usecase.GetGuess}
 */
public class GuessFilter {

    /**
     * @return GuessModel with a new Guessed String
     */
    public GuessModel filter(GuessModel guessModel) {
        GuessModel mGuessModel = guessModel;
        char[] st = new char[mGuessModel.guess.length()];
        for (Map.Entry<Integer, Boolean> keyValye : mGuessModel.guessTrue.entrySet()) {
            if (!keyValye.getValue()) {
                st[keyValye.getKey()] = '_';
            } else {
                st[keyValye.getKey()] = mGuessModel.guess.charAt(keyValye.getKey());
            }
        }
        mGuessModel.guessed = String.valueOf(st);
        return mGuessModel;
    }


}
