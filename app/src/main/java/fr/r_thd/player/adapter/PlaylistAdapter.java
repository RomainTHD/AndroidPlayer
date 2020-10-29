package fr.r_thd.player.adapter;

import fr.r_thd.player.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.r_thd.player.objects.Playlist;

public abstract class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistHolder> {
    static class PlaylistHolder extends RecyclerView.ViewHolder {
        private ImageView preview;
        private TextView title;

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

    private final List<Playlist> playlistList;

    public PlaylistAdapter(List<Playlist> playlistList) {
        this.playlistList = playlistList;
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
        holder.title.setText(playlistList.get(position).getName());
        // TODO:
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }

    public abstract void onItemClick(View v);

    public abstract boolean onItemLongClick(View v);
}
