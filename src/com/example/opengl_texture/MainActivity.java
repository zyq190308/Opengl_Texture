package com.example.opengl_texture;



import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		MyGLSurfaceview glSurfaceview = new MyGLSurfaceview(this);
		setContentView(glSurfaceview);





	}

}
