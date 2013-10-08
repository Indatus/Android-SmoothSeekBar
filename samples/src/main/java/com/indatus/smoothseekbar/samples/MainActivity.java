package com.indatus.smoothseekbar.samples;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.indatus.smoothseekbar.library.SmoothSeekBar;

public class MainActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
	public static final String TAG = MainActivity.class.getSimpleName();

	SmoothSeekBar mSeekBar;
	MediaPlayer mPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSeekBar = (SmoothSeekBar) findViewById(R.id.seekbar);
		mSeekBar.setOnSeekBarChangeListener(this);

		Button btnPlay = (Button) findViewById(R.id.btn_play);
		btnPlay.setOnClickListener(this);

		Button btnStop = (Button) findViewById(R.id.btn_stop);
		btnStop.setOnClickListener(this);

		mPlayer = new MediaPlayer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void playAudio() {
		try {
			mPlayer.reset();
			mPlayer.setDataSource("somewhere");
			mPlayer.prepare();
			mPlayer.start();

			mSeekBar.setProgress(0);
			mSeekBar.setEndTime(mPlayer.getDuration());
			mSeekBar.beginAnimating();
		}
		catch (IOException e) {
			Log.w(TAG, "The audio file you're trying to play isn't available.");
			e.printStackTrace();
		}
	}

	private void stopAudio() {
		mPlayer.stop();
		mSeekBar.endAnimation();
	}

	//     ___       _             __                  ___                 _                           _        _   _
	//    |_ _|_ __ | |_ ___ _ __ / _| __ _  ___ ___  |_ _|_ __ ___  _ __ | | ___ _ __ ___   ___ _ __ | |_ __ _| |_(_) ___  _ __  ___
	//     | || '_ \| __/ _ \ '__| |_ / _` |/ __/ _ \  | || '_ ` _ \| '_ \| |/ _ \ '_ ` _ \ / _ \ '_ \| __/ _` | __| |/ _ \| '_ \/ __|
	//     | || | | | ||  __/ |  |  _| (_| | (_|  __/  | || | | | | | |_) | |  __/ | | | | |  __/ | | | || (_| | |_| | (_) | | | \__ \
	//    |___|_| |_|\__\___|_|  |_|  \__,_|\___\___| |___|_| |_| |_| .__/|_|\___|_| |_| |_|\___|_| |_|\__\__,_|\__|_|\___/|_| |_|___/
	//                                                              |_|

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mSeekBar.pauseAnimating();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mSeekBar.beginAnimating();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_play:
				// do stuff
				playAudio();
				break;
			case R.id.btn_stop:
				// do more stuff
                stopAudio();
				break;
		}
	}
}
