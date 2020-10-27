package fr.r_thd.player.adapter;

import fr.r_thd.player.model.Music;

public interface MusicAdapterListener {
    void onLongClick();
    void onClick(int pos);
    void onEditButtonClick(int pos);
    void onDeleteButtonClick(int pos);
}
