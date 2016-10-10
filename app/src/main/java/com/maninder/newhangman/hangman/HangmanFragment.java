package com.maninder.newhangman.hangman;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maninder.newhangman.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Maninder on 02/10/16.
 */

public class HangmanFragment extends Fragment implements HangmanContract.View {

    @BindView(R.id.textLinear)
    LinearLayout linearLayout;
    @BindView(R.id.line1)
    LinearLayout line1;
    @BindView(R.id.line2)
    LinearLayout line2;
    @BindView(R.id.line3)
    LinearLayout line3;
    @BindView(R.id.line4)
    LinearLayout line4;
    @BindView(R.id.circle)
    ImageView circle;
    @BindView(R.id.body)
    ImageView body;
    @BindView(R.id.leftHand)
    ImageView leftHand;
    @BindView(R.id.rightHand)
    ImageView rightHand;
    @BindView(R.id.leftLeg)
    ImageView leftLeg;
    @BindView(R.id.rightLeg)
    ImageView rightLeg;

    @BindView(R.id.reset)
    TextView reset;

    private HangmanContract.Presenter mPresenter;

    public static HangmanFragment newInstance() {
        return new HangmanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hangman_fragment, container, false);
        ButterKnife.bind(this, view);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset.setText(getActivity().getResources().getString(R.string.reset));
                reset.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.black));
                mPresenter.resetGame();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull HangmanContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * This change the View in which we have the Guess word
     *
     * @param word --> is the Word to Guess
     */
    @Override
    public void changeGuessLayout(String word) {
        linearLayout.removeAllViews();
        for (int i = 0; i < word.length(); i++) {
            TextView textView = new TextView(getActivity().getApplication());
            textView.setText(Character.toString(word.charAt(i)));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.size_word));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 5, 0);
            params.weight = 1;
            textView.setLayoutParams(params);
            linearLayout.addView(textView);
        }
    }

    /**
     * Here we built the UI regards the Alphabetic Layout
     */
    @Override
    public void addLetterLayout(String letterGuessed) {
        char[] alphabet = "abcdefg".toCharArray();
        char[] alphabet1 = "hijklmn".toCharArray();
        char[] alphabet2 = "opqrstu".toCharArray();
        char[] alphabet3 = "vwxyz".toCharArray();

        setClickableLayout(true);
        createLayoutAlph(alphabet, line1, letterGuessed);
        createLayoutAlph(alphabet1, line2, letterGuessed);
        createLayoutAlph(alphabet2, line3, letterGuessed);
        createLayoutAlph(alphabet3, line4, letterGuessed);

    }

    /**
     * Create the Alphabetic View
     *
     * @param chars      list of the Alphabetic char
     * @param linearAlph LinearLayout in which we add the Alphabetic line
     */
    public void createLayoutAlph(char[] chars, LinearLayout linearAlph, String letterGuessed) {
        linearAlph.removeAllViews();
        for (int i = 0; i < chars.length; i++) {
            final TextView textView = new TextView(getActivity().getApplication());
            textView.setText(Character.toString(chars[i]));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(getResources().getDimensionPixelSize(R.dimen.size_word));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 5, 0);
            params.weight = 1;
            textView.setLayoutParams(params);
            textView.setClickable(true);
            linearAlph.addView(textView);
            if (letterGuessed.indexOf(chars[i]) >= 0) {
                textView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                textView.setClickable(false);
            } else {
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.checkLetter(textView.getText().toString());
                        textView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        textView.setClickable(false);
                    }
                });
            }
        }
    }

    public void setClickableLayout(boolean clickableLayout) {
        line1.setClickable(clickableLayout);
        line2.setClickable(clickableLayout);
        line3.setClickable(clickableLayout);
        line4.setClickable(clickableLayout);
    }

    /**
     * Update the UI to show the User possibilities
     */
    @Override
    public void updateUI(int chance, boolean isCompleted) {
        switch (chance) {
            case 0:
                circle.setVisibility(View.INVISIBLE);
                body.setVisibility(View.INVISIBLE);
                leftHand.setVisibility(View.INVISIBLE);
                rightHand.setVisibility(View.INVISIBLE);
                leftLeg.setVisibility(View.INVISIBLE);
                rightLeg.setVisibility(View.INVISIBLE);
                break;
            case 1:
                circle.setVisibility(View.VISIBLE);
                break;
            case 2:
                circle.setVisibility(View.VISIBLE);
                body.setVisibility(View.VISIBLE);
                break;
            case 3:
                circle.setVisibility(View.VISIBLE);
                body.setVisibility(View.VISIBLE);
                leftHand.setVisibility(View.VISIBLE);
                break;
            case 4:
                circle.setVisibility(View.VISIBLE);
                body.setVisibility(View.VISIBLE);
                leftHand.setVisibility(View.VISIBLE);
                rightHand.setVisibility(View.VISIBLE);
                break;
            case 5:
                circle.setVisibility(View.VISIBLE);
                body.setVisibility(View.VISIBLE);
                leftHand.setVisibility(View.VISIBLE);
                rightHand.setVisibility(View.VISIBLE);
                leftLeg.setVisibility(View.VISIBLE);
                break;
            case 6:
                rightLeg.setVisibility(View.VISIBLE);
                reset.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                reset.setText(getActivity().getResources().getString(R.string.lose));
                setClickableLayout(false);
                break;
            default:
                circle.setVisibility(View.VISIBLE);
                body.setVisibility(View.VISIBLE);
                leftHand.setVisibility(View.VISIBLE);
                rightHand.setVisibility(View.VISIBLE);
                leftLeg.setVisibility(View.VISIBLE);
                reset.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                reset.setText(getActivity().getResources().getString(R.string.lose));
                setClickableLayout(false);
                break;
        }
        if (isCompleted) {
            reset.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.green));
            reset.setText(getActivity().getResources().getString(R.string.win));
            setClickableLayout(false);
        }
    }
}
