package com.example.imagevrakapp;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.imagevrakapp.core.Entry;
import com.example.imagevrakapp.form.ImageAddForm;
import com.example.imagevrakapp.service.web.download.ExtraImagesLoader;
import com.example.imagevrakapp.service.web.download.ImageLoader;
import com.example.imagevrakapp.service.web.upload.Uploader;

public class MainActivity extends ActionBarActivity {

	private static final int CAMERA_REQUEST = 1888;
	private static final String IMAGE_FILE_NAME = "image.jpg";
	
	private RelativeLayout baseLayout;
	private Button imageButton; 
	private ImageView imageView;
	private LinearLayout buttonsLayout;
	private LinearLayout imagesLayout;
	private boolean showButtons = false;
	private boolean showImage = false;
	private ScrollView scrollView;
	private int height;
	private int width;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        baseLayout = (RelativeLayout) findViewById(R.id.base_layout);
        baseLayout.setBackgroundResource(R.drawable.backgroundimage);
        baseLayout.getBackground().setAlpha(150);

        initViewElements();
        runOnUiThread(new ImageLoader(MainActivity.this, imagesLayout));
        
        if (savedInstanceState != null) {
        	if (savedInstanceState.getBoolean("showButtons")) {
				 showButtons = true;
        		 showImageAddButtons();
        	}
        	if (savedInstanceState.getBoolean("showImage")) {
				 showImage = true;
				 showImage();
        	}
        }
	}

	private void initViewElements() {
		initDimensions();
		imageView = (ImageView) findViewById(R.id.imageView1);
        buttonsLayout = (LinearLayout) findViewById(R.id.linearLayout);
        imageButton = (Button) findViewById(R.id.capture_button);
        imageButton.setOnClickListener(getImageButtonListener());
        initImagesLayout();
        initScrollView();
	}
	
	@SuppressLint("NewApi")
	private void initScrollView() {
		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		View v = scrollView.getChildAt(0);
		scrollView.removeView(v);
		scrollView = new ScrollView(MainActivity.this) {
			
			@Override
			protected void onScrollChanged(int l, int t, int oldl, int oldt) {
				View view = (View) getChildAt(getChildCount()-1);
		        int diff = (view.getBottom()-(getHeight()+getScrollY()+view.getTop()));
		        if(diff == 0){  
		            ExtraImagesLoader imageLoader = new ExtraImagesLoader(MainActivity.this, imagesLayout);
					runOnUiThread(imageLoader);
		        }
				super.onScrollChanged(l, t, oldl, oldt);
			}

		};
		scrollView.addView(v);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height - 150);
		params.setMargins(0, 150, 0, 0);

		scrollView.setLayoutParams(params);
		baseLayout.addView(scrollView);
	}

	@SuppressLint("NewApi")
	private void initDimensions() {
		Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
        width = size.x;
	}

	private void initImagesLayout() {
		imagesLayout = (LinearLayout) findViewById(R.id.linearLayout2);

		FrameLayout.LayoutParams imageViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        imageViewParams.setMargins((width - (150 * 3)) / 2 - 35, 0, 0, 0);
        imagesLayout.setLayoutParams(imageViewParams);
	}

	private void showImage() {
		File takenImage = new File(Environment.getExternalStorageDirectory()  + File.separator + IMAGE_FILE_NAME);
		Uri outputFileUri = Uri.fromFile(takenImage);
		imageView.setImageURI(outputFileUri);
		
		int imageBottom = imageView.getBottom();
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height - imageBottom);
        params.setMargins(0, height - imageBottom, 0, 0);
        scrollView.setLayoutParams(params);
	}

	@SuppressLint("NewApi")
	private void showImageAddButtons() {
        final LinearLayout row = new LinearLayout(this);
        row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
  
		Button saveButton = new Button(getApplicationContext());
		saveButton.setText(getResources().getString(R.string.save_image));
		saveButton.setOnClickListener(saveButtonListener());
		Button cancelButton = new Button(getApplicationContext());
		cancelButton.setText(getResources().getString(R.string.forget_image));
		row.addView(saveButton);
		row.addView(cancelButton);
		buttonsLayout.addView(row);
	}
	
	private OnClickListener getImageButtonListener() {
		return new View.OnClickListener() {
			
			@SuppressLint("NewApi")
			public void onClick(View v) {
				showButtons = true;
				String path = Environment.getExternalStorageDirectory()  + File.separator + IMAGE_FILE_NAME;
				File file = new File(path);
				Uri outputFileUri = Uri.fromFile(file);

				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				startActivityForResult(intent, CAMERA_REQUEST); 
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height - 300);
			    params.setMargins(0, 300, 0, 0);
			    scrollView.setLayoutParams(params);
                showImageAddButtons();
				imageView.setVisibility(View.VISIBLE);
				buttonsLayout.setVisibility(View.VISIBLE);
			}
		};
	}
	
	 protected OnClickListener saveButtonListener() {
	    	return new View.OnClickListener() {
	    		
	    		@Override
	    		public void onClick(View v) {
	    			Context context = MainActivity.this;
	    			final ImageAddForm imageForm = new ImageAddForm(context);
	    			AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	    			alertDialog.setView(imageForm);
	    			alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.save), saveImageButtonListener(imageForm));
	    			alertDialog.show();
	    		}
	    	};
		}

		protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	    	if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
	    		File takenImage = new File(Environment.getExternalStorageDirectory()  + File.separator + IMAGE_FILE_NAME);
	    		Uri outputFileUri = Uri.fromFile(takenImage);
	    		imageView.setImageURI(outputFileUri);
	    		showButtons = true;
	    		showImage = true;
	    		
	    		scrollView.setVisibility(View.INVISIBLE);
	    	} else {
	    		Toast.makeText(MainActivity.this, getResources().getString(R.string.image_save_error) + resultCode, Toast.LENGTH_SHORT).show();
	    	}
	    } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("showButtons", showButtons);
		outState.putBoolean("showImage", showImage);
		super.onSaveInstanceState(outState);
	}

	private android.content.DialogInterface.OnClickListener saveImageButtonListener(
			final ImageAddForm imageForm) {
		return new DialogInterface.OnClickListener() {
		    
			@SuppressLint("NewApi")
			public void onClick(DialogInterface dialog, int which) {
				Entry entry = getEntry(imageForm);
				new Uploader().execute(entry);
				imagesLayout.removeAllViewsInLayout();
				runOnUiThread(new ImageLoader(MainActivity.this, imagesLayout));  
				showButtons = false;
				showImage = false;
				scrollView.setVisibility(View.VISIBLE);
				imageView.setVisibility(View.INVISIBLE);
				buttonsLayout.setVisibility(View.INVISIBLE);
		    }
		};
	}

	private Entry getEntry(ImageAddForm imageForm) {
		String comment = imageForm.getComment().getText().toString();
		String header = imageForm.getHeader().getText().toString();
		imageView.buildDrawingCache();
		Bitmap bmap = imageView.getDrawingCache();
		return new Entry(bmap, comment, header);
	}
}
