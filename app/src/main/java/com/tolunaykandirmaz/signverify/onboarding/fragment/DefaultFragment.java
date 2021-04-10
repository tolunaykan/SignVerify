package com.tolunaykandirmaz.signverify.onboarding.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tolunaykandirmaz.signverify.R;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultFragment extends Fragment {

    private TextView titleTextView, descriptionTextView;

    private ImageView imageView;

    private final String title;

    private final String description;

    private final int image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_default_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView = view.findViewById(R.id.title);
        descriptionTextView = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.image);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageResource(image);

    }

}
