package com.example.ouicoding.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ouicoding.Service.EmployeResponse;
import com.example.ouicoding.R;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private List<EmployeResponse> item;
    private Context context;

    public CustomAdapter(List<EmployeResponse> item, Context context) {
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_layout, null);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.id.setText("Identificateur: "+item.get(position).getId());
        holder.name.setText("Nom: "+item.get(position).getNom());
        holder.firstname.setText("Prénom: "+item.get(position).getPrenom());
        holder.email.setText("Email: "+item.get(position).getEmail());
        holder.type.setText("Type du Contrat: "+ item.get(position).getType());
        holder.debut.setText("Debut du Contrat: "+ item.get(position).getDebut());
        holder.fin.setText("Fin du Contrat: "+ item.get(position).getFin());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id,name,firstname,email,type,debut,fin;
        public ViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            id = (TextView) itemView.findViewById(R.id.ident);
            name = (TextView) itemView.findViewById(R.id.name);
            firstname = (TextView) itemView.findViewById(R.id.firstname);
            email = (TextView) itemView.findViewById(R.id.mail);
            type= (TextView) itemView.findViewById(R.id.typerec);
            debut= (TextView) itemView.findViewById(R.id.debutrec);
            fin= (TextView) itemView.findViewById(R.id.finrec);
        }
    }

}