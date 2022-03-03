package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reserva{
    public Reserva(String uid, String data, int event, int id, int pagades) {
        setId(id);
        setDataReserva(data);
        setClient(uid);
        setEvent(event);
        setPagades(pagades);
    }

    @JsonProperty("client")
    public String getClient() {
        return this.client; }
    public void setClient(String client) {
        this.client = client; }
    String client;
    @JsonProperty("dataReserva")
    public String getDataReserva() {
        return this.dataReserva; }
    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva; }
    String dataReserva;
    @JsonProperty("event")
    public int getEvent() {
        return this.event; }
    public void setEvent(int event) {
        this.event = event; }
    int event;
    @JsonProperty("id")
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    int id;
    @JsonProperty("pagades")
    public int getPagades() {
        return this.pagades; }
    public void setPagades(int pagades) {
        this.pagades = pagades; }
    int pagades;

    @Override
    public String toString() {
        return "Reserva{" +
                "client='" + client + '\'' +
                ", dataReserva='" + dataReserva + '\'' +
                ", event=" + event +
                ", pagades=" + pagades +
                '}';
    }
}

