package com.example.handshaker.ui.Mensaje;

public class Mensaje {

  public  String destinatario;
  public  String emisor;
  public String mensaje;

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Mensaje(String destinatario, String emisor, String mensaje) {
        this.destinatario = destinatario;
        this.emisor = emisor;
        this.mensaje = mensaje;
    }
}
