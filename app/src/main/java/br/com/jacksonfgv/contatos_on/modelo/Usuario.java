package br.com.jacksonfgv.contatos_on.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;

import br.com.jacksonfgv.contatos_on.config.ConfiguracaoFirebase;

/**
 * Created by Jackson Fernandes
 * @JacksonFGV
 */

public class Usuario implements Serializable {

    private String idUsuario;
    private String nome;
    private String email;
    private  String password;


    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebase.child("usuarios")
                .child( this.idUsuario )
                .setValue( this );
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getIdUsuario() + " - " + getNome();
    }

}

