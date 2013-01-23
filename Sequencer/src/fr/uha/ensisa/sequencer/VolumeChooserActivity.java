package fr.uha.ensisa.sequencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class VolumeChooserActivity extends Activity{
	
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		LinearLayout layout = new LinearLayout(this);
		final SeekBar bar = new SeekBar(this);
		bar.setMax(15);
		
		
		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
		bar.setProgress(currentVolume);
		
		TextView text = new TextView(this);
		text.setText(R.string.pickTheVolume);
		text.setGravity(Gravity.CENTER);
		
		Button valid = new Button(this);valid.setText(R.string.valid);valid.setGravity(Gravity.CENTER);
		Button cancel = new Button(this);cancel.setText(R.string.cancel);cancel.setGravity(Gravity.CENTER);
		valid.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,SequencerActivity.class);
				i.putExtra("volume", bar.getProgress());
				startActivity(i);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context,SequencerActivity.class);
				startActivity(i);
			}
		});
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.addView(valid);ll.addView(cancel);
		ll.setGravity(Gravity.CENTER);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(text);
		layout.addView(bar);
		layout.addView(ll);
		layout.setGravity(Gravity.CENTER);
		
		this.setContentView(layout);
	}
	
}
