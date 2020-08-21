package com.example.ouicoding.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ouicoding.Service.EmployeResponse;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSecond extends Fragment {
    private RecyclerView recycler;
    private List<EmployeResponse>employeList = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_second,container,false);
        getEmployes(view);
        return view;
    }

    private void getEmployes(final View view){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        (RetrofitClient.getClient().getEmploye()).enqueue(new Callback<List<EmployeResponse>>() {
            @Override
            public void onResponse(Call<List<EmployeResponse>> call, Response<List<EmployeResponse>> response) {
                employeList= response.body();
                recycler= view.findViewById(R.id.recycler);
                LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
                recycler.setLayoutManager(linearLayoutManager);
                CustomAdapter customAdapter= new CustomAdapter(employeList, getActivity());
                recycler.setAdapter(customAdapter);
                progressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<List<EmployeResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }

}
