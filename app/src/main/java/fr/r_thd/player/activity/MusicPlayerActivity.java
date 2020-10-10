package fr.r_thd.player.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import fr.r_thd.player.R;
import fr.r_thd.player.service.MusicPlayerService;

public class MusicPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        FloatingActionButton playPauseButton = findViewById(R.id.button_play_pause);

        final TextView text = findViewById(R.id.text);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(MusicPlayerService.class)) {
                    // playPauseButton.setImageDrawable();
                    text.setText("Stoped");
                    stopService(new Intent(MusicPlayerActivity.this, MusicPlayerService.class));
                } else {
                    text.setText("Started");
                    startService(new Intent(MusicPlayerActivity.this, MusicPlayerService.class));
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
