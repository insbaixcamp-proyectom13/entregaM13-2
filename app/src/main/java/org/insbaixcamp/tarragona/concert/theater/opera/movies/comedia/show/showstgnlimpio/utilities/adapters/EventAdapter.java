package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.PicassoTrustAll;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Event;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements View.OnClickListener {

    protected ArrayList<Event> events;
    private View root;

    public EventAdapter( ArrayList<Event> events) {
        this.events = events;
    }
    private View.OnClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);

        root.setOnClickListener(this);

        return new ViewHolder(this.root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitol.setText(events.get(position).getNom());
        holder.tvPreu.setText(events.get(position).getPreu());
        holder.tvData.setText(events.get(position).getData());

        PicassoTrustAll.getInstance().load(events.get(position).getImatge()).into(holder.ivImatge);
        holder.ivImatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final NavController nav = Navigation.findNavController(view);
//                Bundle bundle = new Bundle();
//                Log.i("position", events.get(position).getNom());
//                bundle.putSerializable("event", events.get(position));
//                nav.navigate(R.id.nav_event, bundle);
            }
        });
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }
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
