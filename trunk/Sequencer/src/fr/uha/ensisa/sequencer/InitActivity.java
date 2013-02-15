package fr.uha.ensisa.sequencer;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class InitActivity extends Activity {

	Button hello;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
		hello = (Button) findViewById(R.id.button1);
		hello.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),SequencerActivity.class);
				Log.v("parcelable", "init post checbox");
				ArrayList<HashMapCheckBoxParcelable> cb=new ArrayList<HashMapCheckBoxParcelable>();
				cb.add(new HashMapCheckBoxParcelable());
				intent.putExtra("checkbox", cb);
				Log.v("parcelable", "init :"+cb.get(0));
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_init, menu);
		return true;
	}

}
