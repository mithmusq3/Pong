package com.nova.pong;

import android.graphics.RectF;

import java.util.Random;

class Ball {


    private RectF rect;
    private  float xSpeed;
    private float ySpeed;
    private float ballWidth,ballHeight;

    public Ball(int ScreenX, int ScreenY) {

        ballWidth = ScreenX/50;
        ballHeight = ballWidth;


        ySpeed = ScreenY/4;
        xSpeed = ySpeed;


        rect = new RectF();

    }

    public RectF getRect(){
        return rect;
    }


    public void update(long fps){

        rect.left = rect.left +(xSpeed/fps);
        rect.top = rect.top +(ySpeed/fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }


    public void xSpeedReverse(){
        xSpeed = -xSpeed;
    }

    public void ySpeedReverse(){
        ySpeed = -ySpeed;
    }


    public void setRandomXSpeed(){

        Random r = new Random();
        int ans = r.nextInt(2);

        if(ans == 0){
            xSpeedReverse();
        }
    }


    public void increaseSpeed(){

        xSpeed = xSpeed +xSpeed/10;
        ySpeed = ySpeed +ySpeed/10;

    }

    public void clearObsY(float y){
        rect.bottom = y;
        rect.top = y - ballHeight;
    }

    public  void clearObsX(float x){
        rect.left = x;
        rect.right = x+ballWidth;

    }

    public  void reset(int x,int y){

        rect.left=x/2;
        rect.top= y-20;
        rect.right =x/2+ballWidth;
        rect.bottom = y -20 -ballHeight;
    }

}