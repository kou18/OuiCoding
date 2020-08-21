package com.example.ouicoding.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ouicoding.Service.CongeResponse;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentThird extends Fragment {

    private TextView email,nbjours;
    private Button modifier;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_third,container,false);
        email=view.findViewById(R.id.emailconge);
        nbjours=view.findViewById(R.id.nbjours);
        modifier=view.findViewById(R.id.modifier);

        modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifier();
            }
        });


        return view;
    }

    private void modifier(){
        Call<CongeResponse> call = RetrofitClient.getClient().updateConge(
                email.getText().toString().trim(),
                nbjours.getText().toString().trim()
        );

        call.enqueue(new Callback<CongeResponse>() {
            @Override
            public void onResponse(Call<CongeResponse> call, Response<CongeResponse> response) {
                Toast.makeText(getActivity(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CongeResponse> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
