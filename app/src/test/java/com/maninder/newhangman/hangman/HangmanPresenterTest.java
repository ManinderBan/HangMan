package com.maninder.newhangman.hangman;

import com.maninder.newhangman.TestUseCaseScheduler;
import com.maninder.newhangman.data.HangmanDatasource;
import com.maninder.newhangman.data.HangmanRepository;
import com.maninder.newhangman.executor.UseCase;
import com.maninder.newhangman.executor.UseCaseHandler;
import com.maninder.newhangman.hangman.domain.filter.CheckGuessFilter;
import com.maninder.newhangman.hangman.domain.model.GuessModel;
import com.maninder.newhangman.hangman.domain.usecase.CheckGuess;
import com.maninder.newhangman.hangman.domain.usecase.GetGuess;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by Maninder on 03/10/16.
 */

public class HangmanPresenterTest {

    private static GuessModel guessModel;

    @Mock
    private HangmanRepository mHangmanRepository;

    @Mock
    private HangmanContract.View mHangmanView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<HangmanDatasource.LoadGuessCallback> mLoadGuessCallbackCaptor;

    private HangmanPresenter mHangmanPresenter;

    @Before
    public void setupTasksPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.

        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test

        mHangmanPresenter = givenTasksPresenter();

        // We start with a populated GuessModel
        guessModel = new GuessModel();
        guessModel.guess = "helloo";
        guessModel.recreateModel();
        guessModel.guessed = "______";
    }

    /**
     * @return Mock HangmanPresenter
     */
    private HangmanPresenter givenTasksPresenter() {
        UseCaseHandler useCaseHandler = new UseCaseHandler(new TestUseCaseScheduler());
        GetGuess getGuess = new GetGuess(mHangmanRepository);
        CheckGuess checkGuess = new CheckGuess(mHangmanRepository);
        return new HangmanPresenter(useCaseHandler, mHangmanView, getGuess, checkGuess, new GuessModel());
    }

    @Test
    public void loadGuessFromRepositoryAndLoadIntoView() {
        //This first step check the when the application Start
        mHangmanPresenter.loadGuess();

        //Callback captured and invoked
        verify(mHangmanRepository).getGuess(mLoadGuessCallbackCaptor.capture());
        //Get the guess value
        mLoadGuessCallbackCaptor.getValue().onGuessLoaded(guessModel.guess);

        //Check the Guessed String
        verify(mHangmanView).changeGuessLayout(guessModel.guessed);
        verify(mHangmanView).updateUI(guessModel.count, guessModel.isCompleted);
    }

}
