package com.nova.pong;

import android.graphics.RectF;

class Plank {

    private RectF pRect;

    private float pLength;
    private float pHeight;

    private float xCo;
    private float yCo;
    private float pSpeed;
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    private int pMoving = STOPPED;

    private int screenX;
    private int screenY;

    public Plank(int x, int y){

        screenX = x;
        screenY = y;

        pLength = screenX/5;
        pHeight = screenY/25;
        xCo = screenX/2;
        yCo = screenY-20;

        pRect = new RectF(xCo, yCo, xCo+pLength ,yCo+pHeight);

        pSpeed = screenX;

    }

    public RectF getRect(){
        return pRect;
    }

    public void setMovementState(int state){
        pMoving = state;
    }

    public void update(long fps){

        if(pMoving == LEFT){
            xCo = xCo - (pSpeed/fps);
        }

        if(pMoving == RIGHT){
            xCo = xCo + (pSpeed/fps);
        }
        // not leaving the screen
        if(pRect.left < 0){
            xCo=0;
        }
        if (pRect.right > screenX){
            xCo = screenX - (pRect.right-pRect.left);
        }

        pRect.left = xCo;
        pRect.right = xCo + pLength;


    }


}