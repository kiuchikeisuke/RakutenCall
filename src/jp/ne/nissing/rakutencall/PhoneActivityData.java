package jp.ne.nissing.rakutencall;

import android.graphics.drawable.Drawable;

public class PhoneActivityData {
	private String mApplicationName;
	private Drawable mIcon;
	public String getApplicationName() {
		return mApplicationName;
	}
	public void setApplicationName(String ApplicationName) {
		this.mApplicationName = ApplicationName;
	}
	public Drawable getIcon() {
		return mIcon;
	}
	public void setIcon(Drawable mIcon) {
		this.mIcon = mIcon;
	}
}
