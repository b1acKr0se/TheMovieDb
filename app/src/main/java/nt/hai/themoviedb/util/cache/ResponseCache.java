package nt.hai.themoviedb.util.cache;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nt.hai.themoviedb.data.model.Media;

import static android.os.Environment.isExternalStorageRemovable;

public class ResponseCache {
    private DiskLruCache diskLruCache;

    public ResponseCache(Context context) throws IOException {
        final File diskCacheDir = getDiskCacheDir(context, "TheMovieDb");
        diskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, (long) (20 * Math.pow(2, 20)));
    }

    public void insert(String key, List<Media> list) {
        Parcel parcel = Parcel.obtain();
        parcel.writeList(list);
        saveValue(key, parcel);
    }

    public List<Media> get(String key) {
        List<Media> list = new ArrayList<>();
        Parcel parcel = getParcel(key);
        if (parcel != null) {
            try {
                parcel.readList(list, Media.class.getClassLoader());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                parcel.recycle();
            }
        }
        return list;
    }

    private Parcel getParcel(String key) {
        byte[] value = null;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot == null) {
                return null;
            }
            value = getBytesFromStream(snapshot.getInputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(value, 0, value.length);
        parcel.setDataPosition(0);
        return parcel;
    }

    private void saveValue(String key, Parcel value) {
        key = key.toLowerCase();
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream outputStream = editor.newOutputStream(0);
            writeBytesToStream(outputStream, value.marshall());
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            value.recycle();
        }
    }

    private static byte[] getBytesFromStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            int count;
            while ((count = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, count);
            }
            buffer.flush();
            return buffer.toByteArray();
        } finally {
            is.close();
            buffer.close();
        }
    }

    private static void writeBytesToStream(OutputStream outputStream, byte[] bytes) throws IOException {
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    private static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath =
                Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                        !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
                        context.getCacheDir().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    private static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }
}
