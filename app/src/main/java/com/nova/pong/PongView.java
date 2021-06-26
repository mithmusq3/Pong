package com.nova.pong;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

class PongView extends SurfaceView implements Runnable{


    Thread gameThread = null;
    SurfaceHolder holder;

    volatile boolean playing;
    boolean paused = true;
    Canvas canvas;
    Paint paint;

    long FPS;

    int gScreenX;
    int gScreenY;

    Plank myPlank;
    Ball myBall;

    SoundPool sp ;

    int beep1ID = -1;
    int beep2ID = -1;
    int beep3ID = -1;
    int loseLifeID = -1;
    int explodeID = -1;

    int myScore = 0;

    int myLives = 3;

    public PongView(Context context, int x, int y) {
        super(context);


        gScreenX =x;
        gScreenY=y;
        holder =getHolder();
        paint = new Paint();

        myPlank = new Plank(gScreenX, gScreenY);

        myBall = new Ball(gScreenX, gScreenY);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }



        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("beep1.ogg");
            beep1ID = sp.load(descriptor,0);

            descriptor = assetManager.openFd("beep2.ogg");
            beep2ID = sp.load(descriptor,0);

            descriptor = assetManager.openFd("beep3.ogg");
            beep3ID = sp.load(descriptor,0);

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = sp.load(descriptor, 0);

            descriptor = assetManager.openFd("explode.ogg");
            explodeID = sp.load(descriptor, 0);

        }catch (IOException e){
            Log.e("error","failed to load sound files");
        }

        setupAndRestart();


    }

    public void setupAndRestart(){
        myBall.reset(gScreenX, gScreenY);
        if(myLives == 0){
            myScore=0;
            myLives =3;
        }
    }









    @Override
    public void run() {
        while (playing){

            long startFrameTime = System.currentTimeMillis();

            if(!paused){
                update();
            }

           draw();

             long timeThisFrame = System.currentTimeMillis() -startFrameTime;
             if(timeThisFrame >= 1){
                 FPS = 1000/timeThisFrame;
             }
        }
    }

    public  void update(){
        myPlank.update(FPS);
        myBall.update(FPS);

        if (RectF.intersects(myPlank.getRect(),myBall.getRect())){
            myBall.setRandomXSpeed();
            myBall.ySpeedReverse();
            myBall.clearObsY(myPlank.getRect().top-2);
            myScore++;
            myBall.increaseSpeed();
            sp.play(beep1ID,1,1,0,0,1);
        }

        if (myBall.getRect().bottom > gScreenY){
            myBall.ySpeedReverse();
            myBall.clearObsY(gScreenY-2);

            myLives--;
            sp.play(loseLifeID,1,1,0,0,1);

            if(myLives==0){
                paused=true;
                setupAndRestart();
            }
        }

        if (myBall.getRect().top<0){
            myBall.ySpeedReverse();
            myBall.clearObsY(12);
            sp.play(beep2ID,1,1,0,0,1);
        }

        if (myBall.getRect().left<0){
            myBall.xSpeedReverse();
            myBall.clearObsX(2);
            sp.play(beep3ID,1,1,0,0,1);
        }

        if (myBall.getRect().right>gScreenX){
            myBall.xSpeedReverse();
            myBall.clearObsX(gScreenX-22);
            sp.play(beep3ID,1,1,0,0,1);
        }
    }



    public void draw(){

        if (holder.getSurface().isValid()){

            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255,0,0,197));
            paint.setColor(Color.argb(255,255,255,255));

            canvas.drawRect(myPlank.getRect(),paint);
            canvas.drawRect(myBall.getRect(),paint);

            paint.setColor(Color.argb(255,255,255,255));

            paint.setTextSize(40);
            canvas.drawText("Score "+ myScore + " Lives " + myLives ,10 ,50,paint );

            holder.unlockCanvasAndPost(canvas);

            
        }
    }

    public void pause(){
        playing = false;
        try{
            gameThread.join();
        }catch (InterruptedException e){
            Log.e("Error:","joining thread");
        }
    }

    public void resume(){
        playing = true;
        gameThread= new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {


        switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                paused = false;

                if(motionEvent.getX() > gScreenX/2){
                    myPlank.setMovementState(myPlank.RIGHT);
                }
                else{
                    myPlank.setMovementState(myPlank.LEFT);
                }
                break;

            case MotionEvent.ACTION_UP:
                myPlank.setMovementState(myPlank.STOPPED);
                break;
        }
return true;
    }
}