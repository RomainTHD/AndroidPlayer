package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.r_thd.player.R;
import fr.r_thd.player.model.Music;
import fr.r_thd.player.model.Playlist;

public class HomeActivity extends AppCompatActivity {
    private static List<Playlist> playlistList = new ArrayList<>();

    static {
        Playlist p1 = new Playlist();
        p1.add(new Music("test", R.raw.abc));
        p1.add(new Music("test2", R.raw.abc));

        Playlist p2 = new Playlist();
        p2.add(new Music("test3", R.raw.abc));
        p2.add(new Music("test4", R.raw.abc));

        playlistList.add(p1);
        playlistList.add(p2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
