package com.maninder.newhangman.data;

import android.support.annotation.NonNull;

/**
 * Created by Maninder on 02/10/16.
 */

public interface HangmanDatasource {

    interface LoadGuessCallback {
        void onGuessLoaded(String guess);

        void onDataNoyAvailable();

    }

    interface LoadGuessResponseCallback {
        void onGuessResponse(int guessResponse);

        void onDataNoyAvailable();

    }

    void getGuess(@NonNull LoadGuessCallback callback);

    void saveGuess(@NonNull String guess);

    void sendGuessToServer(@NonNull String letter, @NonNull final LoadGuessResponseCallback loadGuessResponseCallback);
}
