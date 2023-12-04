# lab5
___Лабораторна робота № 5: «Створення 3D об'єктів та додавання текстур»___

Хід роботи

package com.example.cube3d;

        import android.opengl.GLES20;
        import android.opengl.Matrix;
        import android.view.MotionEvent;
        import java.nio.ByteBuffer;
        import java.nio.ByteOrder;
        import java.nio.FloatBuffer;
        import java.nio.ShortBuffer;

public class TexturedCube {
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.2f };
    private final int vertexCount=vertices.length/COORDS_PER_VERTEX;
    private int numFaces = 6;
    private int colorHandle;
    private int mvpMatrixHandle;
    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 aPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * aPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 uColor;" +
                    "void main() {" +
                    "  gl_FragColor = uColor;" +
                    "}";
    private int mvpMatrixHandle;
    private int positionHandle;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int program;
    private float[][] colors = {
            {1.0f, 0.5f, 0.0f, 1.0f},  // 0. orange
            {1.0f, 0.0f, 1.0f, 1.0f},  // 1. violet
            {0.0f, 1.0f, 0.0f, 1.0f},  // 2. green
            {0.0f, 0.0f, 1.0f, 1.0f},  // 3. blue
            {1.0f, 0.0f, 0.0f, 1.0f},  // 4. red
            {1.0f, 1.0f, 0.0f, 1.0f}
    };

    static final int COORDS_PER_VERTEX = 3;
    static float vertices[] = {
            -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
            0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
            -0.2f,  0.2f,  0.2f,  // 2. left-top-front
            0.2f,  0.2f,  0.2f,  // 3. right-top-front
            // BACK
            0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
            -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
            0.2f,  0.2f, -0.2f,  // 7. right-top-back
            -0.2f,  0.2f, -0.2f,  // 5. left-top-back
            // LEFT
            -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
            -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
            -0.2f,  0.2f, -0.2f,  // 5. left-top-back
            -0.2f,  0.2f,  0.2f,  // 2. left-top-front
            // RIGHT
            0.2f, -0.2f,  0.2f,  // 1. right-bottom-front
            0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
            0.2f,  0.2f,  0.2f,  // 3. right-top-front
            0.2f,  0.2f, -0.2f,  // 7. right-top-back
            // TOP
            -0.2f,  0.2f,  0.2f,  // 2. left-top-front
            0.2f,  0.2f,  0.2f,  // 3. right-top-front
            -0.2f,  0.2f, -0.2f,  // 5. left-top-back
            0.2f,  0.2f, -0.2f,  // 7. right-top-back
            // BOTTOM
            -0.2f, -0.2f, -0.2f,  // 4. left-bottom-back
            0.2f, -0.2f, -0.2f,  // 6. right-bottom-back
            -0.2f, -0.2f,  0.2f,  // 0. left-bottom-front
            0.2f, -0.2f,  0.2f   // 1. right-bottom-front
    };
    short[] indices = {
            0, 1, 2, 2, 1, 3, 5, 4, 7, 7, 4, 6, 8, 9, 10, 10, 9, 11, 12, 13, 14, 14, 13, 15, 16, 17, 18, 18, 17, 19, 22, 23, 20, 20, 23, 21
    };
    public TexturedCube() {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length * 2).order(ByteOrder.nativeOrder()).asShortBuffer();
        indexBuffer.put(indices).position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }
    public void draw(float[] modelViewProjectionMatrix) {
        GLES20.glUseProgram(program);

        positionHandle = GLES20.glGetAttribLocation(program, "aPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        colorHandle = GLES20.glGetUniformLocation(program, "uColor");

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, modelViewProjectionMatrix, 0);

        for (int face = 0; face < numFaces; face++) {
            GLES20.glUniform4fv(colorHandle, 1, colors[face], 0);
            indexBuffer.position(face * 6);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        }

        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}


![](https://i.ibb.co/k3hVTdK/Screenshot-24.png)
