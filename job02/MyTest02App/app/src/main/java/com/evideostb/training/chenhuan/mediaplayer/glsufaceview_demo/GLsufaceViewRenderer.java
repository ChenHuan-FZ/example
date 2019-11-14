package com.evideostb.training.chenhuan.mediaplayer.glsufaceview_demo;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class GLsufaceViewRenderer implements GLSurfaceView.Renderer
{
	public GLsufaceViewRenderer(boolean useTranslucentBackground)
	{
		mTranslucentBackground = useTranslucentBackground;
		mGLsufaceViewCube = new GLsufaceViewCube();
	}

	public void onDrawFrame(GL10 gl)
	{
		//glClearColor函数设置好清除颜色，glClear利用glClearColor函数设置好的当前清除颜色设置窗口颜色
		gl.glClearColor(0f, 0f, 0f, 1f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW); //对模型视景矩阵堆栈应用随后的矩阵操作.
		gl.glLoadIdentity(); //该函数的功能是重置当前指定的矩阵为单位矩阵
		gl.glTranslatef(0, 0, -5.0f);//将三角形在z轴上移动 //沿X轴正方向平移x个单位(x是有符号数) 沿Y轴正方向平移y个单位(y是有符号数) 沿Z轴正方向平移z个单位(z是有符号数)
		gl.glRotatef(mAngle, 1, 0, 0); //angle为旋转的角度

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY); //启用顶点数组
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY); //启用颜色数组

		mGLsufaceViewCube.draw(gl);

		gl.glTranslatef(5.0f, 0, 0);
		gl.glRotatef(mAngle , 0, 1, 1);
		mGLsufaceViewCube.draw(gl);

		mAngle += 1.0f;
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) //视图发生变化的时候调用
	{
		//一般不直接设置视口大小，而是利用glviewport 设置场景大小，再利用glFrustumf来设定大小
		//one的大小是投影在第一层时候观察的大小，显示为整个openglsurfaceview的控件中宽的大小
		// 当为folat类型数据时候，1代表的屏幕宽度

		//设置OpenGL场景的大小
		gl.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		//设置当前矩阵模式:
		gl.glMatrixMode(GL10.GL_PROJECTION); //GL_PROJECTION,对投影矩阵应用随后的矩阵操作.
		gl.glLoadIdentity(); //将以前的改变都清掉
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10); //设置视角大小
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) //创建的时候调用好
	{

		gl.glDisable(GL10.GL_DITHER);

		//glHint 函数指定特定于实现的提示
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST); //GL_FASTEST：选择速度最快选项。

		if (mTranslucentBackground)
		{
			gl.glClearColor(0, 0, 0, 0);
		}
		else
		{
			gl.glClearColor(1, 1, 1, 1);
		}
		gl.glEnable(GL10.GL_CULL_FACE);
		//设置着色模式
		//光滑着色时（即GL_SMOOTH），独立的处理图元中各个顶点的颜色。
		// 对于线段图元，线段上各点的颜色将根据两个顶点的颜色通过差值得到。
		// 对于多边形图元，多边形内部区域的颜色将根据所有顶点的颜色差值得到。
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
	}

	private boolean mTranslucentBackground;
	private GLsufaceViewCube mGLsufaceViewCube;
	private float mAngle;
}
