package br.com.jacksonfgv.contatos_on.helper;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;

import br.com.jacksonfgv.contatos_on.activity.FormularioActivity;
import br.com.jacksonfgv.contatos_on.modelo.Contato;
import br.com.jacksonfgv.contatos_on.R;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class FormularioHelper {

    private final EditText nameView;
    private final EditText enderecoView;
    private final EditText telefoneView;
    private final RatingBar notaView;

    private Contato contato;

    public FormularioHelper(FormularioActivity activity){
        nameView = activity.findViewById(R.id.formulario_nome);
        enderecoView = activity.findViewById(R.id.formulario_endereco);
        telefoneView = activity.findViewById(R.id.formulario_telefone);
        notaView = activity.findViewById(R.id.formulario_nota);
        contato = new Contato();
    }

    public Contato getContato() {
        contato.setNome(nameView.getText().toString());
        contato.setEndereco(enderecoView.getText().toString());
        contato.setTelefone(telefoneView.getText().toString());
        contato.setNota(Double.valueOf(notaView.getProgress()));

        return contato;
    }

    public void preencheFormulario(Contato contato) {
        nameView.setText(contato.getNome());
        enderecoView.setText(contato.getEndereco());
        telefoneView.setText(contato.getTelefone());
        notaView.setProgress(contato.getNota().intValue());
        this.contato = contato;
    }

    public void ocutarTeclado(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(nameView.getWindowToken(), 0);
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(telefoneView.getWindowToken(), 0);
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(notaView.getWindowToken(), 0);
    }
}