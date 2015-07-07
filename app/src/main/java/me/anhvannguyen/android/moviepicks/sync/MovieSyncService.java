package me.anhvannguyen.android.moviepicks.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by anhvannguyen on 7/7/15.
 */
public class MovieSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
