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
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class SequencerActivity extends Activity {
	private static final int DEFAULT_TEMPO = 140;
	ArrayList<MediaPlayer> mp;
	HashMap<Integer, MediaPlayer> mediaPlayers;
	HashMap<Integer, ArrayList<CheckBox>> checkbox;
	static HashMap<Integer, String> instrumentNameById;
	Button start, stop, addLine;
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
		instrumentCount = instrumentNameById.size();
		Log.i("create", "creation avec "+instrumentCount+" éléments");
		context = this;
		initView();
		
		t = new Timer();

	}

	public void initListInstrument() {
		if (instrumentNameById == null) {
			Log.i("myassets", "InstruList vide");
			instrumentNameById = new HashMap<Integer, String>();
			instrumentNameById.put(0, "kick.wav");
			instrumentNameById.put(1, "tom.wav");
			instrumentNameById.put(2, "wooble.mp3");
			instrumentNameById.put(3, "wooble2.mp3");
		}
	}

	public void initView() {
		checkbox = new HashMap<Integer, ArrayList<CheckBox>>();
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		mediaPlayers = new HashMap<Integer, MediaPlayer>();
		instrumentCount=instrumentNameById.size();
		Log.i("initView", instrumentCount + "");
		for (int line = 0; line < instrumentCount; line++) {
			ArrayList<CheckBox> temp = new ArrayList<CheckBox>();
			LinearLayout instrumentLine = new LinearLayout(this);
			// init of sound
			mediaPlayers.put(line, new MediaPlayer());
			AssetFileDescriptor afd;
			try {
				afd = getAssets().openFd(instrumentNameById.get(line));
				mediaPlayers.get(line).setDataSource(afd.getFileDescriptor(),
						afd.getStartOffset(), afd.getLength());
				mediaPlayers.get(line).prepare();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// init of checkbox + switchbutton
			for (int i = 0; i < 4; i++) {
				CheckBox cb = new CheckBox(this);
				cb.setGravity(Gravity.CENTER);
				instrumentLine.addView(cb,LayoutParams.WRAP_CONTENT);
				temp.add(cb);
			}
			checkbox.put(line, temp);
			Button switchInstrument = new Button(this);
			switchInstrument.setText(instrumentNameById.get(line));
			switchInstrument.setGravity(Gravity.CENTER);
			final int lineNb = line;
			switchInstrument.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,
							FileChooserActivity.class);
					intent.putExtra("InstrumentLine", lineNb);
					Log.i("myassets", lineNb + "");
					startActivity(intent);
				}
			});
			instrumentLine.addView(switchInstrument);
			layout.addView(instrumentLine);
		}

		// Add line
		addLine = new Button(this);
		addLine.setText(R.string.add);
		addLine.setGravity(Gravity.RIGHT);
		addLine.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, FileChooserActivity.class);
				intent.putExtra("InstrumentLine", instrumentCount+1);
				Log.i("myassets", instrumentCount+1 + "<-instrumentCount");
				startActivity(intent);
			}
		});
		layout.addView(addLine);

		// Tempo
		editTempo = new EditText(this);
		editTempo.setHint(R.string.tempoHint);
		editTempo.setInputType(InputType.TYPE_CLASS_NUMBER);
		layout.addView(editTempo);

		// Button Start/Stop
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();
		String rse = intent.getStringExtra("newSound");
		int line = intent.getIntExtra("InstrumentLine", -1);
		Log.i("change", rse+" pour le "+line);
		if (line != -1) {
			Log.i("change size", instrumentNameById.size()+"");
			instrumentNameById.put(line-1, rse);
			Log.i("change size", instrumentNameById.size()+"->"+instrumentNameById.get(5));
			initView();
		}
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
			Log.i("MP", mediaPlayers.size() + "");
			TimerTask tt = new MusicTask(mediaPlayers, checkbox, tempo);
			t.scheduleAtFixedRate(tt, 0, (long) ((60.0 / tempo) * 4000));
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