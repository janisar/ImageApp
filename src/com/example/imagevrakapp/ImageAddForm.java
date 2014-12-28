package com.example.imagevrakapp;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

public class ImageAddForm extends RelativeLayout{

	public static final int COMMENT_EDITTEXT_ID = 1001;
	public static final int HEADER_EDITTEXT_ID = 1002;
	public static final int SAVE_BUTTON_ID = 1003;
	
	Context context;
	
	EditText comment;
	EditText header;
	
	public ImageAddForm(Context context) {
		super(context);
		this.context = context;
		addView(getHeaderRow(context));
		addView(getCommentRow(context));
	}

	protected LinearLayout getHeaderRow(Context context) {
		LinearLayout layout = new LinearLayout(context);
		header = new EditText(context);
		header.setWidth(500);
		header.setHint("Sisesta pealkiri..");
		header.setId(HEADER_EDITTEXT_ID);
		layout.addView(header);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(20, 50, 0, 0);
		header.setLayoutParams(params);
		return layout;
	}
	
	protected LinearLayout getCommentRow(Context context) {
		LinearLayout layout = new LinearLayout(context);
		comment = new EditText(context);
		comment.setLines(3);
		comment.setHint("Sisesta kommentaar..");
		comment.setId(COMMENT_EDITTEXT_ID);
		comment.setWidth(500);
		TableLayout.LayoutParams params = new TableLayout.LayoutParams();
		params.setMargins(20, 150, 0, 0);
		comment.setLayoutParams(params);
		layout.addView(comment);
		return layout;
	}
	
	public EditText getComment() {
		return comment;
	}
	
	public EditText getHeader() {
		return header;
	}
}
