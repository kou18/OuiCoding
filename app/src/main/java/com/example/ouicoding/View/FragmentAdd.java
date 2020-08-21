package com.example.ouicoding.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ouicoding.Model.Contrat;
import com.example.ouicoding.R;
import com.example.ouicoding.Service.RetrofitClient;
import com.example.ouicoding.Service.SignUpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentAdd extends Fragment implements AdapterView.OnItemSelectedListener {

    private EditText nom,prenom,entreprise,email,mdp,confirmmdp,debut,fin,id;
    private Button register;
    private SignUpResponse signUpResponsesData;
    private Spinner spinner;
    private Contrat contrat;
    private String typeContrat;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add,container,false);
        nom = view.findViewById(R.id.nom2);
        prenom = view.findViewById(R.id.prenom2);
        entreprise = view.findViewById(R.id.entreprise2);
        email = view.findViewById(R.id.remail2);
        mdp = view.findViewById(R.id.rpassword2);
        confirmmdp = view.findViewById(R.id.confirmpassword2);
        register=view.findViewById(R.id.ajouter);
        spinner=view.findViewById(R.id.contrat);
        debut=view.findViewById(R.id.datedebut);
        fin=view.findViewById(R.id.datefin);
        id=view.findViewById(R.id.id);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(), R.array.contrat,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        contrat = new Contrat();
        contrat.setType(typeContrat);
        contrat.setDateDebut(debut.getText().toString());
        contrat.setDateFin(debut.getText().toString());

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confpass=confirmmdp.getText().toString().trim();
                String pass=mdp.getText().toString().trim();
                if(validate(nom) && validate (prenom) && validateEmail() && validate(mdp)){
                    if(confpass.equals(pass)){
                        signUp();
                    }
                    else{
                        mdp.setText("");
                        confirmmdp.setText("");
                        mdp.requestFocus();
                        mdp.setError("Your password doesn't match");
                    }

                }
            }
        });


        return view;
    }

    private boolean validateEmail(){
        String emailtext=email.getText().toString().trim();
        if (emailtext.isEmpty() || !isValidEmail(emailtext)){
            email.setError("Email is not valid");
            email.requestFocus();
            return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validate(EditText editText){
        if(editText.getText().toString().trim().length()>0){
            return true;
        }
        editText.setError("Please fill this field");
        editText.requestFocus();
        return false;
    }

    private void signUp(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        (RetrofitClient.getClient().registration2(nom.getText().toString().trim(),
                prenom.getText().toString().trim(),
                entreprise.getText().toString().trim(),
                id.getText().toString().trim(),
                typeContrat,
                debut.getText().toString().trim(),
                fin.getText().toString().trim(),
                email.getText().toString().trim(),
                mdp.getText().toString().trim()


        )).enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                signUpResponsesData=response.body();
                Toast.makeText(getActivity().getApplicationContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("response",t.getStackTrace().toString());
                progressDialog.dismiss();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        typeContrat= adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
