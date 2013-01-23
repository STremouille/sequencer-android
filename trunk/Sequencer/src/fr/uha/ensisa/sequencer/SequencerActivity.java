package fr.uha.ensisa.sequencer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class SequencerActivity extends Activity {
	private static final int DEFAULT_TEMPO = 140;
	ArrayList<MediaPlayer> mp;
	HashMap<Integer, MediaPlayer> mediaPlayers;
	CheckBoxParcelable checkbox;
	static TreeMap<Integer, String> instrumentNameById;
	Button start, stop, addLine;
	Timer t;
	float tempo;
	Context context;
	EditText editTempo;
	TextView numTV;
	android.widget.LinearLayout.LayoutParams foot,body,eraseButton,instrumentName,checkBoxPm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initListInstrument();
		int instrumentCount = instrumentNameById.keySet().size();
		Log.i("create", "creation avec "+instrumentCount+" �l�ments");
		context = this;
		initView();
		
		t = new Timer();

	}

	public void initListInstrument() {
		if (instrumentNameById == null) {
			Log.i("myassets", "InstruList vide");
			instrumentNameById = new TreeMap<Integer, String>();
			instrumentNameById.put(0, "kick.wav");
			instrumentNameById.put(1, "tom.wav");
			instrumentNameById.put(2, "wooble.mp3");
			instrumentNameById.put(3, "wooble2.mp3");
		}
	}

	public void initView() {
		fetchTransmittedInstrument();
		fetchNewVolume();
		
		foot = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
		body = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
		eraseButton = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.1f);
		checkBoxPm = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT,1.0f);
		instrumentName = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1.0f);
		
		checkbox = new CheckBoxParcelable();
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		mediaPlayers = new HashMap<Integer, MediaPlayer>();
		
		
		//line by line
		for (int line : instrumentNameById.keySet()) {
			ArrayList<CheckBox> temp = new ArrayList<CheckBox>();
			LinearLayout instrumentLine = new LinearLayout(this);
			
			//name of instrument
			int tmpIndex = instrumentNameById.get(line).lastIndexOf(".");
			String instrName=instrumentNameById.get(line).substring(0, tmpIndex);
			TextView name = new TextView(this);
			name.setText(instrName);
			name.setGravity(Gravity.RIGHT);
			name.setGravity(Gravity.CENTER_VERTICAL);
			instrumentLine.addView(name,instrumentName);
			
			
			
			
			
			// init of sound
			mediaPlayers.put(line, new MediaPlayer());
			AssetFileDescriptor afd;
			try {
				afd = getAssets().openFd(instrumentNameById.get(line));
				mediaPlayers.get(line).setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
				mediaPlayers.get(line).prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//debug indice instrument
			/*numTV = new TextView(this);
			numTV.setText(String.valueOf(line));
			instrumentLine.addView(numTV);*/

			// init of checkbox
			for (int i = 0; i < 4; i++) {
				CheckBox cb = new CheckBox(this);
				cb.setGravity(Gravity.CENTER);
				instrumentLine.addView(cb,checkBoxPm);
				temp.add(cb);
			}
			checkbox.put(line+"", temp);
			
			//switch
			ImageView switchInstrument = new ImageView(this);
			switchInstrument.setImageResource(R.drawable.change);
			final int lineNb = line;
			switchInstrument.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					Intent intent = new Intent(context,
							FileChooserActivity.class);
					intent.putExtra("InstrumentLine", lineNb);
					Log.i("myassets", lineNb + "");
					startActivity(intent);
				}
			});
			instrumentLine.addView(switchInstrument,eraseButton);
			
			//delete
			ImageView delete = new ImageView(this);
			delete.setImageResource(R.drawable.delete);
			delete.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					reconstruct(lineNb);
					initView();
				}
			});
			instrumentLine.addView(delete,eraseButton);
			//instrumentLine.setGravity(Gravity.CENTER);
			layout.addView(instrumentLine,body);
		}

		// Add line
		addLine = new Button(this);
		addLine.setText(R.string.add);
		addLine.setGravity(Gravity.RIGHT);
		addLine.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(context, FileChooserActivity.class);
				if(instrumentNameById.isEmpty())
					intent.putExtra("InstrumentLine", 1);
				else
					intent.putExtra("InstrumentLine", instrumentNameById.lastKey()+1);
				startActivity(intent);
			}
		});
		layout.addView(addLine,foot);

		// Tempo
		editTempo = new EditText(this);
		editTempo.setHint(R.string.tempoHint);
		editTempo.setInputType(InputType.TYPE_CLASS_NUMBER);
		layout.addView(editTempo,foot);

		// Button Start/Stop
		LinearLayout ButtonLayout = new LinearLayout(this);
		ButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
		start = new Button(this);
		start.setText(R.string.start);
		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startMusic();
			}
		});
		ButtonLayout.addView(start);
		stop = new Button(this);
		stop.setText(R.string.stop);
		stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				stopMusic();
			}
		});
		ButtonLayout.addView(stop);

		layout.addView(ButtonLayout,foot);

		this.setContentView(layout);
	}

	private void fetchNewVolume() {
		Intent intent = getIntent();
		int rse = intent.getIntExtra("volume", -1);
		if(rse!=-1)
		{
			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audio.setStreamVolume(AudioManager.STREAM_MUSIC, rse, AudioManager.FLAG_VIBRATE);
		}
	}

	private void fetchTransmittedInstrument() {
		//fetch the new instrument if there is something in the extra
		Intent intent = getIntent();
		String rse = intent.getStringExtra("newSound");
		int line1 = intent.getIntExtra("InstrumentLine", -1);
				if (line1 != -1) {
					Log.i("filechooser", line1+"");
					instrumentNameById.put(line1, rse);
					Log.i("change size", instrumentNameById.size()+"->"+instrumentNameById.get(5));
				}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	private void stopMusic() {
		t.cancel();
		t.purge();
		t = new Timer();
	}

	private void startMusic() {
		this.stopMusic();
		tempo = getTempo();

		if (tempo >= 40 && tempo <= 300) {
			Log.i("tempo", "Big Step : " + tempo + "/"
					+ (60000.0 / (tempo * 1000.0)) * 4.0);
			Log.i("MP", mediaPlayers.size() + "");
			TimerTask tt = new MusicTask(mediaPlayers, checkbox, tempo);
			t.scheduleAtFixedRate(tt, 0, (long) ((60.0 / tempo) * 4000));
		}
		
		if (!(tempo > 40 && tempo < 300)) {
			DialogFragment df = new TempoDialogFragment();
			df.show(getFragmentManager(), "tempo");
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
		super.onPause();
		stopMusic();
	}
	
	

	private void reconstruct(int i)
	{
		instrumentNameById.remove(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		LinearLayout layout = new LinearLayout(this);
		SeekBar bar = new SeekBar(context);
		bar.setMax(15);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.volume:
			Intent i = new Intent(context,VolumeChooserActivity.class);
			startActivity(i);			
			return true;
		}
		return false;
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		checkbox=savedInstanceState.getParcelable("checkbox");
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("checkbox", checkbox);
		super.onSaveInstanceState(outState);
	}
	
	
	
}