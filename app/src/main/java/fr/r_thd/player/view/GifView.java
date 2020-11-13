package fr.r_thd.player.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import fr.r_thd.player.R;

/**
 * Affichage d'un GIF
 */
public class GifView extends View {
    /**
     * GIF
     */
    private Movie movie;

    /**
     * Début du GIF
     */
    private long movieStart;

    /**
     * Peinture
     */
    private Paint paint;

    /**
     * Initialisation
     *
     * @param context Contexte
     */
    private void init(Context context) {
        setFocusable(true);

        Paint p = new Paint();
        p.setAntiAlias(true);

        java.io.InputStream is;
        is = context.getResources().openRawResource(R.raw.logo);
        movie = Movie.decodeStream(is);
    }

    public GifView(Context context) {
        super(context);
        init(context);
    }

    public GifView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        init(context);
    }

    public GifView(Context context, AttributeSet attrSet, int defStyle) {
        super(context, attrSet, defStyle);
        init(context);
    }

    /**
     * Affichage
     *
     * @param canvas Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x00000000);

        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0) { // Début
            movieStart = now;
        }

        if (movie != null) {
            int dur = movie.duration();

            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - movieStart) % dur);
            movie.setTime(relTime);
            canvas.scale(3, 3, (float) getWidth()/2, (float) getHeight()/2);
            movie.draw(canvas, (float)(getWidth() - movie.width())/2, (float)(getHeight() - movie.height())/2);
            invalidate();
        }
    }
}
