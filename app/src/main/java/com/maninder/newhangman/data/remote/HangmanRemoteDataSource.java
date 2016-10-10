package com.maninder.newhangman.data.remote;

import android.support.annotation.NonNull;

import com.maninder.newhangman.data.HangmanDatasource;
import com.maninder.newhangman.data.remote.model.GuessRemote;
import com.maninder.newhangman.data.remote.model.GuessResponse;
import com.maninder.newhangman.data.remote.service.HangmanService;
import com.maninder.newhangman.data.remote.service.SendHangmanService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Maninder on 02/10/16.
 */

/**
 * Implementation of the Remote Data source to get {@link GuessRemote} from server
 * and the {@link GuessResponse}
 */
public class HangmanRemoteDataSource implements HangmanDatasource {

    private static HangmanRemoteDataSource INSTANCE;

    public static HangmanRemoteDataSource newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HangmanRemoteDataSource();
        }
        return INSTANCE;
    }

    /**
     * Get Guess from server through Retrofit 2 and rxJava
     * This type of request is async
     * I choose to use the {@link GuessRemote} model to get the information from server
     * I aspect an JSON as response from server, and Retrofit automatically covert JSON in {@link GuessRemote}
     *
     * @param callback: used to return the Guess String to {@link com.maninder.newhangman.data.HangmanRepository}
     */
    @Override
    public void getGuess(@NonNull final LoadGuessCallback callback) {
        //100 millisecond to get the response
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(50, TimeUnit.MILLISECONDS).build();
        //Get Guess From Server
        //https://anysite.com/hangman/ --> Scheme for our URL
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://anysite.com/hangman/")
                .build();

        HangmanService gurdwaraService = retrofit.create(HangmanService.class);

        // getNewGuess --> Where we can get the new guess
        Observable<GuessRemote> hangmanRequest = gurdwaraService.getHangmanGuess("/getNewGuess");
        hangmanRequest.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GuessRemote>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNoyAvailable();
                    }

                    @Override
                    public void onNext(GuessRemote s) {
                        callback.onGuessLoaded(s.guess);
                    }
                });
    }

    @Override
    public void saveGuess(@NonNull String guess) {
    }

    /**
     * Send Guess to server and get the JSON response from server. The server valuate if the Guess is right or not
     *
     * @param letter                    the letter chosen by the user
     * @param loadGuessResponseCallback callback to send the response to the UseCase
     */
    @Override
    public void sendGuessToServer(@NonNull String letter, @NonNull final LoadGuessResponseCallback loadGuessResponseCallback) {
        //50 millisecond to get the response
        final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(50, TimeUnit.MILLISECONDS).build();
        //Get Guess From Server
        //https://anysite.com/hangman/ --> Scheme for our URL
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://anysite.com/hangman/")
                .build();

        //TODO: change the POST string for request in SendHangmanService
        SendHangmanService gurdwaraService = retrofit.create(SendHangmanService.class);

        // getNewGuess --> Where we can get the new guess
        Observable<GuessResponse> hangmanRequest = gurdwaraService.sendHangmanGuess(letter);
        hangmanRequest.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GuessResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadGuessResponseCallback.onDataNoyAvailable();
                    }

                    @Override
                    public void onNext(GuessResponse s) {
                        loadGuessResponseCallback.onGuessResponse(s.response);
                    }
                });
    }


}

