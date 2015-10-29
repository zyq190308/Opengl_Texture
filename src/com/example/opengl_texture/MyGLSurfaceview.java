package com.example.opengl_texture;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class MyGLSurfaceview extends GLSurfaceView {

	MyRenderer renderer;
	public MyGLSurfaceview(Context context) {
		super(context);
		this.setEGLContextClientVersion(2);
		renderer = new MyRenderer();
		this.setRenderer(renderer);
		
	}



}
