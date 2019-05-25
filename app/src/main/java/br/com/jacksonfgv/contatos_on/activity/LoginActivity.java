package br.com.jacksonfgv.contatos_on.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.jacksonfgv.contatos_on.R;
import br.com.jacksonfgv.contatos_on.component.MyProgressBar;
import br.com.jacksonfgv.contatos_on.config.ConfiguracaoFirebase;
import br.com.jacksonfgv.contatos_on.modelo.Usuario;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class LoginActivity extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;

    private Button btLoginView, registerBtView;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login");

        // Configurar o formulário de login.
        emailView = findViewById(R.id.email_login);
        passwordView = findViewById(R.id.password_login);
        btLoginView = findViewById(R.id.loginBt_login);
        registerBtView = findViewById(R.id.registerBt_login);


        btLoginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoEmail = emailView.getText().toString();
                String textoSenha = passwordView.getText().toString();

                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {

                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setPassword(textoSenha);
                        validarLogin();

                    } else {
                        Toast.makeText(LoginActivity.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Preencha o email!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTelaCadastro();
            }
        });
    }

    public void validarLogin(){
        ocutarTeclado();
        dialog = MyProgressBar.abrirDialogCarregamento("Carregando...", this);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){

                    abrirTelaPrincipal();

                }else {

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário não está cadastrado.";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    dialog.cancel();

                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirTelaCadastro() {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this, ListaContatosActivity.class));
        finish();
    }

    public void ocutarTeclado() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(emailView.getWindowToken(), 0);
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(passwordView.getWindowToken(), 0);
    }


}

