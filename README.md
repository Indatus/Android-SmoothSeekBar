Android-SmoothSeekBar
=====================

An abstraction of Android's SeekBar that animates smoothly.

Compatible with Android 4.0+

The latest SNAPSHOT can be found [here](https://oss.sonatype.org/content/repositories/snapshots/com/indatus/jonstaff/androidsmoothseekbar/library/1.0.0-SNAPSHOT/) - I'm working on getting it properly uploaded to MavenCentral.

## Importing Using Android Studio and Gradle

Using Gradle is the easiest way to incorporate this into your project. Add the following lines inside your `build.gradle` after importing the as a module:

````
dependencies {
    compile project(':library')
}
````


## XML Attributes

Make sure to add `xmlns:custom="http://schemas.android.com/apk/res-auto"` to the root view of any layout where you use the SmoothSeekBar. You can then set the background and fill colors in XML like so:
````
custom:progressBackgroundColor="@color/bg_color"
custom:progressFillColor="@color/fill_color"
````


## Implementation

As shown in the sample application, the following steps will be necessary for proper functionality:
- `setOnSeekBarChangeListener(listener)` needs to be implemented
- call `setEndTime(int)` to give the seekbar the length of playback
- call `beginAnimating()` to start the animation
- call `pauseAnimating()` to pause the animation
- call `endAnimation()` to end the animation

#### SeekBar.OnSeekBarChangeListener Method Overrides

You'll need to override the following methods to use the SmoothSeekBar. Mine generally look something like this:

````
@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if (fromUser) {
			seekBar.setProgress(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mPlayer.pause();
		mSeekBar.pauseAnimating();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mPlayer.seekTo(getSeekToProgress());
		mPlayer.start();
		mSeekBar.beginAnimating();
	}
````

where `getSeekToProgress()` calculates where the MediaPlayer should seek to and returns its value as an int:

````
	private int getSeekToProgress() {
		return (int) ((double) mSeekBar.getProgress() * (1 / (double) mSeekBar.getMax()) * (double) mPlayer.getDuration());
	}
````



## Disclaimer

This readme is a work in progress and will be updated accordingly. For now, view the samples for a better understanding of how to implement this into your project.
