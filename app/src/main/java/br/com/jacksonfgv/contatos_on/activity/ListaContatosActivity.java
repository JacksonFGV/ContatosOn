package br.com.jacksonfgv.contatos_on.activity;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.jacksonfgv.contatos_on.component.MyProgressBar;
import br.com.jacksonfgv.contatos_on.helper.Base64Custom;
import br.com.jacksonfgv.contatos_on.R;
import br.com.jacksonfgv.contatos_on.config.ConfiguracaoFirebase;
import br.com.jacksonfgv.contatos_on.modelo.Contato;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class ListaContatosActivity extends AppCompatActivity {

    private ListView listaContatos;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference contatosRef;
    private ValueEventListener valueEventListenerContatos;

    private Contato contato;
    private ArrayList<Contato> contatos = new ArrayList<>();
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        listaContatos = findViewById(R.id.lista_contatos);

        listaContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);
                Intent intentVaiProFormulario = new Intent(ListaContatosActivity.this, FormularioActivity.class);
                intentVaiProFormulario.putExtra("contato", contato);
                startActivity(intentVaiProFormulario);
            }
        });

        Button novoUsuario = findViewById(R.id.btNewContact);
        novoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVaiProFormulario = new Intent(ListaContatosActivity.this, FormularioActivity.class);
                startActivity(intentVaiProFormulario);
            }
        });

        registerForContextMenu(listaContatos);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //carregaLista();
    }


    private void carregaLista() {

        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(this, android.R.layout.simple_list_item_1, this.contatos);
        listaContatos.setAdapter(adapter);

        listaContatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = (Contato) listaContatos.getItemAtPosition(position);
                excluirContato(contato);
                return true;
            }
        });
    }

    public void recuperarContatos(){

        dialog = MyProgressBar.abrirDialogCarregamento("Carregando contatos...", this);

        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64( emailUsuario );
        contatosRef = firebaseRef.child("usuarios")
                .child( idUsuario )
                .child( "contatos" );

        valueEventListenerContatos = contatosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contatos.clear();
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Contato contato = dados.getValue( Contato.class );
                    contato.setId( dados.getKey() );
                    contatos.add( contato );

                }

                carregaLista();
                dialog.cancel();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public void excluirContato(final Contato contato){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Contato");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir esse contato?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                contato.deletar();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListaContatosActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                //adapterMovimentacao.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair :
                autenticacao.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarContatos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        contatosRef.removeEventListener( valueEventListenerContatos );
    }

}