package com.example.barca.pengogame;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.Random;

public class MovingObject {
    private int posX;
    private int posY;
    private int velocityX;
    private int velocityY;
    private int speed;
    private ID id;
    private int counter;
    private boolean firstMove;

    MovingObject(int posX, int posY, int velocityX, int velocityY, ID id){
        this.posX = posX;
        this.posY = posY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        if(id != ID.Ice){
            speed = 4;
        }
        else speed = 1;
        this.id = id;
        counter = 1;
        firstMove = true;
    }


    public void setPosition(int posX, int posY){
        this.posY = posY;
        this.posX = posX;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }
    public ID getID(){
        return id;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
    public boolean Move(GameView game, GameBoard board){
        if(counter%speed != 0) {
            counter++;
            return true;
        }
        if(id == ID.Enemyl || id == ID.Enemyr) {
            if(counter != 0 && counter%8 == 0)
            RandomVelocity();
            if(velocityY > 0) {
                board.setAtPosition(posX, posY, ID.Enemyr);
                id = ID.Enemyr;
            }
            else {
                board.setAtPosition(posX, posY, ID.Enemyl);
                id = ID.Enemyl;
            }
            //Log.d("Enemy", "enemyRandom ");

        }
        int nextX = posX + velocityX;
        int nextY = posY + velocityY;
        ID next = board.getAtPosition(nextX, nextY);

        if( next == ID.Empty){
            board.setAtPosition(posX, posY, ID.Empty);
            posX = nextX;
            posY = nextY;
            board.setAtPosition(nextX, nextY, id);
            if(id==ID.Player) setVelocity(0,0);
            firstMove = false;
        }
        else if(id == ID.Icecrushed){
            board.setAtPosition(posX, posY, ID.Empty);
            game.RemoveObject(posX, posY, ID.Icecrushed);
            id = ID.Empty;
            velocityY = 0;
            velocityX =0;
        }
        else if(id == ID.Ice){
            if(next == ID.Ice && firstMove){
                board.setAtPosition(posX, posY, ID.Icecrushed);
                id = ID.Icecrushed;
                velocityX = 0;
                velocityY =0;
            }
            else if(next == ID.Enemyr || next == ID.Enemyl){
                if(game.RemoveObject(nextX, nextY, ID.Enemyl)){
                    board.EnemyDead();
                    board.setAtPosition(nextX, nextY, ID.Empty);
                }
            }
            else
            game.RemoveObject(this);
        }
        else if(id == ID.Enemyl || id == ID.Enemyr){
            if(next == ID.Player){
                board.EraseP();
            }
            if(next == ID.Ice || next == ID.Wall){
                velocityY*=-1;
                velocityY*=-1;
            }
        }
        else if(id == ID.Player){
            if(next == ID.Ice) return false;
            if(next == ID.Enemyl || next == ID.Enemyr){
                board.EraseP();
            }
            else {

                board.setPlayerX(posX);
                board.setPlayerY(posY);
            }
        }

        counter++;
        return true;

    }
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return  posY;
    }
    public void setVelocity(int x, int y){
        velocityX = x;
        velocityY = y;
    }

    public void RandomVelocity(){
        Random r = new Random();
        int vel = r.nextInt(4);

        switch(vel) {
            case 0:
                velocityX = 1;
                velocityY = 0;  //dol;
                break;
            case 1:
                velocityX = -1;
                velocityY = 0;  // nahoru
                break;
            case 2:
                velocityY = 1;
                velocityX = 0; //do prava
                break;
            case 3:
                velocityY = -1;
                velocityX = 0; //do leva
                break;
        }
    }
}
