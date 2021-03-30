package com.tolunaykandirmaz.signverify.onboarding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.tolunaykandirmaz.signverify.R;

public class CustomOnboardingFragment1 extends Fragment implements SlidePolicy {

    private Button askPermissionButton;

    private boolean isClicked = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.custom_onboarding_layout1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        askPermissionButton = view.findViewById(R.id.button);

        askPermissionButton.setOnClickListener(v -> isClicked = true);
    }

    @Override
    public boolean isPolicyRespected() {
        return isClicked;
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(requireContext(), "Lütfen butona tıklayın", Toast.LENGTH_SHORT).show();
    }

    public static CustomOnboardingFragment1 newInstance(){
        return new CustomOnboardingFragment1();
    }
}
