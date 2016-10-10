package com.maninder.newhangman.hangman.domain.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Maninder on 02/10/16.
 */

public class GuessModel implements Parcelable {


    /**
     * This Hash Map Maintain the reference into the String position, the char already founded
     */
    public HashMap<Integer, Boolean> guessTrue = new HashMap<>();

    /**
     * Count the Guessed Failed
     */
    public int count = 0;

    /**
     * The Letter to guess
     */
    public String guess;

    /**
     * The Guess string already guessed
     */
    public String guessed;
    /**
     * All the letter clicked by User
     */
    public String letterGuessed;

    /**
     * To get information about the Game
     * true: WIN
     * false: LOSE
     */
    public boolean isCompleted = false;

    public GuessModel() {
        guess = "";
        guessed = "";
        letterGuessed = "";
    }

    /**
     * Recreate the Guess Model for a new Game
     */
    public void recreateModel() {
        populateMap();
        guessed = "";
        letterGuessed = "";
        count = 0;
        isCompleted = false;
    }

    public void populateMap() {
        checkNotNull(guess);
        guessTrue = new HashMap<>();
        for (int i = 0; i < guess.length(); i++) {
            guessTrue.put(i, false);
        }
    }

    public void setGuess(String guess) {
        this.guess = guess.toLowerCase();
    }

    /**
     * Parcelable used to save and load data
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    private GuessModel(Parcel in) {
        guess = in.readString();
        guessed = in.readString();
        isCompleted = in.readByte() != 0;
        count = in.readInt();
        letterGuessed = in.readString();
        Bundle bundle = in.readBundle();
        guessTrue = (HashMap<Integer, Boolean>) bundle.getSerializable("guessMap");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(guess);
        dest.writeString(guessed);
        dest.writeByte((byte) (isCompleted ? 1 : 0));
        dest.writeInt(count);
        dest.writeString(letterGuessed);
        Bundle bundle = new Bundle();
        bundle.putSerializable("guessMap", guessTrue);
        dest.writeBundle(bundle);

    }

    public static final Parcelable.Creator<GuessModel> CREATOR
            = new Parcelable.Creator<GuessModel>() {
        public GuessModel createFromParcel(Parcel in) {
            return new GuessModel(in);
        }

        public GuessModel[] newArray(int size) {
            return new GuessModel[size];
        }
    };

}
