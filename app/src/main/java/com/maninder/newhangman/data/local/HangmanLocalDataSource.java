package com.maninder.newhangman.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.maninder.newhangman.data.HangmanDatasource;
import com.maninder.newhangman.hangman.MainActivity;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * This is very Simple implementation of a data source
 * I used an {@link SharedPreferences} to save the Guess String
 */
public class HangmanLocalDataSource implements HangmanDatasource {

    String tempGuess = "HELLO";

    private static HangmanLocalDataSource INSTANCE;
    private Context mContext;

    /**
     * @param context: use to svae the Guess and retrieve the Guess String
     */
    public static HangmanLocalDataSource newInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HangmanLocalDataSource(context);
        }
        return INSTANCE;
    }

    public HangmanLocalDataSource(Context context) {
        mContext = context;
    }

    /**
     * Get the String Guess if is saved in SharedPreference, otherwise get the tempGuess
     *
     * @param callback
     */
    @Override
    public void getGuess(@NonNull LoadGuessCallback callback) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(LocalDataSourceCons.GUESS, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(LocalDataSourceCons.GUESS)) {
            callback.onGuessLoaded(sharedPreferences.getString(LocalDataSourceCons.GUESS, ""));
        } else {
            callback.onGuessLoaded(tempGuess);
            //Save the guess on Local
            saveGuess(tempGuess);
        }
    }

    /**
     * Save the Guess String
     *
     * @param guess: string to save
     */
    @Override
    public void saveGuess(@NonNull String guess) {
        SharedPreferences sharedPref = ((MainActivity) mContext).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LocalDataSourceCons.GUESS, guess);
        editor.apply();
    }

    @Override
    public void sendGuessToServer(@NonNull String letter, @NonNull final LoadGuessResponseCallback loadGuessResponseCallback) {

    }
}
