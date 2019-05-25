package br.com.jacksonfgv.contatos_on.activity;

import android.content.Intent;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import br.com.jacksonfgv.contatos_on.component.MyProgressBar;
import br.com.jacksonfgv.contatos_on.helper.FormularioHelper;
import br.com.jacksonfgv.contatos_on.modelo.Contato;
import br.com.jacksonfgv.contatos_on.R;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class FormularioActivity extends AppCompatActivity {

    private FormularioHelper helper;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Contato contato = (Contato) intent.getSerializableExtra("contato");

        if(contato != null){
            helper.preencheFormulario(contato);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:
                helper.ocutarTeclado(this);
                dialog = MyProgressBar.abrirDialogCarregamento("Salvando...", this);
                Contato contato = helper.getContato();

                if(contato.getId() != null){
                    contato.atualizar();
                }
                else{
                    contato.salvar();
                }

                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                dialog.cancel();

                Toast.makeText(FormularioActivity.this, "Contato " + contato.getNome() + " salvo!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}