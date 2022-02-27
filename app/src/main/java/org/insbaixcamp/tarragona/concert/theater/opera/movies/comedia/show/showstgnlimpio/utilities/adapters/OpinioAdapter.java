package org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.R;
import org.insbaixcamp.tarragona.concert.theater.opera.movies.comedia.show.showstgnlimpio.utilities.pojo.Opinio;

import java.util.ArrayList;

public class OpinioAdapter extends RecyclerView.Adapter<OpinioAdapter.ViewHolder> {

    private ArrayList<Opinio> opinions;
    private View root;

    public OpinioAdapter(ArrayList<Opinio> opinions) {
        this.opinions = opinions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.root = LayoutInflater.from(parent.getContext()).inflate(R.layout.opinio_view, parent, false);
        return new ViewHolder(this.root);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvUsername.setText(opinions.get(position).getNomUsuari());
        holder.tvData.setText(opinions.get(position).getData());
        holder.tvOpinio.setText(opinions.get(position).getOpinio());
        holder.ivUser.setImageResource(R.drawable.teatro);
    }

    @Override
    public int getItemCount() {
        return opinions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvOpinio;
        TextView tvData;
        ImageView ivUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOpinio = itemView.findViewById(R.id.tvOpinio);
            tvUsername = itemView.findViewById(R.id.tvUsernameComment);
            tvData = itemView.findViewById(R.id.tvDataComment);
            ivUser = itemView.findViewById(R.id.ivUserComment);
        }
    }
}
