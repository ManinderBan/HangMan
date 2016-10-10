package com.maninder.newhangman;

import android.content.Context;
import android.support.annotation.NonNull;

import com.maninder.newhangman.data.HangmanRepository;
import com.maninder.newhangman.data.local.HangmanLocalDataSource;
import com.maninder.newhangman.data.remote.HangmanRemoteDataSource;
import com.maninder.newhangman.executor.UseCaseHandler;
import com.maninder.newhangman.hangman.domain.usecase.CheckGuess;
import com.maninder.newhangman.hangman.domain.usecase.GetGuess;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Maninder on 03/10/16.
 */

/**
 * Enables injection of mock implementations for
 * {@link HangmanRepository} at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
public class Injection {

    public static UseCaseHandler provideUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }

    public static HangmanRepository provideHangmanRepository(@NonNull Context context) {
        checkNotNull(context);
        return HangmanRepository.getInstance(HangmanLocalDataSource.newInstance(context), HangmanRemoteDataSource.newInstance());
    }

    public static GetGuess provideGetGuess(@NonNull Context context) {
        return new GetGuess(provideHangmanRepository(context));
    }

    public static CheckGuess provideCheckGuess(@NonNull Context context) {
        return new CheckGuess(provideHangmanRepository(context));
    }
}
