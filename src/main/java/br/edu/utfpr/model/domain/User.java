package br.edu.utfpr.model.domain;

import br.edu.utfpr.util.Sha256Generator;

import javax.persistence.*;

/**
 * Created by ronifabio on 01/05/2019.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "login")
    private String email;

    private String name;

    @Column(name = "pwd")
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @PrePersist
    @PreUpdate
    public void onSave() {

//        if (this.password == null) {
//            return;
//        }
//
//        final String hashed = Sha256Generator.generate(this.password);
//        this.setPassword(hashed);
    }
}
