package com.example.barca.pengogame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import static java.lang.Math.abs;

/*******************************************************************************
 *
 *
 * @author Bára Groňová
 */
public class GameView extends View{

    TextView enemyTex;
    TextView levelTex;
    TextView livesTex;
    Bitmap[] bmp;

    int lx = 20;
    int ly = 13;
    GameBoard board;
    int width;
    int height;

    int playerX = 10;
    int playerY = 5;

    float xDown;
    float yDown;

    float playerWorldX;
    float playerWorldY;
    boolean firstDown = false;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        bmp = new Bitmap[7];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.enemyl);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.enemyr);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.ice);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.icecrushed);
        bmp[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pengo);

        board = new GameBoard(1);
        lx = board.getxSize();
        ly = board.getySize();

        /*enemyTex = findViewById(R.id.enemyText);
        levelTex = findViewById(R.id.levelText);
        livesTex = findViewById(R.id.livesText);
        enemyTex.setText("x " + board.getNumOfEnemy());
        levelTex.setText("Level: 1");
        livesTex.setText("4 x");*/
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / ly;
        height = h / lx;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                yDown = event.getX();
                xDown = event.getY();
                Log.d("GameView", "X" + xDown);
                playerWorldX = (height * playerX) + (height/2);
                playerWorldY = (width * playerY) + (width/2);

                ResolveDirection();
                firstDown = true;
            break;

        }
        return super.onTouchEvent(event);
    };
    protected void ResolveDirection(){
        float resX = abs(xDown - playerWorldX);
        float resY = abs(yDown - playerWorldY);

        if(resX >= resY) {


            if (xDown > playerWorldX ) {

                MoveDown();
            }

            if (xDown < playerWorldX) {

                MoveUp();
            }
        }
        else {
            if (yDown < playerWorldY) {

                MoveLeft();
            }
            if (yDown > playerWorldY) {

                MoveRight();
            }
        }

    }

    protected void MoveRight(){
        if(playerY < ly-1){
        int nextX = playerX;
        int nextY = playerY+1;
        Move(nextX, nextY);}

    }

    protected void MoveLeft(){
        if(playerY > 0) {

            int nextX = playerX;
            int nextY = playerY - 1;
            Move(nextX, nextY);
        }
    }

    protected void MoveUp(){
        if(playerX > 0) {

            int nextX = playerX - 1;
            int nextY = playerY;
            Move(nextX, nextY);
        }
    }
    protected void MoveDown(){
        if(playerX < lx-1) {

            int nextX = playerX + 1;
            int nextY = playerY;
            Move(nextX, nextY);
        }
    }
    protected void Move(int nextX, int nextY){
        ID next = board.getAtPosition(nextX, nextY);
        if( next == ID.Empty){
            board.setAtPosition(playerX, playerY, ID.Empty);
            playerX = nextX;
            playerY = nextY;
            board.setAtPosition(nextX, nextY, ID.Player);

        }

        else if(firstDown && next == ID.Ice){
            int dirX = nextX - playerX;
            int dirY = nextY - playerY;
            MoveIce(nextX, nextY, dirX, dirY);
            board.setAtPosition(playerX, playerY, ID.Empty);
            playerX = nextX;
            playerY = nextY;
            board.setAtPosition(nextX, nextY, ID.Player);
        }
        invalidate(); //znovu zavola Draw a prekresli
        firstDown = false;
    }
    protected void MoveIce(int actX, int actY, int dirX, int dirY){
        int nextX = actX + dirX;
        int nextY = actY + dirY;
Log.d("MoveIce", "NextX: " +nextX + " NExtY: "+nextY);
        ID next = board.getAtPosition(nextX, nextY);

        if(next == ID.Ice || next == ID.Wall){
            //board.setAtPosition(actX, actY, ID.Icecrushed);
            return;
        }
        else if (next == ID.Empty) {
            board.setAtPosition(actX, actY, ID.Empty);
            board.setAtPosition(nextX, nextY, ID.Ice);

        }
        else if(next == ID.Enemyl || next == ID.Enemyr)
        {
            board.setAtPosition(actX, actY, ID.Empty);
            board.setAtPosition(nextX, nextY, ID.Ice);
            board.EnemyDead();
            //enemyTex.setText("x " + board.getNumOfEnemy());

        }
        MoveIce(nextX, nextY, dirX, dirY) ;

    }
    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d("GameView", "Xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + xDown);

        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {
                int id = ID.valueOf(board.getAtPosition(i,j).name()).ordinal();
                canvas.drawBitmap(bmp[id], null,
                        new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
            }
        }

    }
}
