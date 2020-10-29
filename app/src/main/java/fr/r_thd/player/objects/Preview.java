package fr.r_thd.player.objects;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import fr.r_thd.player.objects.task.ImageFromURLTask;

public abstract class Preview implements Parcelable {
    private Bitmap bitmap;

    public abstract void onBitmapUpdate();

    public Preview(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Preview(String url) {
        new ImageFromURLTask(this).execute(url);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        onBitmapUpdate();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Preview(Parcel parcel) {
        this.bitmap = parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(bitmap, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Preview(source) {
                @Override
                public void onBitmapUpdate() {}
            };
        }

        @Override
        public Object[] newArray(int size) {
            return new Preview[size];
        }
    };
}
