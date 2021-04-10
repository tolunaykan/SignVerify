package com.tolunaykandirmaz.signverify.onboarding.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.tolunaykandirmaz.signverify.R;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PermissionFragment extends Fragment implements SlidePolicy {

    private TextView titleTextView, descriptionTextView;

    private ImageView imageView;

    private final String title;

    private final String description;

    private final int image;

    private boolean isStoragePermissionConfirmed = false;

    private boolean isCameraPermissionConfirmed = false;

    private static final int STORAGE_PERMISSION_REQUEST = 200;

    private static final int CAMERA_PERMISSION_REQUEST = 201;

    private static final int IMAGE_PICK_GALLERY_REQUEST = 300;

    String[] storagePermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    String[] cameraPermissions = new String[]{Manifest.permission.CAMERA};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.onboarding_permission_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleTextView = view.findViewById(R.id.title);
        descriptionTextView = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.image);
        final Button storagePermissionButton = view.findViewById(R.id.storage_permission_button);
        final Button cameraPermissionButton = view.findViewById(R.id.camera_permission_button);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        imageView.setImageResource(image);

        storagePermissionButton.setOnClickListener(v -> requestStoragePermission());
        cameraPermissionButton.setOnClickListener(v -> requestCameraPermission());

    }

    @Override
    public boolean isPolicyRespected() {
        return checkStoragePermission() && checkCameraPermission();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        if(!isCameraPermissionConfirmed){
            Toast.makeText(requireContext(), requireContext().getString(R.string.came_permission_warning), Toast.LENGTH_SHORT).show();
        }

        if(!isStoragePermissionConfirmed){
            Toast.makeText(requireContext(), requireContext().getString(R.string.storage_permission_warning), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkStoragePermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCameraPermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission(){

        if(checkStoragePermission()){
            Toast.makeText(requireContext(), requireContext().getString(R.string.storage_permission_exists), Toast.LENGTH_SHORT).show();
            return;
        }

        requestPermissions(storagePermissions, STORAGE_PERMISSION_REQUEST);
    }

    private void requestCameraPermission(){

        if(checkCameraPermission()){
            Toast.makeText(requireContext(), requireContext().getString(R.string.camera_permission_exists), Toast.LENGTH_SHORT).show();
            return;
        }

        requestPermissions(cameraPermissions, CAMERA_PERMISSION_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_REQUEST && grantResults.length > 0) {
            isStoragePermissionConfirmed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length > 0) {
            isCameraPermissionConfirmed = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
