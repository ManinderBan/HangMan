package com.maninder.newhangman.hangman;

import android.support.annotation.NonNull;

import com.maninder.newhangman.executor.UseCase;
import com.maninder.newhangman.executor.UseCaseHandler;
import com.maninder.newhangman.hangman.domain.model.GuessModel;
import com.maninder.newhangman.hangman.domain.usecase.CheckGuess;
import com.maninder.newhangman.hangman.domain.usecase.GetGuess;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * This is the Presenter in MVP Architecture.
 * This class listen the user actions from the UI {@link HangmanFragment}, retrieves the data from UseCase
 * and updates the UI as required
 */
public class HangmanPresenter implements HangmanContract.Presenter {

    private final HangmanContract.View mHangmanView;
    private final GetGuess mGetGuess;
    private final UseCaseHandler mUseCaseHandler;
    private GuessModel mGuessModel;
    private final CheckGuess mCheckGuess;

    /**
     * Constructor
     *
     * @param useCaseHandler: This allow to run UseCase in separated Thread
     * @param hangmanView:    Here is where we hold the UI
     * @param getGuess:       UseCase that allow to retrieve data from Server or Local
     * @param checkGuess:     UseCase to check the Guess
     * @param guessModel:     Hold Guess data
     */
    public HangmanPresenter(@NonNull UseCaseHandler useCaseHandler,
                            @NonNull HangmanContract.View hangmanView,
                            @NonNull GetGuess getGuess,
                            @NonNull CheckGuess checkGuess,
                            @NonNull GuessModel guessModel) {
        mHangmanView = hangmanView;
        mUseCaseHandler = useCaseHandler;
        mGetGuess = getGuess;
        mCheckGuess = checkGuess;
        mGuessModel = guessModel;

        mHangmanView.setPresenter(this);
    }

    /**
     * Set up the UI when User launch the application
     * I set the WAIT! String when the Application start because retrieve data from Server coast time.
     */
    @Override
    public void start() {
        mHangmanView.addLetterLayout(mGuessModel.letterGuessed);
        if (mGuessModel.guessTrue.size() > 0) {
            mHangmanView.changeGuessLayout(mGuessModel.guessed);
            mHangmanView.updateUI(mGuessModel.count, mGuessModel.isCompleted);
        } else {
            mHangmanView.changeGuessLayout("WAIT!");
            loadGuess();
        }
    }

    /**
     * This method is called when use want to start a new Game
     */
    @Override
    public void resetGame() {
        mHangmanView.addLetterLayout("");
        mHangmanView.changeGuessLayout("WAIT!");
        loadGuess();
    }

    /**
     * This method get the new Guess from Server {@link com.maninder.newhangman.data.remote.HangmanRemoteDataSource}
     * or from Local {@link com.maninder.newhangman.data.local.HangmanLocalDataSource}
     * <p>
     * {@link com.maninder.newhangman.executor.UseCase.UseCaseCallback} --> is used as a callback to get
     * the data from Repository {@link com.maninder.newhangman.data.HangmanRepository}
     * <p>
     *
     * Take 50Millisecond to get Guess From server after that try to get Guess from local.
     * This is why we need to wait before to start the game.
     */
    @Override
    public void loadGuess() {
        GetGuess.RequestValue requestValue = new GetGuess.RequestValue(mGuessModel);

        mUseCaseHandler.execute(mGetGuess, requestValue,
                new UseCase.UseCaseCallback<GetGuess.ResponseValue>() {
                    @Override
                    public void onSuccess(GetGuess.ResponseValue response) {
                        mGuessModel = response.getGuess();
                        mHangmanView.changeGuessLayout(mGuessModel.guessed);
                        mHangmanView.updateUI(mGuessModel.count, mGuessModel.isCompleted);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    /**
     * This method is called to check the letter guessed by the User
     * <p>
     * {@link CheckGuess} is used to check if the letter is right or not
     *
     * @param letter: letter chosen by user
     */
    @Override
    public void checkLetter(String letter) {
        mGuessModel.letterGuessed = mGuessModel.letterGuessed + letter;
        if (mGuessModel.count > 6 || mGuessModel.isCompleted) {
            return;
        }

        CheckGuess.RequestValue requestValue = new CheckGuess.RequestValue(mGuessModel, letter);

        mUseCaseHandler.execute(mCheckGuess, requestValue, new UseCase.UseCaseCallback<CheckGuess.ResponseValue>() {
            @Override
            public void onSuccess(CheckGuess.ResponseValue response) {
                mGuessModel = response.getGuessModel();
                mHangmanView.changeGuessLayout(mGuessModel.guessed);
                mHangmanView.updateUI(mGuessModel.count, mGuessModel.isCompleted);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public GuessModel getGuessModel() {
        return mGuessModel;
    }

    @Override
    public void setGuessModel(@NonNull GuessModel guessModel) {
        checkNotNull(guessModel);
        mGuessModel = guessModel;
    }
}
