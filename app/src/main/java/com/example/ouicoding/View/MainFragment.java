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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.ouicoding.View.MainActivity.PREF_EMAIL;
import static com.example.ouicoding.View.MainActivity.PREF_FIRSTNAME;
import static com.example.ouicoding.View.MainActivity.PREF_NAME;

public class MainFragment extends Fragment {
    FloatingActionButton add;
    TextView nom,prenom,email;
    EditText mdp, confmdp;
    String mdptext, confirmmdptext,emailtext;
    Button modify;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_main,container,false);
        add= view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentAdd nextFrag= new FragmentAdd();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        preferences= this.getActivity().getSharedPreferences("SaveData",MODE_PRIVATE);
        nom=view.findViewById(R.id.mainnom);
        prenom=view.findViewById(R.id.mainprenom);
        email=view.findViewById(R.id.mainemail);
        mdp=view.findViewById(R.id.nvmdp);
        confmdp=view.findViewById(R.id.confirmnvmdp);
        modify=view.findViewById(R.id.modifiermdp);
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
