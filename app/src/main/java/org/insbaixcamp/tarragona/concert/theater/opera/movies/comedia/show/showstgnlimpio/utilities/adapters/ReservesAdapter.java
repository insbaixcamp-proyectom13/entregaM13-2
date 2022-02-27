package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.PicassoTrustAll;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;

import java.util.ArrayList;

public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {

    protected ArrayList<Event> events;
    private ArrayList<Reserva> reservas;
    private View root;
//    private FirebaseConnection fb;


    public ReservesAdapter(ArrayList<Reserva> reservas, ArrayList<Event> events) {
        this.events = events;
        this.reservas = reservas;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito_view, parent, false);

        return new ViewHolder(this.root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = getEvent(reservas.get(position).getEvent());
        Log.i("event", event.toString());
//        if (event != null) {
            holder.tvTitol.setText(event.getNom());
            holder.tvPreu.setText(String.valueOf(reservas.get(position).getPagades()));
            holder.tvData.setText(String.valueOf(reservas.get(position).getDataReserva()));

            PicassoTrustAll.getInstance().load(event.getImatge()).into(holder.ivImatge);
//        }

//        holder.tvTitol.setText(events.get(position).getNom());
//        holder.tvPreu.setText(String.valueOf(reservas.get(position).getPagades()));
//        holder.tvData.setText(String.valueOf(reservas.get(position).getDataReserva()));
//
//        PicassoTrustAll.getInstance().load(events.get(position).getImatge()).into(holder.ivImatge);

    }

    private Event getEvent(int id) {
        Event newEvent = null;
        for (Event event : events) {
            if (event.getId() == id) {
                newEvent = event;
            }
        }
        return newEvent;
    }

    private Reserva getReserva(int position) {
        for (Reserva reserva : reservas) {
            if (reserva.getId() == events.get(position).getId()) {
                return reserva;
            }
        }
        return null;
    }


    @Override
    public long getItemId(int position) {
        return reservas.get(position).getEvent();
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitol;
        TextView tvPreu;
        TextView tvData;
        ImageView ivImatge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitol = (TextView) itemView.findViewById(R.id.tv_title);
            tvPreu = (TextView) itemView.findViewById(R.id.tv_preu);
            tvData = (TextView) itemView.findViewById(R.id.tv_descripcio);
            ivImatge = (ImageView) itemView.findViewById(R.id.iv_imatge);
        }
    }
}
