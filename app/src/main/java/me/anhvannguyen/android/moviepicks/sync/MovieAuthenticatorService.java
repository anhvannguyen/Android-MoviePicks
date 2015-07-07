package me.anhvannguyen.android.moviepicks.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by anhvannguyen on 7/7/15.
 */
public class MovieAuthenticatorService extends Service {
    private MovieAccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new MovieAccountAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
