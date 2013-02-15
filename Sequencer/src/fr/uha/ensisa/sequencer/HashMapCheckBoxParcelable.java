package fr.uha.ensisa.sequencer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import android.R.integer;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.CheckBox;

public class HashMapCheckBoxParcelable implements Parcelable {
	private HashMap<String,ArrayList<Boolean>> data;
	
	public HashMapCheckBoxParcelable(){
		super();
		data = new HashMap<String, ArrayList<Boolean>>();
		TreeMap<Integer, String> treemap = SequencerActivity.initInstr();
		for(int i : treemap.keySet())
		{
			ArrayList<Boolean> a= new ArrayList<Boolean>();
			for(int j=0;j<4;j++)
			{
				a.add(false);
			}
			data.put(treemap.get(i), a);
			Log.v("parcelable", "init data"+this);
		}
	}
	
	public HashMapCheckBoxParcelable(Parcel src){
		super();
		int lineNumber=src.readInt();
		data=new HashMap<String, ArrayList<Boolean>>();
		for(int line=0;line<lineNumber;line++){
			ArrayList<Boolean> l = new ArrayList<Boolean>();
			String key = src.readString();
			for(int i=0;i<4;i++){
				if(src.readInt()==1)
				{
					l.add(true);
				}
				else
				{
					l.add(false);
				}
			}
			data.put(key, l);
		}
	}
	
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		Log.v("parcelable", "Start Write to Parcel");
		Log.v("parcelable", data.size()+"");
		dest.writeInt(data.size()); //size of the map : number of loop to retreive all checkboxes
		
		
		for(String instrument : data.keySet()){
			//Content of the CheckBox arraylist : 1 if checked, 0 if not
			dest.writeString(instrument);
			for(int j=0;j<4;j++){
				//Log.v("parcelable", String.valueOf(data.get(String.valueOf(i)).get(j).isChecked()));
				if(data.get(String.valueOf(instrument)).get(j)==true)
					dest.writeInt(1);
				else
					dest.writeInt(0);
			}
		}
		Log.v("parcelable", "End Write to Parcel");
	}
	
	public HashMap<String, ArrayList<Boolean>> getData() {
		return data;
	}

	public void setData(HashMap<String, ArrayList<Boolean>> data) {
		this.data = data;
	}

	public static final Parcelable.Creator<HashMapCheckBoxParcelable> CREATOR = new Parcelable.Creator<HashMapCheckBoxParcelable>() {

		public HashMapCheckBoxParcelable createFromParcel(Parcel source) {
			return new HashMapCheckBoxParcelable(source);
		}

		public HashMapCheckBoxParcelable[] newArray(int size) {
			return new HashMapCheckBoxParcelable[size];
		}
		
	};
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(data.size()+":");
		for(String s : data.keySet()){
			builder.append(s);
			for(int i=0;i<4;i++){
				if(data.get(s)
						.get(i)==true)
					builder.append('+');
				else
					builder.append('-');
			}
			builder.append("||||");
		}
		return builder.toString();
	}
}



