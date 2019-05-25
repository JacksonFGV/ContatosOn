package br.com.jacksonfgv.contatos_on.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import br.com.jacksonfgv.contatos_on.R;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class MyProgressBar {

    private static AlertDialog dialog;

    public static AlertDialog abrirDialogCarregamento(String titulo, Activity a) {

        LayoutInflater inflater = (LayoutInflater) a.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View viewCarregando = (View) inflater.inflate(R.layout.component_carregamento, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(a);
        alert.setTitle( titulo);
        alert.setCancelable( false );
        alert.setView( viewCarregando  );

        dialog = alert.create();
        dialog.show();

        return dialog;

    }

    public static AlertDialog messageDialog(Activity a, String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(a);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setNeutralButton("OK", null);

        dialog = alert.create();
        dialog.show();

        return dialog;

    }

}
