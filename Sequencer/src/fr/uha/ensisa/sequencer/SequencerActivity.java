package fr.uha.ensisa.sequencer;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SequencerActivity extends Activity {
	/** Called when the activity is first created. */
	ArrayList<MediaPlayer> mp;
	HashMap<Integer, ArrayList<CheckBox>> cb;
	Button start, stop;
	Timer t;
	float tempo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		tempo = 60;

		cb = new HashMap<Integer, ArrayList<CheckBox>>();
		//1er line
		ArrayList<CheckBox> temp1 = new ArrayList<CheckBox>();
		temp1.add((CheckBox) findViewById(R.id.CheckBox01));
		temp1.add((CheckBox) findViewById(R.id.CheckBox02));
		temp1.add((CheckBox) findViewById(R.id.CheckBox03));
		temp1.add((CheckBox) findViewById(R.id.CheckBox04));
		cb.put(0, temp1);
		
		//2 line
		ArrayList<CheckBox> temp2 = new ArrayList<CheckBox>();
		temp2.add((CheckBox) findViewById(R.id.CheckBox11));
		temp2.add((CheckBox) findViewById(R.id.CheckBox12));
		temp2.add((CheckBox) findViewById(R.id.CheckBox13));
		temp2.add((CheckBox) findViewById(R.id.CheckBox14));
		cb.put(1, temp2);
		
		//3 line
		ArrayList<CheckBox> temp3 = new ArrayList<CheckBox>();
		temp3.add((CheckBox) findViewById(R.id.CheckBox21));
		temp3.add((CheckBox) findViewById(R.id.CheckBox22));
		temp3.add((CheckBox) findViewById(R.id.CheckBox23));
		temp3.add((CheckBox) findViewById(R.id.CheckBox24));
		cb.put(2, temp3);
		
		//4 line
		ArrayList<CheckBox> temp4 = new ArrayList<CheckBox>();
		temp4.add((CheckBox) findViewById(R.id.CheckBox31));
		temp4.add((CheckBox) findViewById(R.id.CheckBox32));
		temp4.add((CheckBox) findViewById(R.id.CheckBox33));
		temp4.add((CheckBox) findViewById(R.id.CheckBox34));
		cb.put(3, temp4);

		t = new Timer();
		for (int i = 0; i < cb.size(); i++) {
			for(int j=0;j<4;j++)
			{
				cb.get(i).get(j).setChecked(false);
			}
		}
		mp = new ArrayList<MediaPlayer>();
		mp.add(new MediaPlayer());mp.add(new MediaPlayer());mp.add(new MediaPlayer());
		mp.add(new MediaPlayer());
		try {
			AssetFileDescriptor afd1 = getAssets().openFd("kick.wav");
			mp.get(0).setDataSource(afd1.getFileDescriptor(), afd1.getStartOffset(),
					afd1.getLength());
			mp.get(0).prepare();
			AssetFileDescriptor afd2 = getAssets().openFd("tom.wav");
			mp.get(1).setDataSource(afd2.getFileDescriptor(), afd2.getStartOffset(),
					afd2.getLength());
			mp.get(1).prepare();
			mp.get(2).setDataSource(afd2.getFileDescriptor(), afd2.getStartOffset(),
					afd2.getLength());
			mp.get(2).prepare();
			mp.get(3).setDataSource(afd2.getFileDescriptor(), afd2.getStartOffset(),
					afd2.getLength());
			mp.get(3).prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//

		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMusic();
			}
		});

		stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopMusic();
			}
		});
	}

	private void stopMusic() {
		t.cancel();
		t.purge();
		t = new Timer();
	}

	private void startMusic() {
		this.stopMusic();
		tempo = getTempo();

		if (tempo > 40 && tempo < 200) {
			Log.i("tempo", "Big Step : " + tempo + "/"
					+ (60000.0 / (tempo * 1000.0)) * 4.0);
			TimerTask tt = new MusicTask(mp, cb, tempo);
			t.scheduleAtFixedRate(tt, 0, (long) ((60.0/ tempo) * 4000));
		}
	}

	private int getTempo() {
		EditText temp = (EditText) findViewById(R.id.tempo);
		if (temp.getText().toString().equals(""))
			return 60;
		else
			return Integer.valueOf(temp.getText().toString());
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopMusic();
	}

}