package fr.uha.ensisa.sequencer;

import java.io.FileDescriptor;
import java.io.IOException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SequencerActivity extends Activity {
	private static final int DEFAULT_TEMPO = 140;
	ArrayList<MediaPlayer> mp;
	HashMap<Integer, ArrayList<CheckBox>> checkbox;
	HashMap<Integer, String> instrumentNameById;
	Button start, stop;
	int instrumentCount;
	Timer t;
	float tempo;
	Context context;
	EditText editTempo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initListInstrument();
		instrumentCount = 4;
		checkbox = new HashMap<Integer, ArrayList<CheckBox>>();
		context=this;
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		mp = new ArrayList<MediaPlayer>();
		
		
		for(int line=0;line<instrumentCount;line++){
			ArrayList<CheckBox> temp = new ArrayList<CheckBox>();
			LinearLayout instrumentLine = new LinearLayout(this);
			
			//init of sound
			mp.add(new MediaPlayer());
			AssetFileDescriptor afd;
			try {
				afd = getAssets().openFd(instrumentNameById.get(line));
				mp.get(line).setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				mp.get(line).prepare();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//init of checkbox + switchbutton
			for(int i=0;i<4;i++)
			{
				CheckBox cb = new CheckBox(this);
				cb.setGravity(Gravity.CENTER);
				instrumentLine.addView(cb);
				temp.add(cb);
			}
			checkbox.put(line, temp);
			Button switchInstrument = new Button(this); switchInstrument.setText(R.string.mySwitch); switchInstrument.setGravity(Gravity.CENTER);
			switchInstrument.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,FileChooserActivity.class);
					intent.putExtra("InstrumentLine", "file");
					startActivity(intent);
				}
			});
			instrumentLine.addView(switchInstrument);
			layout.addView(instrumentLine,LayoutParams.MATCH_PARENT);
		}
		
		//Tempo
		editTempo = new EditText(this);
		editTempo.setHint(R.string.tempoHint);
		editTempo.setInputType(InputType.TYPE_CLASS_NUMBER);
		layout.addView(editTempo);
		
		//Button Start/Stop
		LinearLayout ButtonLayout = new LinearLayout(this);
		ButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
		start = new Button(this);
		start.setText(R.string.start);
		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMusic();
			}
		});
		ButtonLayout.addView(start);
		stop = new Button(this);
		stop.setText(R.string.stop);
		stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopMusic();
			}
		});
		ButtonLayout.addView(stop);
		
		layout.addView(ButtonLayout);
		
		
		
		this.setContentView(layout);
		

		t = new Timer();
	
	}
	
	public void initListInstrument(){
		instrumentNameById = new HashMap<Integer, String>();
		instrumentNameById.put(0, "kick.wav");
		instrumentNameById.put(1, "tom.wav");
		instrumentNameById.put(2, "wooble.mp3");
		instrumentNameById.put(3, "wooble2.mp3");
	}
		
	
	public void switchClick(View view) {
	    // Do something in response to button
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();
		String rse = intent.getStringExtra("newSound");
		Log.i("myassets", "new sound -> "+rse);
	}

	private void stopMusic() {
		t.cancel();
		t.purge();
		t = new Timer();
	}

	private void startMusic() {
		this.stopMusic();
		tempo = getTempo();

		if (tempo > 40 && tempo < 300) {
			Log.i("tempo", "Big Step : " + tempo + "/"
					+ (60000.0 / (tempo * 1000.0)) * 4.0);
			TimerTask tt = new MusicTask(mp, checkbox, tempo);
			t.scheduleAtFixedRate(tt, 0, (long) ((60.0/ tempo) * 4000));
		}
	}

	private int getTempo() {
		if (editTempo.getText().toString().equals(""))
			return DEFAULT_TEMPO;
		else
			return Integer.valueOf(editTempo.getText().toString());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopMusic();
	}

}