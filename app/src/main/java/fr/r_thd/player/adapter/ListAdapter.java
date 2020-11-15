package fr.r_thd.player.adapter;

import android.graphics.Bitmap;
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

/**
 * Adapter générique
 */
public class ListAdapter<T> extends RecyclerView.Adapter<ListAdapter.ItemHolder<T>> implements Filterable {
    /**
     * Holder de vue
     */
    static class ItemHolder<T> extends RecyclerView.ViewHolder {
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
         * @param listAdapter Adapter
         * @param itemView Item
         * @param listener Listener
         */
        public ItemHolder(@NonNull final ListAdapter<T> listAdapter, @NonNull View itemView, @NonNull final AdapterListener<T> listener) {
            super(itemView);

            preview = itemView.findViewById(R.id.item_preview);
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(listAdapter.getTruePos(getAdapterPosition()));
                }
            });

            title = itemView.findViewById(R.id.item_title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(listAdapter.getTruePos(getAdapterPosition()));
                }
            });

            ImageView editButton = itemView.findViewById(R.id.item_edit_button);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEditButtonClick(listAdapter.getTruePos(getAdapterPosition()));
                }
            });

            ImageView deleteButton = itemView.findViewById(R.id.item_delete_button);
            deleteButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteButtonClick(listAdapter.getTruePos(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Playlist associée full
     */
    private final List<T> listFull;

    /**
     * Playlist associée filtrée
     */
    private final List<T> listCurrent;

    /**
     * Listener
     */
    private final AdapterListener<T> listener;

    /**
     * Filtre de recherche
     */
    private final Filter filter;

    /**
     * Constructeur
     *
     * @param playlist Playlist
     * @param listener Listener
     */
    public ListAdapter(@NonNull final List<T> playlist, @NonNull final AdapterListener<T> listener) {
        this.listFull = playlist;
        this.listCurrent = new ArrayList<>(playlist);
        this.listener = listener;
        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<T> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0)
                    filteredList.addAll(listFull);
                else
                    for (T elem : listFull)
                        if (listener.containsFilter(elem, constraint.toString().trim().toLowerCase()))
                            filteredList.add(elem);

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listCurrent.clear();
                listCurrent.addAll((List<T>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public ItemHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick();
                return true;
            }
        });

        return new ItemHolder<>(this, view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, final int position) {
        itemHolder.title.setText(listener.getTitle(listCurrent.get(position)));

        Bitmap bitmap = listener.getPreview(listCurrent.get(position));
        if (bitmap != null)
            itemHolder.preview.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return listCurrent.size();
    }

    @Override
    @NonNull
    public Filter getFilter() {
        return filter;
    }

    public void add(@NonNull T m) {
        listCurrent.add(m);
        listFull.add(m);
    }

    public void remove(int i) {
        listCurrent.remove(listFull.get(i));
        listFull.remove(i);
    }

    /**
     * Lorsque l'on cherche dans la searchbar les indexes sont décalés, ce qui pose un problème avec les listener.
     *
     * Exemple de liste non triée:
     * Ah
     * B
     * C
     * Dh
     *
     * Filtre par 'h':
     * Ah
     * Dh
     *
     * Clic sur 'Dh', on a la 2e position, mais on veut en réalité récupérer le 4e adapter,
     * d'où cette fonction
     *
     * @param pos "Fausse" position
     * @return "Vraie" position
     */
    private int getTruePos(int pos) {
        return listFull.indexOf(listCurrent.get(pos));
    }
}
