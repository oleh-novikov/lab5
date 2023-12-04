package com.example.cube3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        renderer = new MyGLRenderer(context);
        setRenderer(renderer);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }
}