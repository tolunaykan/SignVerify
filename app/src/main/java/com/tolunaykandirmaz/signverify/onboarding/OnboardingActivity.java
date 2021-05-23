package com.tolunaykandirmaz.signverify.onboarding;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.tolunaykandirmaz.signverify.Constants;
import com.tolunaykandirmaz.signverify.MainActivity;
import com.tolunaykandirmaz.signverify.R;
import com.tolunaykandirmaz.signverify.SharedPreferencesManager;
import com.tolunaykandirmaz.signverify.onboarding.fragment.DefaultFragment;
import com.tolunaykandirmaz.signverify.onboarding.fragment.PermissionFragment;
import com.tolunaykandirmaz.signverify.onboarding.fragment.ReferenceFragment;

public class OnboardingActivity extends AppIntro {

    @Override
    protected void onStart() {

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        boolean isOnboardingDone = sharedPreferencesManager.getBoolean(Constants.SharedPrefConstants.IS_ONBOARDING_DONE);
        if (isOnboardingDone) {
            startMainActivity();
        }

        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final DefaultFragment welcomeFragment = new DefaultFragment(getString(R.string.welcome_fragment_title), getString(R.string.welcome_fragment_description), R.drawable.onboarding1);
        addSlide(welcomeFragment);

        final PermissionFragment permissionFragment = new PermissionFragment(getString(R.string.permission_fragment_title), getString(R.string.permission_fragment_description), R.drawable.onboarding2);
        addSlide(permissionFragment);

        final DefaultFragment tempFragment = new DefaultFragment("Geçiçi Fragment", "Geçici çözüm için koyuldu", R.drawable.onboarding1);
        addSlide(tempFragment);

        final ReferenceFragment referenceFragment = new ReferenceFragment(getString(R.string.reference_fragment_title), getString(R.string.reference_fragment_description), R.drawable.onboarding3);
        addSlide(referenceFragment);


        // Fade Transition
        //setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);

        //setColorDoneText(getColor(R.color.onboarding_buttons));
        setNextArrowColor(getColor(R.color.onboarding_buttons));
        //setBackArrowColor(getColor(R.color.onboarding_buttons));

        setIndicatorColor(getColor(R.color.onboarding_indicator_selected), getColor(R.color.onboarding_indicator_unselected));

        setSkipButtonEnabled(false);
        setWizardMode(true);
        setDoneText(R.string.done);
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        sharedPreferencesManager.setBoolean(Constants.SharedPrefConstants.IS_ONBOARDING_DONE, true);

        startMainActivity();
    }

    private void startMainActivity(){
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}