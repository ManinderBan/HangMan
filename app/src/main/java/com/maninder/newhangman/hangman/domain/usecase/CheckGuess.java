package com.maninder.newhangman.hangman.domain.usecase;

import android.support.annotation.NonNull;

import com.maninder.newhangman.executor.UseCase;
import com.maninder.newhangman.data.HangmanRepository;
import com.maninder.newhangman.hangman.domain.filter.CheckGuessFilter;
import com.maninder.newhangman.hangman.domain.model.GuessModel;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * USE CASE: Here we have all the class wrote in Java
 * Check the Letter passed by Presenter and return an updated GuessModel
 */
public class CheckGuess extends UseCase<CheckGuess.RequestValue, CheckGuess.ResponseValue> {

    /**
     * This Repository is used to send the choice to the server
     */
    private final HangmanRepository mHangmanRepository;

    /**
     * @param hangmanRepository: Singleton HangmanRepository that hold {@link com.maninder.newhangman.data.local.HangmanLocalDataSource} and
     *                           {@link com.maninder.newhangman.data.remote.HangmanRemoteDataSource}
     */
    public CheckGuess(@NonNull HangmanRepository hangmanRepository) {
        mHangmanRepository = hangmanRepository;
    }

    /**
     * This method is running in Thread
     *
     * @param requestValues: We send the GuessModel{@link GuessModel} and the letter
     */
    @Override
    protected void executeUseCase(final RequestValue requestValues) {
        //TODO: I commented this part because now we don't need it. In future we can de-commented this part to validate the user letter choice
        // send Guess to Server and get the response
        //0: WRONG
        //1: RIGHT

//        mHangmanRepository.sendGuessToServer(requestValues.mLetter, new HangmanDatasource.LoadGuessResponseCallback() {
//            @Override
//            public void onGuessResponse(int guessResponse) {
//                //We have the Response form Server, now we need to update the Local Data
//            }
//
//            @Override
//            public void onDataNoyAvailable() {
//                //We don't have any response from server
//                CheckGuessFilter checkGuessFilter = new CheckGuessFilter(requestValues.mGuessModel);
//                GuessModel guessModel = checkGuessFilter.getNewModel(requestValues.mLetter);
//                ResponseValue responseValue = new ResponseValue(guessModel);
//                getUseCaseCallback().onSuccess(responseValue);
//            }
//        });

        CheckGuessFilter checkGuessFilter = new CheckGuessFilter(requestValues.mGuessModel);
        GuessModel guessModel = checkGuessFilter.getNewModel(requestValues.mLetter);
        ResponseValue responseValue = new ResponseValue(guessModel);
        getUseCaseCallback().onSuccess(responseValue);
    }

    public static final class RequestValue implements UseCase.RequestValues {
        private final GuessModel mGuessModel;
        private final String mLetter;

        public RequestValue(@NonNull GuessModel guessModel,
                            @NonNull String letter) {
            mGuessModel = guessModel;
            mLetter = letter;

        }
    }

    /**
     * Get an updated GuessModel from Filter
     */
    public static final class ResponseValue implements UseCase.ResponseValue {
        private final GuessModel mGuessModel;


        public ResponseValue(@NonNull GuessModel guessModel) {
            mGuessModel = guessModel;

        }

        public GuessModel getGuessModel() {
            return mGuessModel;
        }

    }
}
