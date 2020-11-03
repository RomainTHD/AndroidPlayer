package fr.r_thd.player.adapter;

public interface AdapterListener {
    void onLongClick();
    void onClick(int pos);
    void onEditButtonClick(int pos);
    void onDeleteButtonClick(int pos);
}
