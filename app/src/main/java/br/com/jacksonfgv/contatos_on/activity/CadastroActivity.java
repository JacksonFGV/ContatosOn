package br.com.jacksonfgv.contatos_on.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.jacksonfgv.contatos_on.helper.Base64Custom;
import br.com.jacksonfgv.contatos_on.R;
import br.com.jacksonfgv.contatos_on.config.ConfiguracaoFirebase;
import br.com.jacksonfgv.contatos_on.component.MyProgressBar;
import br.com.jacksonfgv.contatos_on.modelo.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class CadastroActivity extends AppCompatActivity {

    private EditText nameView, emailView, passwordView;
    private Button registerBtView, loginBtView;
    private FirebaseAuth autenticacao;
    private AlertDialog dialog;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        setTitle("Cadastro");

        nameView = findViewById(R.id.name_cadastro);
        emailView = findViewById(R.id.email_cadastro);
        passwordView = findViewById(R.id.password_cadastro);
        registerBtView = findViewById(R.id.registerBt_cadastro);
        loginBtView = findViewById(R.id.loginBt_cadastro);

        registerBtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoNome = nameView.getText().toString();
                String textoEmail = emailView.getText().toString();
                String textoSenha = passwordView.getText().toString();

                //Validar se os campos foram preenchidos
                if ( !textoNome.isEmpty() ){
                    if ( !textoEmail.isEmpty() ){
                        if ( !textoSenha.isEmpty() ){

                            usuario = new Usuario();
                            usuario.setNome( textoNome );
                            usuario.setEmail( textoEmail );
                            usuario.setPassword( textoSenha );
                            cadastrarUsuario();

                        }else {
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha a senha!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CadastroActivity.this,
                                "Preencha o email!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o nome!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        loginBtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaLogin();
            }
        });

    }

    public void cadastrarUsuario(){

        this.ocutarTeclado();
        dialog = MyProgressBar.abrirDialogCarregamento("Cadastrando...", this);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword( usuario.getEmail(), usuario.getPassword() )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {



                if ( task.isSuccessful() ){

                    String idUsuario = Base64Custom.codificarBase64( usuario.getEmail() );
                    usuario.setIdUsuario( idUsuario );
                    usuario.salvar();
                    dialog.cancel();
                    abrirTelaPrincipal();
                    finish();

                }else {

                    String excecao = "Bem vindo!";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    dialog.cancel();

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void abrirTelaLogin() {
        finish();
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(CadastroActivity.this, ListaContatosActivity.class));
        finish();
    }

    public void ocutarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(nameView.getWindowToken(), 0);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(emailView.getWindowToken(), 0);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(passwordView.getWindowToken(), 0);
    }

}
