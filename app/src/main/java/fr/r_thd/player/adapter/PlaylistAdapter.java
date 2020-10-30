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

public abstract class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> implements Filterable {
    static class PlaylistHolder extends RecyclerView.ViewHolder {
        private final ImageView preview;
        private final TextView title;

        public PlaylistHolder(@NonNull View itemView) {
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

    private final List<Playlist> playlistListFull;

    private List<Playlist> playlistListCurrent;

    private final Filter filter;

    public PlaylistAdapter(List<Playlist> playlistList) {
        this.playlistListFull = new ArrayList<>(playlistList);
        this.playlistListCurrent = playlistList;
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistAdapter.this.onItemClick(v);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return PlaylistAdapter.this.onItemLongClick(v);
            }
        });

        return new PlaylistHolder(view);
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

    public abstract void onItemClick(View v);

    public abstract boolean onItemLongClick(View v);
}
