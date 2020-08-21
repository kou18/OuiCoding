package com.example.ouicoding.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ouicoding.Service.ContratResponse;
import com.example.ouicoding.Model.User;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSecondEmploye extends Fragment {
    TextView type,ddc,dfc;
    ContratResponse contratResponseData;
    User mUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_second_employe,container,false);
        type=view.findViewById(R.id.typeex);
        ddc=view.findViewById(R.id.ddcex);
        dfc=view.findViewById(R.id.dfcex);
        mUser=MainActivity.getUser();
        Log.d("tag", "value= "+mUser.getId());
        getContrat();
        return view;
    }

    private void getContrat() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog


        (RetrofitClient.getClient().getContrat(
                mUser.getId()
        )).enqueue(new Callback<ContratResponse>() {
            @Override
            public void onResponse(Call<ContratResponse> call, Response<ContratResponse> response) {
                progressDialog.dismiss(); //dismiss progress dialog
                contratResponseData = response.body();
                type.setText(contratResponseData.getType());
                ddc.setText(contratResponseData.getDebut());
                dfc.setText(contratResponseData.getFin());

            }

            @Override
            public void onFailure(Call<ContratResponse> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }
}
