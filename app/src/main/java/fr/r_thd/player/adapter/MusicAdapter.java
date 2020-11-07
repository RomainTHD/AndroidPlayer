package fr.r_thd.player.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.objects.Music;

/**
 * Adapter de musique
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> implements Filterable {
    /**
     * Holder de view
     */
    static class MusicHolder extends RecyclerView.ViewHolder {
        /**
         * Preview
         */
        private final ImageView preview;

        /**
         * Titre
         */
        private final TextView title;

        /**
         * Constructeur
         *
         * @param musicAdapter Adapter
         * @param itemView Item
         * @param listener Listener
         */
        public MusicHolder(@NonNull final MusicAdapter musicAdapter, @NonNull View itemView, @NonNull final AdapterListener listener) {
            super(itemView);

            preview = itemView.findViewById(R.id.item_preview);
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(musicAdapter.getTruePos(getAdapterPosition()));
                }
            });

            title = itemView.findViewById(R.id.item_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(musicAdapter.getTruePos(getAdapterPosition()));
                }
            });

            ImageView editButton = itemView.findViewById(R.id.item_edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditButtonClick(musicAdapter.getTruePos(getAdapterPosition()));
                }
            });

            ImageView deleteButton = itemView.findViewById(R.id.item_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteButtonClick(musicAdapter.getTruePos(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Playlist associée full
     */
    private final List<Music> playlistFull;

    /**
     * Playlist associée filtrée
     */
    private final List<Music> playlistCurrent;

    /**
     * Listener
     */
    private final AdapterListener listener;

    /**
     * Filtre
     */
    private final Filter filter;

    /**
     * Constructeur
     *
     * @param playlist Playlist
     * @param listener Listener
     */
    public MusicAdapter(@NonNull final List<Music> playlist, @NonNull AdapterListener listener) {
        this.playlistFull = playlist;
        this.playlistCurrent = new ArrayList<>(playlist);
        this.listener = listener;
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Music> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(playlistFull);
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Music music : playlistFull) {
                        if (music.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(music);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                playlistCurrent.clear();
                playlistCurrent.addAll((List<Music>) results.values);
                notifyDataSetChanged();
            }
        };
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

        return new MusicAdapter.MusicHolder(this, view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicHolder holder, final int position) {
        holder.title.setText(playlistCurrent.get(position).getTitle());
        holder.preview.setImageBitmap(playlistCurrent.get(position).getPicture());
    }

    @Override
    public int getItemCount() {
        return playlistCurrent.size();
    }

    @Override
    @NonNull
    public Filter getFilter() {
        return filter;
    }

    public void add(@NonNull Music m) {
        playlistCurrent.add(m);
        playlistFull.add(m);
    }

    public void remove(int i) {
        playlistCurrent.remove(playlistFull.get(i));
        playlistFull.remove(i);
    }

    private int getTruePos(int pos) {
        Music music = playlistCurrent.get(pos);
        return playlistFull.indexOf(music);
    }
}
