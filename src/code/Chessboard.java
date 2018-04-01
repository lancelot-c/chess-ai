/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Lancelot
 */
public class Chessboard {
    
    private Pawn[][] pawns;
    
    public Chessboard(boolean isClone) {
        
        pawns = new Pawn[Chessgame.SIZE][Chessgame.SIZE];
        
        if (isClone)
            return;
        
        
        for (int i=0; i<Chessgame.SIZE; i++)
            for (int j=0; j<Chessgame.SIZE; j++)
                pawns[i][j] = null;
        
        // Adding computer's pawns
        pawns[0][0] = new Pawn(this, "rook", "computer", new Point(0, 0));
        pawns[1][0] = new Pawn(this, "knight", "computer", new Point(1, 0));
        pawns[2][0] = new Pawn(this, "bishop", "computer", new Point(2, 0));
        pawns[3][0] = new Pawn(this, "queen", "computer", new Point(3, 0));
        pawns[4][0] = new Pawn(this, "king", "computer", new Point(4, 0));
        pawns[5][0] = new Pawn(this, "bishop", "computer", new Point(5, 0));
        pawns[6][0] = new Pawn(this, "knight", "computer", new Point(6, 0));
        pawns[7][0] = new Pawn(this, "rook", "computer", new Point(7, 0));
        pawns[0][1] = new Pawn(this, "pawn", "computer", new Point(0, 1));
        pawns[1][1] = new Pawn(this, "pawn", "computer", new Point(1, 1));
        pawns[2][1] = new Pawn(this, "pawn", "computer", new Point(2, 1));
        pawns[3][1] = new Pawn(this, "pawn", "computer", new Point(3, 1));
        pawns[4][1] = new Pawn(this, "pawn", "computer", new Point(4, 1));
        pawns[5][1] = new Pawn(this, "pawn", "computer", new Point(5, 1));
        pawns[6][1] = new Pawn(this, "pawn", "computer", new Point(6, 1));
        pawns[7][1] = new Pawn(this, "pawn", "computer", new Point(7, 1));
        
        // Adding player's pawns
        pawns[0][7] = new Pawn(this, "rook", "human", new Point(0, 7));
        pawns[1][7] = new Pawn(this, "knight", "human", new Point(1, 7));
        pawns[2][7] = new Pawn(this, "bishop", "human", new Point(2, 7));
        pawns[3][7] = new Pawn(this, "queen", "human", new Point(3, 7));
        pawns[4][7] = new Pawn(this, "king", "human", new Point(4, 7));
        pawns[5][7] = new Pawn(this, "bishop", "human", new Point(5, 7));
        pawns[6][7] = new Pawn(this, "knight", "human", new Point(6, 7));
        pawns[7][7] = new Pawn(this, "rook", "human", new Point(7, 7));
        pawns[0][6] = new Pawn(this, "pawn", "human", new Point(0, 6));
        pawns[1][6] = new Pawn(this, "pawn", "human", new Point(1, 6));
        pawns[2][6] = new Pawn(this, "pawn", "human", new Point(2, 6));
        pawns[3][6] = new Pawn(this, "pawn", "human", new Point(3, 6));
        pawns[4][6] = new Pawn(this, "pawn", "human", new Point(4, 6));
        pawns[5][6] = new Pawn(this, "pawn", "human", new Point(5, 6));
        pawns[6][6] = new Pawn(this, "pawn", "human", new Point(6, 6));
        pawns[7][6] = new Pawn(this, "pawn", "human", new Point(7, 6));
        
        updatePossibleMoves();
    }

    
    public Chessboard cloning() {
        Chessboard clone = new Chessboard(true);
        
        for (int i=0; i<Chessgame.SIZE; i++) {
            for (int j=0; j<Chessgame.SIZE; j++) {
                if (getPawnAt(i,j) == null) {
                    clone.setPawnAt(i, j, null);
                }
                else {
                    Pawn pawn = getPawnAt(i,j).cloning(clone);
                    clone.setPawnAt(i, j, pawn);
                }
            }
        }
        
        return clone;
    }
    
    
    public static boolean isOnChessboard(Point p) {
        return isOnChessboard((int)p.getX(), (int)p.getY());
    }
    
    public static boolean isOnChessboard(int x, int y) {
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7)
            return true;
        else
            return false;
    }
    
    public Pawn[][] getPawns() {
        return pawns;
    }
    
    public Pawn getPawnAt(Point position) {
        return getPawnAt((int)position.getX(), (int)position.getY());
    }
        
    public Pawn getPawnAt(int x, int y) {
        if (!isOnChessboard(x, y))
            return null;
        
        return pawns[x][y];
    }
    
    
    public void setPawnAt(Point position, Pawn pawn) {
        setPawnAt((int)position.getX(), (int)position.getY(), pawn);
    }
        
    public void setPawnAt(int x, int y, Pawn pawn) {
        if (!isOnChessboard(x, y))
            return;
        
        pawns[x][y] = pawn;
    }

    
    public LinkedList<Pawn> getPawnsFrom(String owner) {
        LinkedList<Pawn> list = new LinkedList<>();
        
        for (int i=0; i<Chessgame.SIZE; i++)
            for (int j=0; j<Chessgame.SIZE; j++)
                if (pawns[i][j] != null && pawns[i][j].ownedBy().equals(owner))
                    list.add(pawns[i][j]);
                
        return list;
    }
    
    public void updatePossibleMoves() {
        for (Pawn[] p1 : pawns)
            for (Pawn p2 : p1)
                if (p2 != null)
                    p2.updatePossibleMoves();
    }
    
    
    
    
    
    // Check whether there is a pawn between p1 and p2 (excluded)
    // We assume the line formed by p1 and p2 is either a diagonal line, an horizontal line or a vertical line 
    public boolean noPawnsBetween(Point p1, Point p2) {
        
        // If there is no square between p1 and p2 there is obviously no pawn between them
        if (Math.abs(p1.getX()-p2.getX()) + Math.abs(p1.getY()-p2.getY()) <= 1)
            return true;
        
        // Vertical line
        if (p1.getX() == p2.getX()) {
            for (int y=(int)Math.min(p1.getY(), p2.getY())+1; y<(int)Math.max(p1.getY(), p2.getY()); y++) {
                if (getPawnAt((int)p1.getX(), y) != null) {
                    return false;
                }
            }
        }
        // Horizontal line
        else if (p1.getY() == p2.getY()) {
            for (int x=(int)Math.min(p1.getX(), p2.getX())+1; x<(int)Math.max(p1.getX(), p2.getX()); x++) {
                if (getPawnAt(x, (int)p1.getY()) != null) {
                    return false;
                }
            }
        }
        // Diagonal line from top-left to bottom-right
        else if (p1.getY()-p1.getX() == p2.getY()-p2.getX()) {
            int y;
            for (int x=(int)Math.min(p1.getX(), p2.getX())+1; x<(int)Math.max(p1.getX(), p2.getX()); x++) {
                
                y = (int)Math.min(p1.getY(), p2.getY())+x-(int)Math.min(p1.getX(), p2.getX());
                
                if (getPawnAt(x, y) != null) {
                    return false;
                }
                
            }
        }
        // Diagonal line from top-right to bottom-left
        else if (p1.getX()+p1.getY() == p2.getX()+p2.getY())  {
            int x;
            for (int y=(int)Math.min(p1.getY(), p2.getY())+1; y<(int)Math.max(p1.getY(), p2.getY()); y++) {
                
                x = (int)Math.max(p1.getX(), p2.getX())-(y-(int)Math.min(p1.getY(), p2.getY()));
                
                if (getPawnAt(x, y) != null) {
                    return false;
                }
                
            }
        }
        
        return true;
    }
    
    public boolean isInCheck(String player) {
        LinkedList<Pawn> pawnsList = getPawnsFrom(player);
        Point kingPosition = new Point();
        
        for (Pawn pawn : pawnsList)
            if (pawn.getType().equals("king"))
                kingPosition = pawn.getPosition();
        
        
        String opponent = (player.equals("computer")) ? "human" : "computer";
        pawnsList = getPawnsFrom(opponent);
        for (Pawn pawn : pawnsList)
            for (Point point : pawn.getPossibleMoves())
                if (point.equals(kingPosition))
                    return true;
   
        return false;
    }
    
    public boolean isInCheckmate(String player) {
        LinkedList<Pawn> pawnsList = getPawnsFrom(player);

        for (Pawn pawn : pawnsList) {
            LinkedList<Point> pointsList = pawn.getPossibleMoves();

            for (Point point : pointsList) {

                Chessboard childState = this.cloning();
                Pawn tempPawn = childState.getPawnAt(pawn.getPosition());
                tempPawn.moveTo(point);
                
                if (!childState.isInCheck(player))
                    return false;
            }
        }
        
        return true;
    }
}
