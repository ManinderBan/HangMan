package com.maninder.newhangman.data.remote.service;

import com.maninder.newhangman.data.remote.model.GuessRemote;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Maninder on 02/10/16.
 */

public interface HangmanService {

    @GET
    Observable<GuessRemote> getHangmanGuess(@Url String guess);
}
