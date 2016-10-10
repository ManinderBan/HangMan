package com.maninder.newhangman.hangman.domain.usecase;

import android.support.annotation.NonNull;

import com.maninder.newhangman.executor.UseCase;
import com.maninder.newhangman.data.HangmanDatasource;
import com.maninder.newhangman.data.HangmanRepository;
import com.maninder.newhangman.hangman.domain.filter.GuessFilter;
import com.maninder.newhangman.hangman.domain.model.GuessModel;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * This Use Case is used to get Guess String from Backend Server if is possible, other to get From local
 */
public class GetGuess extends UseCase<GetGuess.RequestValue, GetGuess.ResponseValue> {

    private final HangmanRepository mHangmanRepository;

    /**
     * @param hangmanRepository: Singleton Instance that allow to get data from Server and Local
     */
    public GetGuess(@NonNull HangmanRepository hangmanRepository) {
        mHangmanRepository = hangmanRepository;
    }

    /**
     * Get data from Repository{@link HangmanRepository}.
     * Get from remote if possible otherwise get Local
     *
     * @param requestValues: contain {@link GuessModel} that hold guess data
     */
    @Override
    protected void executeUseCase(final RequestValue requestValues) {
        mHangmanRepository.getGuess(new HangmanDatasource.LoadGuessCallback() {
            @Override
            public void onGuessLoaded(String guess) {
                requestValues.mGuessModel.guess = guess.toLowerCase();
                requestValues.mGuessModel.recreateModel();
                GuessFilter guessFilter = new GuessFilter();
                GuessModel guessModel = guessFilter.filter(requestValues.mGuessModel);
                ResponseValue responseValue = new ResponseValue(guessModel);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNoyAvailable() {
            }
        });
    }

    /**
     * Contain data to send for a Request
     */
    public static final class RequestValue implements UseCase.RequestValues {
        private final GuessModel mGuessModel;

        public RequestValue(@NonNull GuessModel guessModel) {
            mGuessModel = guessModel;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final GuessModel mGuessModel;

        public ResponseValue(@NonNull GuessModel guessModel) {
            mGuessModel = guessModel;
        }

        public GuessModel getGuess() {
            return mGuessModel;
        }

    }

}
