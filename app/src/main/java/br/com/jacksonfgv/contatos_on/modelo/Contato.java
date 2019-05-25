package br.com.jacksonfgv.contatos_on.modelo;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.jacksonfgv.contatos_on.helper.Base64Custom;
import br.com.jacksonfgv.contatos_on.config.ConfiguracaoFirebase;

import static android.content.ContentValues.TAG;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class Contato implements Serializable {

    private String id;
    private String nome;
    private String telefone;
    private String endereco;
    private Double nota;

    public Contato() {

    }

    public void salvar(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child( idUsuario )
                .child( "contatos" )
                .push()
                .setValue(this);

    }

    public void atualizar() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child( idUsuario )
                .child( "contatos" )
                .child( this.getId() )
                .setValue(this);
    }

    public void deletar() {
        Log.i(TAG, "deletar: "+this.getId());
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64( autenticacao.getCurrentUser().getEmail() );

        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child( idUsuario )
                .child( "contatos" )
                .child( this.getId() ).setValue( null );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return this.getNome();
    }
}
