package com.example.szakdolgozat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class listaadapter extends ArrayAdapter {

    private ArrayList<String> elsokep = new ArrayList<>();
    private ArrayList<String> masodikkep = new ArrayList<>();
    private ArrayList<String> jelentkezettneve = new ArrayList<>();
    private Activity context;

    public listaadapter(Activity context, ArrayList<String> elsokep,ArrayList<String> masodikkep,ArrayList<String> jelentkezettneve){
        super(context, R.layout.alairasokme, elsokep);
        this.context = context;
        this.elsokep = elsokep;
        this.masodikkep = masodikkep;
        this.jelentkezettneve = jelentkezettneve;
    }

    @Override
    public View getView(int pozicio, View view, ViewGroup viewGroup){
     View sor = view;
        LayoutInflater layoutInflater = context.getLayoutInflater();
        if (view == null)
            sor = layoutInflater.inflate(R.layout.alairasokme, null, true);
            ImageView elsokepkep = (ImageView) sor.findViewById(R.id.elsokep);
            ImageView masodikepkep = (ImageView) sor.findViewById(R.id.masodikkep);
            TextView hallgatoneptun = (TextView) sor.findViewById(R.id.emberneve);

            Picasso.get().load(elsokep.get(pozicio)).into(elsokepkep);
            Picasso.get().load(masodikkep.get(pozicio)).into(masodikepkep);
            hallgatoneptun.setText(jelentkezettneve.get(pozicio));
            return sor;
    }
}
