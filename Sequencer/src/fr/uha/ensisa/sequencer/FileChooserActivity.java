package fr.uha.ensisa.sequencer;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FileChooserActivity extends Activity{
	AssetManager am;
	ArrayList<HashMapCheckBoxParcelable> data;
	String[] ressources;
	String selectedFile = "";
	Context context;
	int line;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.filechooser);
		am = this.getAssets();
		try {
			ressources=am.list("");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent i = getIntent();
		final String comingFrom = i.getStringExtra("comingFrom");
		data = i.getParcelableArrayListExtra("checkbox");
		line = i.getIntExtra("InstrumentLine",-1);
		Log.i("myassets", "line to change : "+line);
		Log.i("myassets", "Start");
		context = this;
				
		
		//Build layout
		ScrollView view = new ScrollView(this);
		LinearLayout child = new LinearLayout(context);
		child.setOrientation(LinearLayout.VERTICAL);
		
		//ArrayList<Button> res = new ArrayList<Button>();
		for(String fileInAsset : ressources){
			Log.i("myassets", fileInAsset);
			if(!(fileInAsset.equals("images")||fileInAsset.equals("sounds")||fileInAsset.equals("webkit")||fileInAsset.equals("kioskmode")))
			{
				final Button temp = new Button(this);
				temp.setText(fileInAsset);
				temp.setOnClickListener( new View.OnClickListener() {
					
					public void onClick(View v) {
						
						
						selectedFile = (String) temp.getText();
						Intent intent = new Intent(context, SequencerActivity.class);
						intent.putExtra("newSound", selectedFile);
						Log.i("filechooser", selectedFile);
						intent.putExtra("InstrumentLine", line);
						
						//Operating on checkbox hashmap
						ArrayList<Boolean> newLine = new ArrayList<Boolean>();
						for(int i=0;i<4;i++){
							newLine.add(false);
						}
						data.get(0).getData().put(selectedFile, newLine);
						Log.v("parcelable", "switch/add "+data.get(0));
						intent.putParcelableArrayListExtra("checkbox", data);
						startActivity(intent);
					}
				});
				child.addView(temp);
			}
		}
		
		view.addView(child);
		
//		for(int i=0;i<res.size();i++){
//			view.addView(res.get(i), LayoutParams.WRAP_CONTENT);
//		}
		this.setContentView(view);
	}
	
	
}
