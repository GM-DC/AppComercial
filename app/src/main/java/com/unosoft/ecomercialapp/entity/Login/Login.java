package com.unosoft.ecomercialapp.entity.Login;

public class Login {
    private String usuariomozo;
    private String passmozo;

    public Login(String usuariomozo, String passmozo) {
        this.usuariomozo = usuariomozo;
        this.passmozo = passmozo;
    }

    public String getUsuariomozo() {
        return usuariomozo;
    }

    public void setUsuariomozo(String usuariomozo) {
        this.usuariomozo = usuariomozo;
    }

    public String getPassmozo() {
        return passmozo;
    }

    public void setPassmozo(String passmozo) {
        this.passmozo = passmozo;
    }
}
