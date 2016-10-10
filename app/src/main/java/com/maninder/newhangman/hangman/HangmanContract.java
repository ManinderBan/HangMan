package com.maninder.newhangman.hangman;

import android.support.annotation.NonNull;

import com.maninder.newhangman.BasePresenter;
import com.maninder.newhangman.BaseView;
import com.maninder.newhangman.hangman.domain.model.GuessModel;

/**
 * Created by Maninder on 02/10/16.
 */

public interface HangmanContract {

    interface View extends BaseView<Presenter> {

        void addLetterLayout(String letterGuessed);

        void changeGuessLayout(String guess);

        void updateUI(int chance, boolean isCompleted);

    }

    interface Presenter extends BasePresenter {

        void resetGame();

        void checkLetter(String letter);

        void loadGuess();

        GuessModel getGuessModel();

        public void setGuessModel(@NonNull GuessModel guessModel);
    }
}
