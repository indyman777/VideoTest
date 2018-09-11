package com.example.loft.videotest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    // Declare variables
    private int maxVolume;
    //    private ProgressDialog pDialog;
    private VideoView videoview;
    protected final int REQUEST_PERMISSIONS_CODE = 225;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        videoview = findViewById(R.id.videoView);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Log.i("DBINIT", "checking permission");

            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_SETTINGS, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.RECEIVE_BOOT_COMPLETED, Manifest.permission.WAKE_LOCK, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
            if (!hasPermissions(this, permissions)) {

                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
            }
        }

        // Set listener for button that will close the screen
        findViewById(R.id.btSaveDefaults).setOnClickListener(v -> onBackPressed());

        // Get max volume
        AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

//        // Execute StreamVideo AsyncTask
//
//        // Create a progressbar
//        pDialog = new ProgressDialog(VideoPlayer.this);
//        // Set progressbar title
//        pDialog.setTitle("Maintenance Video");
//        // Set progressbar message
//        pDialog.setMessage("Buffering...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(false);
//        // Show progressbar
//        pDialog.show();

        Bundle args = getIntent().getExtras();
        String videoPath = Environment.getExternalStorageDirectory().toString() + "/Video/Abstract Rainbow.mp4";
        int volume = 10;

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    MainActivity.this);
            mediacontroller.setAnchorView(videoview);

            // Get the URL from String VideoURL
            videoview.setMediaController(mediacontroller);
            videoview.setVideoPath(videoPath);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(setVolume(volume), setVolume(volume));
//                pDialog.dismiss();
                videoview.start();
            }
        });

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                onBackPressed();
            }
        });


    }

    public float setVolume(int amount) {

        // Convert number to a float from 0 to 1, logarithmically.
        final double numerator = maxVolume - amount > 0 ? Math.log(maxVolume - amount) : 0;
        final float volume = (float) (1 - (numerator / Math.log(maxVolume)));

        return volume;
    }

    private boolean hasPermissions(Context context, String[] permissions) {

        boolean permissionsGranted = true;
        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED)
                permissionsGranted = false;

        }
        return permissionsGranted;
    }

}
