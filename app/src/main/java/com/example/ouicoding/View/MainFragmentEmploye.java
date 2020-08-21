package com.example.ouicoding.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import static android.content.Context.MODE_PRIVATE;
import static com.example.ouicoding.View.MainActivity.PREF_EMAIL;
import static com.example.ouicoding.View.MainActivity.PREF_FIRSTNAME;
import static com.example.ouicoding.View.MainActivity.PREF_NAME;

public class MainFragmentEmploye extends Fragment {
    TextView nom,prenom,email;
    EditText mdp, confmdp;
    String mdptext, confirmmdptext,emailtext;
    Button modify;
    SharedPreferences preferences;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main_employe,container,false);

        nom=view.findViewById(R.id.mainnome);
        prenom=view.findViewById(R.id.mainprenome);
        email=view.findViewById(R.id.mainemaile);
        mdp=view.findViewById(R.id.nvmdpe);
        confmdp=view.findViewById(R.id.confirmnvmdpe);
        modify=view.findViewById(R.id.modifiermdpe);
        preferences= this.getActivity().getSharedPreferences("SaveData",MODE_PRIVATE);
        mdptext=mdp.getText().toString().trim();
        confirmmdptext=confmdp.getText().toString().trim();
        emailtext=email.getText().toString().trim();

        nom.setText("Nom: "+preferences.getString(PREF_NAME,""));
        prenom.setText("Prénom: "+preferences.getString(PREF_FIRSTNAME,""));
        emailtext= preferences.getString(PREF_EMAIL,"");
        email.setText("Email: "+emailtext);

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdptext=mdp.getText().toString().trim();
                confirmmdptext=confmdp.getText().toString().trim();
                if(mdptext.equals(confirmmdptext)) {
                    UpdateMdp();
                }
                else
                {
                    mdp.setText("");
                    confmdp.setText("");
                    mdp.requestFocus();
                    mdp.setError("Your password doesn't match");
                }

            }
        });

        return view;
    }

    private void UpdateMdp(){

            Call<CongeResponse> call = RetrofitClient.getClient().updateMdp(mdptext,emailtext);
            call.enqueue(new Callback<CongeResponse>() {
                @Override
                public void onResponse(Call<CongeResponse> call, Response<CongeResponse> response) {
                    Toast.makeText(getActivity().getApplicationContext(), response.body().getResponse(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<CongeResponse> call, Throwable t) {
                    Toast.makeText(getActivity().getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }


}
