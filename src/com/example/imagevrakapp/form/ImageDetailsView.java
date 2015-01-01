package com.example.imagevrakapp.form;

import com.example.imagevrakapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ImageDetailsView extends RelativeLayout {

	Context context;
	
	ImageView image;
	TextView comment;
	TextView header;
	TextView date;
	
	public ImageDetailsView(Context context, ImageView image, String comment, String header, String date) {
		super(context);
		this.context = context;
		addView(getImageView(image));
		Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
		int h = bitmap.getHeight();
		addView(getComment(comment, h));
		addView(getHeader(header, h));
		addView(getDate(date, h));
	}

	private View getHeader(String header2, int h) {
		LinearLayout layout = new LinearLayout(context);
		this.header = new TextView(context);
		this.header.setText(getResources().getString(R.string.header) + header2);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(20, h + 50, 0, 0);
		this.header.setLayoutParams(params);
		layout.addView(this.header);
		return layout;
	}

	private View getDate(String date2, int h) {
		LinearLayout layout = new LinearLayout(context);
		this.date = new TextView(context);
		this.date.setText(getResources().getString(R.string.date) + date2);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(20, h + 120, 0, 0);
		this.date.setLayoutParams(params);
		layout.addView(this.date);
		return layout;
	}

	private View getComment(String comment2, int h) {
		LinearLayout layout = new LinearLayout(context);
		comment = new TextView(context);
		comment.setText(getResources().getString(R.string.comment) + comment2);
		TableLayout.LayoutParams firstText = new TableLayout.LayoutParams();
		firstText.setMargins(20, h + 85, 0, 0);
		comment.setLayoutParams(firstText);
		layout.addView(comment);
		return layout;
	}

	private View getImageView(ImageView image) {
		LinearLayout layout = new LinearLayout(context);
		this.image = new ImageView(context);
		Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
		this.image.setImageBitmap(bitmap);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(20, 50, 0, 20);
		this.image.setLayoutParams(params);
		layout.removeAllViews();
		layout.addView(this.image);
		return layout;
	}

}
