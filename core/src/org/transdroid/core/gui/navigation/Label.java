/* 
 * Copyright 2010-2013 Eric Kok et al.
 * 
 * Transdroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Transdroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Transdroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.transdroid.core.gui.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.transdroid.core.gui.lists.SimpleListItem;
import org.transdroid.daemon.Torrent;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Represents some label that is active or available on the server.
 * @author Eric Kok
 */
public class Label implements SimpleListItem, NavigationFilter, Comparable<Label> {

	private static String unnamedLabelText = null;

	private final String name;
	private final int count;

	public Label(org.transdroid.daemon.Label daemonLabel) {
		this.name = daemonLabel.getName();
		this.count = daemonLabel.getCount();
	}

	@Override
	public String getName() {
		if (TextUtils.isEmpty(this.name))
			return unnamedLabelText;
		return this.name;
	}

	public int getCount() {
		return count;
	}

	/**
	 * Returns true if the torrent label's name matches this (selected) label's name, false otherwise
	 */
	@Override
	public boolean matches(Torrent torrent) {
		return torrent.getLabelName() != null && torrent.getLabelName().equals(name);
	}

	@Override
	public int compareTo(Label another) {
		return this.name.compareTo(another.getName());
	}

	/**
	 * Converts a list of labels as retrieved from a server daemon into a list of labels that can be used in the UI as
	 * navigation filters.
	 * @param daemonLabels The raw list of labels as received from the server daemon adapter
	 * @param unnamedLabel The text to show for the empty label (i.e. the unnamed label)
	 * @return A label items that can be used in a filter list such as the action bar spinner
	 */
	public static ArrayList<Label> convertToNavigationLabels(List<org.transdroid.daemon.Label> daemonLabels,
			String unnamedLabel) {
		if (daemonLabels == null)
			return null;
		unnamedLabelText = unnamedLabel;
		ArrayList<Label> localLabels = new ArrayList<Label>();
		for (org.transdroid.daemon.Label label : daemonLabels) {
			localLabels.add(new Label(label));
		}
		Collections.sort(localLabels);
		return localLabels;
	}

	private Label(Parcel in) {
		this.name = in.readString();
		this.count = in.readInt();
	}

	public static final Parcelable.Creator<Label> CREATOR = new Parcelable.Creator<Label>() {
		public Label createFromParcel(Parcel in) {
			return new Label(in);
		}

		public Label[] newArray(int size) {
			return new Label[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(count);
	}

}
