package fr.r_thd.player.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;

import fr.r_thd.player.R;
import fr.r_thd.player.objects.Music;

/**
 * Notification de musique
 */
public class MusicPlayerNotification {
    /**
     * Prochain ID libre
     */
    private static int NEXT_FREE_ID = 1;

    /**
     * Nom du channel
     */
    private static final String CHANNEL_NAME = "Musique";

    /**
     * ID du channel
     */
    private static final String CHANNEL_ID = "channel_id";

    /**
     * Description du channel
     */
    private static final String CHANNEL_DESCRIPTION = "Channel de musique";

    /**
     * Nom de la requête
     */
    public static final String REQUEST_NAME = "REQUEST_NAME";

    /**
     * Requête pour musique précédente
     */
    public static final String REQUEST_PREV = "REQUEST_PREV";

    /**
     * Requête pour play / pause la musique
     */
    public static final String REQUEST_PAUSE = "REQUEST_PAUSE";

    /**
     * Requête pour musique suivante
     */
    public static final String REQUEST_NEXT = "REQUEST_NEXT";

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * ID de la notification
     */
    private final int id;

    /**
     * Notification
     */
    private final Notification notification;

    /**
     * Notification manager
     */
    private final NotificationManager manager;

    /**
     * Constructeur
     *
     * @param manager Manager
     * @param context Contexte
     * @param baseIntent Intention de base
     * @param music Musique
     */
    public MusicPlayerNotification(NotificationManager manager, Context context, Intent baseIntent, Music music) {
        this.manager = manager;

        // Création du channel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(CHANNEL_DESCRIPTION);
        manager.createNotificationChannel(channel);

        // Session de média pour un joli affichage
        MediaSession mediaSession = new MediaSession(context, "mediaSession");

        Intent prevIntent = (Intent) baseIntent.clone();
        prevIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        prevIntent.setAction(Intent.ACTION_MAIN);
        prevIntent.putExtra(REQUEST_NAME, REQUEST_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(context, 1, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pauseIntent = (Intent) baseIntent.clone();
        pauseIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        pauseIntent.setAction(Intent.ACTION_MAIN);
        pauseIntent.putExtra(REQUEST_NAME, REQUEST_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getActivity(context, 2, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = (Intent) baseIntent.clone();
        nextIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        nextIntent.setAction(Intent.ACTION_MAIN);
        nextIntent.putExtra(REQUEST_NAME, REQUEST_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getActivity(context, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Création de la notification
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_play)
                .addAction(R.drawable.ic_previous, "Previous", prevPendingIntent)
                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1) // action lors de la vue compacte
                        .setMediaSession(MediaSessionCompat.Token.fromToken(mediaSession.getSessionToken())))
                .setContentTitle(music.getTitle())
                .setContentText(music.getTitle())
                .setLargeIcon(music.getPicture())
                .build();

        this.id = NEXT_FREE_ID++;
    }

    /**
     * Affiche la notification
     */
    public void show() {
        manager.notify(id, notification);
    }

    /**
     * Détruit la notification
     */
    public void destroy() {
        manager.cancel(id);
    }
}
