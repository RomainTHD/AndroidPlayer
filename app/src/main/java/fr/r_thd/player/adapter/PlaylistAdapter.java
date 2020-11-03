package fr.r_thd.player.adapter;

import fr.r_thd.player.R;

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

import fr.r_thd.player.objects.Playlist;

/**
 * Adapter de playlist
 */
public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> implements Filterable {
    static class PlaylistHolder extends RecyclerView.ViewHolder {
        private final ImageView preview;
        private final TextView title;

        public PlaylistHolder(final PlaylistAdapter playlistAdapter, @NonNull View itemView, final AdapterListener listener) {
            super(itemView);

            preview = itemView.findViewById(R.id.item_preview);
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(playlistAdapter.getTruePos(getAdapterPosition()));
                }
            });

            title = itemView.findViewById(R.id.item_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(playlistAdapter.getTruePos(getAdapterPosition()));
                }
            });

            ImageView editButton = itemView.findViewById(R.id.item_edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditButtonClick(playlistAdapter.getTruePos(getAdapterPosition()));
                }
            });

            ImageView deleteButton = itemView.findViewById(R.id.item_delete_button);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteButtonClick(playlistAdapter.getTruePos(getAdapterPosition()));
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

    private final List<Playlist> playlistListFull;

    private List<Playlist> playlistListCurrent;

    private final AdapterListener listener;

    private final Filter filter;

    public PlaylistAdapter(List<Playlist> playlistList, AdapterListener listener) {
        this.playlistListFull = new ArrayList<>(playlistList);
        this.playlistListCurrent = playlistList;
        this.listener = listener;
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Playlist> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(playlistListFull);
                }
                else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Playlist playlist : playlistListFull) {
                        if (playlist.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(playlist);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                playlistListCurrent.clear();
                playlistListCurrent.addAll((List<Playlist>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public PlaylistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_list, parent, false);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick();
                return true;
            }
        });

        return new PlaylistHolder(this, view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistHolder holder, int position) {
        holder.title.setText(playlistListCurrent.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return playlistListCurrent.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void add(Playlist p) {
        playlistListFull.add(p);
    }

    public void remove(int i) {
        playlistListFull.remove(i);
    }

    private int getTruePos(int pos) {
        Playlist playlist = playlistListCurrent.get(pos);
        return playlistListFull.indexOf(playlist);
    }
}
