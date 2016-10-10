package com.maninder.newhangman.data;

import android.support.annotation.NonNull;

/**
 * Created by Maninder on 03/10/16.
 */

/**
 * This implementation is only created for testing
 */
public class FakeDataSource implements HangmanDatasource {

    String tempGuess = "HELLO";

    private static FakeDataSource INSTANCE;

    // Prevent direct instantiation.
    private FakeDataSource() {
    }

    public static FakeDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getGuess(@NonNull LoadGuessCallback callback) {
        callback.onGuessLoaded(tempGuess);
    }

    @Override
    public void saveGuess(@NonNull String guess) {
        tempGuess = guess;
    }

    @Override
    public void sendGuessToServer(@NonNull String letter, @NonNull LoadGuessResponseCallback loadGuessResponseCallback) {
        loadGuessResponseCallback.onGuessResponse(0);
    }
}
