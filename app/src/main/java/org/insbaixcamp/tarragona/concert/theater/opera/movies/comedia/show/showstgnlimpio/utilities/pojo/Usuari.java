package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Usuari{
    @JsonProperty("cognom")
    public String getCognom() {
        return this.cognom; }
    public void setCognom(String cognom) {
        this.cognom = cognom; }
    String cognom;
    @JsonProperty("contrasenya")
    public String getContrasenya() {
        return this.contrasenya; }
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya; }
    String contrasenya;
    @JsonProperty("correu")
    public String getCorreu() {
        return this.correu; }
    public void setCorreu(String correu) {
        this.correu = correu; }
    String correu;
    @JsonProperty("dni")
    public String getDni() {
        return this.dni; }
    public void setDni(String dni) {
        this.dni = dni; }
    String dni;
    @JsonProperty("nom")
    public String getNom() {
        return this.nom; }
    public void setNom(String nom) {
        this.nom = nom; }
    String nom;
    @JsonProperty("telefon")
    public int getTelefon() {
        return this.telefon; }
    public void setTelefon(int telefon) {
        this.telefon = telefon; }
    int telefon;

    @Override
    public String toString() {
        return "Usuari{" +
                "cognom='" + cognom + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", correu='" + correu + '\'' +
                ", dni='" + dni + '\'' +
                ", nom='" + nom + '\'' +
                ", telefon=" + telefon +
                '}';
    }
}
