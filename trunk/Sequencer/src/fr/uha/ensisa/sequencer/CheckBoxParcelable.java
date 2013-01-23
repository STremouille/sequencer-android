package fr.uha.ensisa.sequencer;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckBox;

public class CheckBoxParcelable extends HashMap<String, ArrayList<CheckBox>>
		implements Parcelable {
	int size;

	public CheckBoxParcelable(Parcel in) {
		size = in.readInt();
		for (int i = 0; i < size; i++) {
			String key = in.readString();
			ArrayList<CheckBox> cb = new ArrayList<CheckBox>();
			for (int j = 0; j < 4; j++) {
				int tmp = in.readInt();
				if (tmp == 1)
					cb.get(j).setChecked(true);
			}
			this.put(key, cb);
		}
	}

	public CheckBoxParcelable() {
		super();
		this.size = this.size();
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {

		dest.writeInt(size());
		for (String key : keySet()) {

			dest.writeString(key);
			for (int i = 0; i < 4; i++) {
				if (get(key).get(i).isChecked())
					dest.writeInt(1);
				else
					dest.writeInt(0);
			}
		}
	}

	public CheckBoxParcelable createFromParcel(Parcel source) {
		return new CheckBoxParcelable(source);
	}

	public CheckBoxParcelable[] newArray(int size) {
		return new CheckBoxParcelable[size];
	}

	public static final Parcelable.Creator<CheckBoxParcelable> CREATOR = new Parcelable.Creator<CheckBoxParcelable>() {
		public CheckBoxParcelable createFromParcel(Parcel in) {
			return new CheckBoxParcelable(in);
		}

		public CheckBoxParcelable[] newArray(int size) {
			return new CheckBoxParcelable[size];
		}
	};

}
