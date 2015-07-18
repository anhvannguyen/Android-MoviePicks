package me.anhvannguyen.android.moviepicks.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
