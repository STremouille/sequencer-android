package fr.uha.ensisa.sequencer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.CheckBox;

public class MusicTask extends TimerTask {
	ArrayList<MediaPlayer> mp;
	HashMap<Integer, ArrayList<CheckBox>> cb;
	int waitBetweenStep;

	public MusicTask(ArrayList<MediaPlayer> mp,	HashMap<Integer, ArrayList<CheckBox>> cb, float tempo) {
		this.mp = mp;
		this.cb = cb;
		this.waitBetweenStep = (int) ((60.0 / tempo) * 1000.0);
		Log.i("tempo", "Step : " + 60.0 / (tempo));
	}

	@Override
	public void run() {
		for (int i = 0; i < 4; i++) {
			for (int mpNumber = 0; mpNumber < mp.size(); mpNumber++) {
				if (cb.get(mpNumber).get(i).isChecked()) {
					mp.get(mpNumber).seekTo(0);
					mp.get(mpNumber).start();
				}
			}

			try {
				Thread.currentThread().sleep(waitBetweenStep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}
	}

}
