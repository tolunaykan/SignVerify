package com.tolunaykandirmaz.signverify;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;
import com.tolunaykandirmaz.signverify.onboarding.fragment.CustomOnboardingFragment1;

public class MainActivity extends AppIntro {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance(getString(R.string.hosgeldin), getString(R.string.fragement1_tanitim_yazisi), R.mipmap.ic_launcher, Color.BLUE));

        addSlide(CustomOnboardingFragment1.newInstance());


        addSlide(AppIntroFragment.newInstance(
                "Clean App Intros",
                "This library offers developers the ability to add clean app intros at the start of their apps.",
                R.mipmap.ic_launcher
        ));


        // Fade Transition
        setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }
}