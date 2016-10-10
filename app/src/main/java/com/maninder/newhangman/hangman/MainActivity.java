package com.maninder.newhangman.hangman;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maninder.newhangman.Injection;
import com.maninder.newhangman.R;
import com.maninder.newhangman.hangman.domain.model.GuessModel;

/**
 * For this Application i used MVP + Clean Architecture, with this approach we have an
 * Application maintainable and testable
 * <p>
 * this make the code, independent from framework, independent of UI, independent of database.
 * <p>
 * This class hold {@link HangmanPresenter} that is the Presenter for this Activity
 */
public class MainActivity extends AppCompatActivity {

    HangmanPresenter hangmanPresenter;

    private static final String CURRENT_GUESS_MODEL = "CURRENT_GUESS__MODEL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HangmanFragment hangmanFragment = (HangmanFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (hangmanFragment == null) {
            //Create the fragment
            hangmanFragment = HangmanFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, hangmanFragment);
            transaction.commit();
        }

        //Initialize the Hangman Presenter
        hangmanPresenter = new HangmanPresenter(
                Injection.provideUseCaseHandler(),
                hangmanFragment,
                Injection.provideGetGuess(this),
                Injection.provideCheckGuess(this),
                new GuessModel()
        );

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            GuessModel currentModel = (GuessModel) savedInstanceState.getParcelable(CURRENT_GUESS_MODEL);
            hangmanPresenter.setGuessModel(currentModel);
        }
    }

    /**
     * Save {@link GuessModel} that hold the Guess data
     *
     * @param outState bundle that allow {@link GuessModel} to save as a Parcelable
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_GUESS_MODEL, hangmanPresenter.getGuessModel());
        super.onSaveInstanceState(outState);
    }
}
