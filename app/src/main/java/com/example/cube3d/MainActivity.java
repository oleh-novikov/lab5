package com.example.cube3d;


import androidx.activity.ComponentActivity;

import android.os.Bundle;

public class MainActivity extends ComponentActivity {
    private MyGLSurfaceView gLsurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLsurfaceView=new MyGLSurfaceView(this);
        setContentView(gLsurfaceView);
    }
}

