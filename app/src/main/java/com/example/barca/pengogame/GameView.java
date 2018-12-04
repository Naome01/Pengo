package com.example.barca.pengogame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    Canvas canvas;

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
        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);

        /*enemyTex = findViewById(R.id.enemyText);
        levelTex = findViewById(R.id.levelText);
        livesTex = findViewById(R.id.livesText);
        enemyTex.setText("x " + board.getNumOfEnemy());
        levelTex.setText("Level: 1");
        livesTex.setText("4 x");*/
        //while(true) Log.d("TestLoop", "testing");
    }
    public void update()  {

        for(int i =1; i< objects.size(); i++){
            objects.get(i).Move(this,board);
            if(objects.get(i).getID() == ID.Empty) objects.remove(i);
        }
        boolean secondDown = !objects.get(0).Move(this,board);
        if(firstDown && secondDown) {
            MovingObject player = (objects.get(0));
            MovingObject ice = new MovingObject(player.getPosX()+player.getVelocityX(), player.getPosY()+player.getVelocityY(), player.getVelocityX(), player.getVelocityY(), ID.Ice);
            objects.add(ice);
        }
        else firstDown = secondDown;

        playerY = board.getPlayerY();
        playerX = board.getPlayerX();

       /* int tmp = board.checkBoard();
        if (tmp == -1) {
            this.gameThread.setRunning(false);
            Log.d("Loop", "GAME OVER");
            return;
        }
        else if(tmp == 1){
            this.gameThread.setRunning(false);
            Log.d("Loop", "WIN!!");
            return;
        }*/
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

        width = canvas.getWidth() /ly;
        height = canvas.getHeight() /lx;
        for (int i = 0; i < lx; i++) {
            for (int j = 0; j < ly; j++) {

                int id = ID.valueOf(board.getAtPosition(i,j).name()).ordinal();
                canvas.drawBitmap(bmp[id], null,
                        new Rect(j*width, i*height,(j+1)*width, (i+1)*height), null);
            }
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
        objects = new ArrayList<>();
        objects = board.updateHandler(objects);

        this.gameThread = new GameThread(this, holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();

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
}
