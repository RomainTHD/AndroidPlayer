package fr.r_thd.player.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.r_thd.player.R;
import fr.r_thd.player.model.Playlist;

/**
 * Adapter de musique
 */
public abstract class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    /**
     * Holder de view
     */
    static class MusicHolder extends RecyclerView.ViewHolder {
        /**
         * Preview
         */
        private ImageView preview;

        /**
         * Titre
         */
        private TextView title;

        /**
         * Constructeur
         *
         * @param itemView Item
         */
        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            preview = itemView.findViewById(R.id.item_preview);
            title = itemView.findViewById(R.id.item_title);
        }

        public ImageView getPreview() {
            return preview;
        }

        public TextView getTitle() {
            return title;
        }
    }

    /**
     * Playlist associée
     */
    private Playlist playlist;

    public MusicAdapter(Playlist playlist) {
        this.playlist = playlist;
    }

    @NonNull
    @Override
    public MusicAdapter.MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_elem, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicAdapter.this.onItemClick(v);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return MusicAdapter.this.onItemLongClick(v);
            }
        });

        return new MusicAdapter.MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicHolder holder, int position) {
        holder.title.setText(playlist.get(position).getTitle());
        // TODO:
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    /**
     * On item click
     */
    public abstract void onItemClick(View v);

    /**
     * On item long click
     */
    public abstract boolean onItemLongClick(View v);
}
