package com.swiftsynq.medmanager.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.swiftsynq.medmanager.R;
import com.swiftsynq.medmanager.Utils.CircleTransform;
import com.swiftsynq.medmanager.data.MedManagerContract;
import com.swiftsynq.medmanager.data.MedManagerPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by popoolaadebimpe on 08/04/2018.
 */

public class ProfileFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView profile_image;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtFirstName)
    EditText edtFirstName;
    @BindView(R.id.edtLastName)
    EditText edtLastName;
    @BindView(R.id.tvDisplayName)
    TextView tvDisplayName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.btnUpdate)
    Button btnUpdate;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        ButterKnife.bind(this,rootView); // bind butterknife after
        Init();
        return rootView;

    }
    private void Init()
    {
        Glide
                .with(getActivity())
                .load(Uri.parse(MedManagerPreferences.getUserDetails(getContext()).getPhotoUrl()))
                .transform(new CircleTransform(getContext()))
                .placeholder(R.drawable.pill_icon)
                .into(profile_image);
        Log.d("photo",MedManagerPreferences.getUserDetails(getContext()).getPhotoUrl());
        UpdateUI();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedManagerPreferences.setUserDetails(getContext(),getString(R.string.display_name_values,edtLastName.getText().toString(),edtFirstName.getText().toString()),
                        edtFirstName.getText().toString(),edtLastName.getText().toString(),
                        edtEmail.getText().toString(),MedManagerPreferences.getUserDetails(getContext()).getId(),Uri.parse(MedManagerPreferences.getUserDetails(getContext()).getPhotoUrl()));
                        UpdateUI();
                Toast.makeText(getContext(), R.string.profile_success,Toast.LENGTH_LONG).show();
            }
        });
         }
    private void UpdateUI(){

        edtEmail.setText(MedManagerPreferences.getUserDetails(getContext()).getEmail());
        edtFirstName.setText(MedManagerPreferences.getUserDetails(getContext()).getGivenName());
        edtLastName.setText(MedManagerPreferences.getUserDetails(getContext()).getFamilyName());
        tvEmail.setText(MedManagerPreferences.getUserDetails(getContext()).getEmail());
        tvDisplayName.setText(MedManagerPreferences.getUserDetails(getContext()).getDisplayName());
    }
    public static ProfileFragment newInstance() {
        ProfileFragment fragment=new ProfileFragment();
        return fragment;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Profile");
            }
        }
    }
}
