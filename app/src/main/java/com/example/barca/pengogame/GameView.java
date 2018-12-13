package com.example.barca.pengogame;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static java.lang.Math.abs;

/*******************************************************************************
 *
 *
 * @author Bára Groňová
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback{

    TextView enemyTex;
    TextView levelTex;
    TextView livesTex;
    private GameThread gameThread;
    private ArrayList<MovingObject> objects;
    Bitmap[] bmp;
    boolean updating = true;

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

    DBHelper PendoDB;
    GameHolder gameHolder;


    public GameView(Context context) {
        super(context);
         gameHolder= new GameHolder(context);

        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gameHolder= new GameHolder(context);

        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gameHolder= new GameHolder(context);

        init();
    }

    void init() {
        Log.d("TestLoop", "Init");

        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);
        // Sét callback.
        this.getHolder().addCallback(this);


    }
    public void update()  {
    if(updating) {
        int tmpNum = board.getNumOfEnemy();
        for (int i = 1; i < objects.size(); i++) {
            objects.get(i).Move(this, board);
            if (objects.get(i).getID() == ID.Empty) objects.remove(i);
        }
        boolean secondDown = !objects.get(0).Move(this, board);
        if (firstDown && secondDown) {
            MovingObject player = (objects.get(0));
            MovingObject ice = new MovingObject(player.getPosX() + player.getVelocityX(), player.getPosY() + player.getVelocityY(), player.getVelocityX(), player.getVelocityY(), ID.Ice);
            objects.add(ice);
        } else firstDown = secondDown;

        playerY = board.getPlayerY();
        playerX = board.getPlayerX();
        if(tmpNum != board.getNumOfEnemy()){
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.pickup);
            mediaPlayer.start(); // no need to call prepare(); create() does that for you

        }
        enemyTex.setText("x " + board.getNumOfEnemy());
        int tmp = board.checkBoard();
        if (tmp == -1) {
            Log.d("Win or Over", "GAME OVER");
            gameHolder.SetPref(gameHolder.level, gameHolder.lives - 1);

            /*Intent intent = new Intent("kill");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);*/
            lose();
            //
        } else if (tmp == 1) {
            Log.d("Win or Over", "WIN!!");
            gameHolder.SetPref(gameHolder.level + 1, 4);
            //this.gameThread.setRunning(false);
            win();
           // newGame();

        }

    }
    }
    public void win(){
        board.setAtPosition(0,0, ID.Win);
        updating = false;

    }
    public void lose(){
        board.setAtPosition(0,0, ID.Lose);
        updating = false;

    }
    public  boolean RemoveObject(int posX, int posY, ID id){
        for(int i=0; i<objects.size(); i++){
            if(objects.get(i).getPosX() == posX && objects.get(i).getPosY() == posY && objects.get(i).getID() == id){
                objects.remove(i);
                return true;
            }
        }
        return false;
    }

    public  boolean RemoveObject(MovingObject object){
            objects.remove(object);

        return false;
    }

    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);
        if(updating){
        width = canvas.getWidth() /ly;
        height = canvas.getHeight() /lx;
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {

                int id = ID.valueOf(board.getAtPosition(i,j).name()).ordinal();


                canvas.drawBitmap(bmp[id], null,
                        new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);

            }
        }}
        else {
            int id = ID.valueOf(board.getAtPosition(0, 0).name()).ordinal();

            canvas.drawBitmap(bmp[id], null,
                    new Rect(0, 0, canvas.getWidth(), canvas.getHeight()), null);
        }
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
                if(!updating) {
                    updating = true;
                    newGame();
                    return super.onTouchEvent(event);
                }
                yDown = event.getX();
                xDown = event.getY();
                Log.d("GameView", "X" + xDown);
                playerWorldX = (height * playerX) + (height/2);
                playerWorldY = (width * playerY) + (width/2);

                ResolveDirection();
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
        objects.get(0).setVelocity(0, 1);
        }

    }

    protected void MoveLeft(){
        if(playerY > 0) {
            objects.get(0).setVelocity(0, -1);

        }
    }

    protected void MoveUp(){
        if(playerX > 0) {
            objects.get(0).setVelocity(-1, 0);

        }
    }
    protected void MoveDown(){
        if(playerX < lx-1) {
            objects.get(0).setVelocity(1, 0);

        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        bmp = new Bitmap[9];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.enemyl);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.enemyr);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.ice);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.icecrushed);
        bmp[6] = BitmapFactory.decodeResource(getResources(), R.drawable.pengo);
        bmp[7] = BitmapFactory.decodeResource(getResources(), R.drawable.win);
        bmp[8] = BitmapFactory.decodeResource(getResources(), R.drawable.fail);

        //Cursor res = PendoDB.getLastState()

        board = new GameBoard(gameHolder.level);
        lx = board.getxSize();
        ly = board.getySize();
        objects = new ArrayList<>();
        objects = board.updateHandler(objects);

        enemyTex = (TextView) ((Activity)getContext()).findViewById(R.id.enemyText);
        ImageView lives = ((Activity)getContext()).findViewById(R.id.imageView2);
        lives.setImageResource(R.drawable.hearth);

        ImageView enemy = ((Activity)getContext()).findViewById(R.id.imageView);
        enemy.setImageResource(R.drawable.enemyr);
        levelTex = (TextView) ((Activity)getContext()).findViewById(R.id.levelText);
        livesTex = (TextView) ((Activity)getContext()).findViewById(R.id.livesText);
        enemyTex.setText("x " + board.getNumOfEnemy());
        levelTex.setText("Level: " + gameHolder.level);
        livesTex.setText(gameHolder.lives + " x");

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();

    }
    public void newGame(){
        board = new GameBoard(gameHolder.level);
        lx = board.getxSize();
        ly = board.getySize();
        objects = new ArrayList<>();
        objects = board.updateHandler(objects);

         enemyTex.setText("x " + board.getNumOfEnemy());
        levelTex.setText("Level: " + gameHolder.level);
        livesTex.setText(gameHolder.lives + " x");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;

        while(retry) {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }
    public Dialog CreateDialog(boolean win){
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setPositiveButton("MENU", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }});
        if(win) {

            builder.setNegativeButton("NEXT LEVEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setMessage("Level: " + gameHolder.level)
                    .setTitle("YOU WIN");
        }
        else {
            builder.setNegativeButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.setMessage("Level: " + gameHolder.level)
                    .setTitle("YOU LOSE");
        }
        Log.d("Dialog", "Created");
        return builder.create();
    }
}
