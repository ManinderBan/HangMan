package com.maninder.newhangman.data.remote.service;

import com.maninder.newhangman.data.remote.model.GuessResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Maninder on 02/10/16.
 */

public interface SendHangmanService {
    @FormUrlEncoded
    @POST("/checkGuess")
    Observable<GuessResponse> sendHangmanGuess(@Field("guess") String letter);
}
