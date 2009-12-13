package com.google.code.androidspycam;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;

public class Util {
	public static Drawable fetchImage(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return Drawable.createFromStream((InputStream)content, "src");
	}
}
