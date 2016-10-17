package nt.hai.themoviedb.util.cache;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        DiskLruCache.Editor editor;
        try {
            editor = diskLruCache.edit(key);
            if (editor == null) {
                return;
            }
            ObjectOutputStream out = new ObjectOutputStream(editor.newOutputStream(0));
            out.writeObject(list);
            out.close();
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Media> get(String key) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if(snapshot == null) return null;
            ObjectInputStream in = new ObjectInputStream(snapshot.getInputStream(0));
            return (List<Media>) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
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
