package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.PicassoTrustAll;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Reserva;

import java.util.ArrayList;

public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.ViewHolder> {

    private Event event;
    protected ArrayList<Event> events;
    private ArrayList<Reserva> reservas;
    private View root;

    public ReservesAdapter(ArrayList<Reserva> reservas, ArrayList<Event> events) {
        this.reservas = reservas;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito_view, parent, false);

        return new ViewHolder(this.root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int eventId = reservas.get((reservas.size()-position)-1).getEvent();
        Event event = events.get(eventId);

        holder.tvTitol.setText(event.getNom());
        holder.tvPreu.setText(String.valueOf(reservas.get((reservas.size()-position)-1).getPagades()));
        holder.tvData.setText(String.valueOf(reservas.get((reservas.size()-position)-1).getDataReserva()));

        PicassoTrustAll.getInstance().load(event.getImatge()).into(holder.ivImatge);

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
