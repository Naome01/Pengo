package com.example.barca.pengogame;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/*******************************************************************************
 * Třída {GameBoard} slouží k definování herní desky
 *
 * @author Bára Groňová
 */
public class GameBoard {

    //Rozměry herní desky
    private int xSize = 15;
    private int ySize = 10;
    private int playerX = 10;
    private int playerY = 5;
    private ID[][] board;
    //Počet hráčů (1 nebo 0)
    private int numOfP = 1;
    //Počet Enemy
    private int numOfEnemy;

    /*******************************************************************************
     * 	Vytvoří instanci a vygeneruje herní desku
     */
    public GameBoard(int level)
    {
        if(level > 6){
            xSize = 18;
            ySize = 15;
        }
        board = new ID[xSize][ySize];
        Random r = new Random();
        for(int i = 0; i < xSize; i++)
        {
            for (int j = 0; j < ySize; j++)
            {
                if(i==0 || i== xSize-1) board[i][j] = ID.Wall;

                else if (j == 0 || j == ySize-1) board[i][j] = ID.Wall;
               else  if (r.nextInt(3) == 1) board[i][j] = ID.Ice;
                else board[i][j] = ID.Empty;
            }
        }
        board[playerX][playerY] = ID.Player;
        board[playerX][playerY+1] = ID.Ice;
        board[playerX][playerY-1] = ID.Ice;
        board[playerX+1][playerY] = ID.Ice;
        board[playerX-1][playerY] = ID.Ice;
        int rX, rY;
        int enemy = level;
        if(level >6) enemy = enemy/2+2;
        for(int i = 0; i< enemy; ) {

            rX = r.nextInt(xSize-2)+1;
            rY = r.nextInt(ySize-2)+1;
            if(rX==playerX+1 || rX == playerX -1  || rY == playerY -1 || rY == playerY +1 ){}
            else if(board[rX] [rY] != ID.Player || board[rX] [rY] != ID.Enemyl ) {
                board[rX][rY] = ID.Enemyl;
                this.numOfEnemy++;
                i++;
            }

        }
        //board[xSize-2][ySize-2] = ID.Player;
    }
    /*******************************************************************************
     * @return xSize
     */
    public int getxSize() {
        return this.xSize;
    }
    /*******************************************************************************
     * @return ySize
     */
    public int getySize() {
        return this.ySize;
    }
    /*******************************************************************************
     * @param x x-ová souřadnice
     * @param y y-ová souřadnice
     * @return ID na dané pozici
     */
    public ID getAtPosition(int x, int y) {
        return this.board[x][y];
    }
    /*******************************************************************************
     * Nastaví dané ID na danou pozici herní desky
     * @param x x-ová souřadnice
     * @param y y-ová souřadnice
     * @param id ID, které mábýt nastaveno
     */
    public void setAtPosition(int x, int y, ID id)
    {
        this.board[x][y] = id;
    }

    /*******************************************************************************
     * @return pocet Hráčů (0/1)
     */
    public int getNumOfP() {
        return this.numOfP;
    }
    /*******************************************************************************
     * @return počet Enemy
     */
    public int getNumOfEnemy() {
        return this.numOfEnemy;
    }
    /*******************************************************************************
     * Nastaví počet hráčů na nulu
     */
    public void EraseP() {
        this.numOfP = 0;
    }
    /*******************************************************************************
     * Sníží počet Enemy o 1
     */
    public void EnemyDead() {
        this.numOfEnemy --;

    }

    /*******************************************************************************
     * Kontroluje herní desku
     * @return 1 když již není žádný enemy
     * @return -1 když byl zabit hráč
     * @return 0 pokud neplatí ani jedno z výše uvedeného
     */
    public int checkBoard() {
        if (this.numOfEnemy < 1) return 1;
        if(this.numOfP == 0) return -1;
        else return 0;
    }

    public int getPlayerX(){
        return this.playerX;
    }
    public int getPlayerY(){
        return this.playerY;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public ArrayList<MovingObject> updateHandler(ArrayList<MovingObject> list){
        list.add(0, new MovingObject(playerX, playerY,
                0,0,ID.Player)); // player have to be firts in list
        for(int i = 0; i < xSize; i++)
        {
            for (int j = 0; j < ySize; j++) {
                if(this.getAtPosition(i,j) == ID.Enemyl || this.getAtPosition(i,j) == ID.Enemyr )
                list.add(new MovingObject(i, j, 0, 0, ID.Enemyl));
            }
        }
        return list;
    }

}
