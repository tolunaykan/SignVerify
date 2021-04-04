package com.tolunaykandirmaz.signverify;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroPageTransformerType;
import com.tolunaykandirmaz.signverify.onboarding.fragment.DefaultFragment;
import com.tolunaykandirmaz.signverify.onboarding.fragment.FragmentWithButtons;

public class MainActivity extends AppIntro {

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(DefaultFragment.newInstance(getString(R.string.welcome_fragment_title), getString(R.string.welcome_fragment_description), R.drawable.welcome));

        addSlide(FragmentWithButtons.newInstance("Gerekli İzinler", "Uygulamayı kullanabilmek için bazı izinleri vermen gerekiyor", R.mipmap.ic_launcher));

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