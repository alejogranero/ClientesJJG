package com.grafflersys.alejo.clientes;

public class Cliente {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String domicilioCobro;
    private String telefono;
    private byte[] imagen;

    public Cliente(int id, String nombre, String apellido, String dni, String domicilioCobro, String telefono, byte[] imagen) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.domicilioCobro = domicilioCobro;
        this.telefono = telefono;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDomicilioCobro() {
        return domicilioCobro;
    }

    public void setDomicilioCobro(String domicilioCobro) {
        this.domicilioCobro = domicilioCobro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
