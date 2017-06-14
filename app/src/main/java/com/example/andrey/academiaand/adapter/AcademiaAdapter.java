package com.example.andrey.academiaand.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.andrey.academiaand.AcademiaController;
import com.example.andrey.academiaand.MapsActivity;
import com.example.andrey.academiaand.R;
import com.example.andrey.academiaand.model.Academia;

import java.util.List;

/**
 * Created by Andrey on 12/06/2017.
 */

public class AcademiaAdapter extends BaseAdapter {

    private List<Academia> academias;
    private Activity activity;

    public AcademiaAdapter(List<Academia> academias, Activity activity) {
        this.academias = academias;
        this.activity = activity;
    }

    //tamanho da lista
    @Override
    public int getCount() {
        return academias.size();
    }

    //obter item pela posição
    @Override
    public Academia getItem(int position) {
        return academias.get(position);
    }

    //obtem o id de um item a partir da posição
    @Override
    public long getItemId(int position) {
        return academias.get(position).getId();
    }

    //cria a view de listagem
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Academia academia = academias.get(position);

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.content_lista_academias, null);

        TextView nome = (TextView) view.findViewById(R.id.tv_nomeAcademia);
        TextView contato = (TextView) view.findViewById(R.id.tv_contatoAcademia);
        Button btnMapa = (Button) view.findViewById(R.id.btn_mapa);
        Button btnLigar = (Button) view.findViewById(R.id.btn_ligar);

        nome.setText(academia.getName());
        contato.setText(academia.getPhone());

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcademiaController.getInstace().setAcademia(academia);
                activity.startActivity( new Intent( activity.getBaseContext() , MapsActivity.class) );
            }
        });
        btnLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity( new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + academia.getPhone())));
            }
        });

        return view;
    }
}
