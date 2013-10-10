Android-SmoothSeekBar
=====================

An abstraction of Android's SeekBar that animates smoothly.

Compatible with Android 4.0+

Using Gradle is the easiest way to incorporate this into your project. Add the following lines inside your `build.gradle` after importing the as a module:

````
dependencies {
    compile project(':library')
}
````

Make sure to add `xmlns:custom="http://schemas.android.com/apk/res-auto"` to the root view of any layout where you use the SmoothSeekBar. You can then set the background and fill colors in XML like so:
````
custom:progressBackgroundColor="@color/bg_color"
custom:progressFillColor="@color/fill_color"
````

As shown in the sample application, the following steps will be necessary for proper functionality:
- `setOnSeekBarChangeListener(listener)` needs to be implemented
- call `setEndTime(int)` to give the seekbar the length of playback
- call `beginAnimating()` to start the animation
- call `pauseAnimating()` to pause the animation
- call `endAnimation()` to end the animation




This readme is a work in progress and will be updated accordingly. For now, view the samples for a better understanding of how to implement this into your project.
