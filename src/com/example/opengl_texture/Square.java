package com.example.opengl_texture;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;

public class Square {

	float []vertexs = {
			-1f,1f,0.0f,
			-1f,-1f,0.0f,
			1f,-1f,0.0f,

			1f,-1f,0.0f,
			1f,1f,0.0f,
			-1f,1f,0.0f
	};

	float texture[] = {

			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 1.0f,

			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f,0.0f
	};

	float[] mVMatrix = new float[16];
	float num = 0;
	int mProgram;
	FloatBuffer vertexBuffer;
	FloatBuffer textureBuffer;

	String vertexShaderCode =
			"attribute vec3 vPosition;" +
					"attribute vec2 aTexture;"+
					"varying  vec2 vTexture;"+
					"uniform mat4 mVMatrix; "+//总变换矩阵
					"void main() {" +
					"  gl_Position = mVMatrix*vec4(vPosition,1.0);" +
					"	vTexture = aTexture;"+
					"}";

	String fragmentShaderCode =
			"precision mediump float;" +
					"varying vec2 vTexture;" +
					"uniform sampler2D sTexture;"+
					"void main() {" +
					" gl_FragColor = texture2D(sTexture, vTexture);" +
					"}";

	private Bitmap bitmap;







	public Square(){
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertexs.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertexs);
		vertexBuffer.position(0);

		ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		textureBuffer = tbb.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);

		int vertexShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
		int fragmentShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		GLES20.glShaderSource(vertexShader, vertexShaderCode);
		GLES20.glShaderSource(fragmentShader,fragmentShaderCode);

		GLES20.glCompileShader(vertexShader);
		GLES20.glCompileShader(fragmentShader);

		mProgram = GLES20.glCreateProgram();  
		GLES20.glAttachShader(mProgram, vertexShader);   
		GLES20.glAttachShader(mProgram, fragmentShader);
		GLES20.glLinkProgram(mProgram);


	}
	public void draw(){

		num+=1;

		GLES20.glUseProgram(mProgram);
		int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		int mTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTexture");
		int muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "mVMatrix"); 

		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glEnableVertexAttribArray(mTextureHandle);  

		GLES20.glVertexAttribPointer(mPositionHandle, 3,GLES20.GL_FLOAT, false,0,vertexBuffer);
		GLES20.glVertexAttribPointer(mTextureHandle, 2, GLES20.GL_FLOAT, false,0,textureBuffer); 

		int[] textures = new int[1];//生成纹理id
		GLES20.glGenTextures(1, textures, 0);

		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);//绑定纹理id
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
		try {

			bitmap = BitmapFactory.decodeResource(MyApplication.getInstance().getResources(), R.drawable.bw32);//MyApplication.getInstance().getResources()

			GLUtils.texImage2D
			(
					GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
					0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
					bitmap, 			  //纹理图像
					0					  //纹理边框尺寸
					);




		} catch (Exception e) {
			// TODO: handle exception
		}finally{

			bitmap.recycle();
		}

		Matrix.setIdentityM(mVMatrix, 0);
		Matrix.scaleM(mVMatrix, 0, 0.2f, 0.2f, 0.2f);
		Matrix.rotateM(mVMatrix, 0, num, 0.0f, 1.0f, 0.0f);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1,false,mVMatrix,0);

		// 画三角形
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);


	}

}
