package fr.uha.ensisa.sequencer;

import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FileChooserActivity extends Activity{
	AssetManager am;
	String[] ressources;
	String selectedFile = "";
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.filechooser);
		am = this.getAssets();
		try {
			ressources=am.list("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("myassets", "Start");
		context = this;
		//Build layout
		LinearLayout view = new LinearLayout(this);
		view.setOrientation(LinearLayout.VERTICAL);
		
		//ArrayList<Button> res = new ArrayList<Button>();
		for(String fileInAsset : ressources){
			Log.i("myassets", fileInAsset);
			if(!(fileInAsset.equals("images")||fileInAsset.equals("sounds")||fileInAsset.equals("webkit")))
			{
				final Button temp = new Button(this);
				temp.setText(fileInAsset);
				temp.setOnClickListener( new View.OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						selectedFile = (String) temp.getText();
						Intent intent = new Intent(context, SequencerActivity.class);
						intent.putExtra("newSound", selectedFile);
						startActivity(intent);
					}
				});
				view.addView(temp);
			}
		}
		
//		for(int i=0;i<res.size();i++){
//			view.addView(res.get(i), LayoutParams.WRAP_CONTENT);
//		}
		this.setContentView(view);
	}
	
	
}
