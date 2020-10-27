package fr.r_thd.player.adapter;

import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.r_thd.player.R;
import fr.r_thd.player.activity.PlaylistActivity;
import fr.r_thd.player.dialog.MusicEditDialog;
import fr.r_thd.player.dialog.UpdatableFromDialog;
import fr.r_thd.player.model.Playlist;

/**
 * Adapter de musique
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
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
        private final TextView title;

        private ImageView editButton;

        private ImageView deleteButton;

        /**
         * Constructeur
         *
         * @param itemView Item
         */
        public MusicHolder(@NonNull View itemView, final MusicAdapterListener listener) {
            super(itemView);
            preview = itemView.findViewById(R.id.item_preview);
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });

            title = itemView.findViewById(R.id.item_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });

            editButton = itemView.findViewById(R.id.item_edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditButtonClick(getAdapterPosition());
                }
            });

            deleteButton = itemView.findViewById(R.id.item_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteButtonClick(getAdapterPosition());
                }
            });
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
    private final Playlist playlist;

    private final MusicAdapterListener listener;

    public MusicAdapter(Playlist playlist, MusicAdapterListener listener) {
        this.playlist = playlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicAdapter.MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_elem, parent, false);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick();
                return true;
            }
        });

        return new MusicAdapter.MusicHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicHolder holder, final int position) {
        holder.title.setText(playlist.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }
}
