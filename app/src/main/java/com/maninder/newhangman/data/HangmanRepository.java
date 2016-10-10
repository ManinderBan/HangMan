package com.maninder.newhangman.data;

import android.support.annotation.NonNull;

import com.maninder.newhangman.data.local.HangmanLocalDataSource;
import com.maninder.newhangman.data.remote.HangmanRemoteDataSource;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * Concrete implementation to load the Guess String from the data sources into a cache.
 * <p>
 * This implementation is really simple, It's only used to get and save Guess in local.
 * <p>
 * This class is Singleton
 */
public class HangmanRepository implements HangmanDatasource {

    private static HangmanRepository INSTANCE = null;

    private final HangmanLocalDataSource mHangmanLocalDataSource;

    private final HangmanRemoteDataSource mHangmanRemoteDataSource;

    private HangmanRepository(@NonNull HangmanLocalDataSource hangmanLocalDataSource,
                              @NonNull HangmanRemoteDataSource hangmanRemoteDataSource) {
        mHangmanLocalDataSource = checkNotNull(hangmanLocalDataSource);
        mHangmanRemoteDataSource = checkNotNull(hangmanRemoteDataSource);
    }

    /**
     * @param localDataSource:  the device storage data source
     * @param remoteDataSource: the backend data source
     * @return the {@link HangmanRepository} instance
     */
    public static HangmanRepository getInstance(HangmanLocalDataSource localDataSource, HangmanRemoteDataSource remoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new HangmanRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;

    }

    /**
     * Get Guess from Remote first, if don't have any response get data from Local data source, whichever is available
     *
     * @param callback: call the class who created this Callback and send the Guess String
     */
    @Override
    public void getGuess(@NonNull final LoadGuessCallback callback) {
        checkNotNull(callback);

        mHangmanRemoteDataSource.getGuess(new LoadGuessCallback() {
            @Override
            public void onGuessLoaded(String guess) {
                //Save the Guess in Local
                mHangmanLocalDataSource.saveGuess(guess);
                callback.onGuessLoaded(guess);
            }

            @Override
            public void onDataNoyAvailable() {
                //Try to get Guess from Local
                mHangmanLocalDataSource.getGuess(new LoadGuessCallback() {
                    @Override
                    public void onGuessLoaded(String guess) {
                        callback.onGuessLoaded(guess);
                    }

                    @Override
                    public void onDataNoyAvailable() {
                        callback.onDataNoyAvailable();
                    }
                });
            }
        });
    }


    /**
     * Save the Guess String
     *
     * @param guess
     */
    @Override
    public void saveGuess(@NonNull String guess) {

    }

    /**
     * This method allow to send the Letter to the Backend Server and retrieve if the Letter is right or not;
     *
     * @param letter:                    the letter chosen by User
     * @param loadGuessResponseCallback: get the response
     */
    @Override
    public void sendGuessToServer(@NonNull String letter, @NonNull final LoadGuessResponseCallback loadGuessResponseCallback) {

        mHangmanRemoteDataSource.sendGuessToServer(letter, new LoadGuessResponseCallback() {
            @Override
            public void onGuessResponse(int guessResponse) {
                loadGuessResponseCallback.onGuessResponse(guessResponse);
            }

            @Override
            public void onDataNoyAvailable() {
                loadGuessResponseCallback.onDataNoyAvailable();
            }
        });

    }
}
