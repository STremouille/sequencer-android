package fr.uha.ensisa.sequencer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.CheckBox;

public class MusicTask extends TimerTask {
	HashMap<Integer, MediaPlayer> mediaPlayers;
	HashMap<Integer, ArrayList<CheckBox>> cb;
	int waitBetweenStep;

	public MusicTask(HashMap<Integer, MediaPlayer> mp,	HashMap<Integer, ArrayList<CheckBox>> cb, float tempo) {
		this.mediaPlayers = mp;
		this.cb = cb;
		this.waitBetweenStep = (int) ((60.0 / tempo) * 1000.0);
		Log.i("tempo", "Step : " + 60.0 / (tempo));
	}

	@Override
	public void run() {
		for (int i = 0; i < 4; i++) {
			for (int mpNumber : mediaPlayers.keySet()) {
				if (cb.get(mpNumber).get(i).isChecked()) {
					mediaPlayers.get(mpNumber).seekTo(0);
					mediaPlayers.get(mpNumber).start();
				}
			}

			try {
				Thread.currentThread().sleep(waitBetweenStep);
			} catch (InterruptedException e) {
				e.printStackTrace();

			}
		}
	}

}
