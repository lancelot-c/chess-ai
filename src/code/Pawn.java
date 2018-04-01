/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Lancelot
 */
public class Pawn extends JLabel {
    
    private Chessboard chessboard;
    private String type;
    private String owner;
    private String opponent;
    private Point position;
    private int movesCount; // Needed for some special moves (e.g. En passant)
    private LinkedList<Point> possibleMoves;
    
    private boolean drag;
    private Point originalLocation;
    private Point clickLocation;
    
    
    public Pawn(Chessboard chessboard, String type, String owner, Point position) {
        this();
        
        this.chessboard = chessboard;
        this.owner = owner;
        this.opponent = getOpponent();
        setType(type);
        this.position = position;
        this.movesCount = 0;
        this.drag = false;
        this.possibleMoves = new LinkedList<>();
    }

    public Pawn(Pawn pawn) {
        this(pawn.getChessboard(), pawn.getType(), pawn.ownedBy(), pawn.getPosition());
        this.movesCount = pawn.getMovesCount();
    }
    
    public Pawn() {
        addMouseListener(new MouseAdapter()  
        {
            @Override
            public void mousePressed(MouseEvent e)  
            {  
                drag = true;
                originalLocation = getLocation(); // Relative to the window
                clickLocation = e.getPoint(); // Relative to the object location
            }
            
            @Override
            public void mouseReleased(MouseEvent e)  
            {  
                drag = false;
                
                
                // We want to know the position of the center of the pawn
                int halfWidth = getWidth()/2;
                int x = e.getComponent().getX()+halfWidth;
                int y = e.getComponent().getY()+halfWidth;
                
                Point pawnPosition = convertPosition(x, y);
                
                // If it's the player's turn
                // and the pawn is owned by the player
                // and was drop on the chessboard
                // and the move to this position is possible
                // --> Then we can move the pawn
                
                  
                if (Chessgame.currentPlayer.equals("human") && ownedBy().equals("human") && Chessboard.isOnChessboard(pawnPosition) && possibleMoves.contains(pawnPosition)) {
                    if (moveTo(pawnPosition) == 1) {
                        Chessgame.refresh();
                        Chessgame.computerTurn();
                    }
                    else {
                        setLocation(originalLocation); 
                    }
                }
                else {
                    setLocation(originalLocation);      
                }
            }  
        });
        
        addMouseMotionListener(new MouseAdapter()  
        {
            @Override
            public void mouseDragged(MouseEvent e)  
            {  
                setLocation(e.getComponent().getX()+e.getX()-(int)clickLocation.getX(),
                            e.getComponent().getY()+e.getY()-(int)clickLocation.getY()); 
            }  
        });
    }
    
    //@Override
    public Pawn cloning(Chessboard c) {
        Pawn clone = new Pawn();
        
        clone.setChessboard(c);
        clone.setOwner(owner);
        clone.setOpponent(getOpponent());
        clone.setType(type);
        clone.setPosition(position);
        clone.clonePossibleMoves(getPossibleMoves());
        clone.setMovesCount(getMovesCount());
        
        return clone;
    }
    
    public int getMovesCount() {
        return movesCount;
    }
    
    public void setMovesCount(int c) {
        movesCount = c;
    }
    
    public void clonePossibleMoves(LinkedList<Point> list) {
        possibleMoves = new LinkedList();
        
        for (Point p : list)
            possibleMoves.add(new Point(p));
    }
    
    public LinkedList<Point> getPossibleMoves() {
        return possibleMoves;
    }
    
    public int getPossibleMovesCount() {
        return possibleMoves.size();
    }
    
    public Chessboard getChessboard() {
        return chessboard;
    }
    
    public void setChessboard(Chessboard c) {
        chessboard = c;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String t) {
        type = t;
        setIcon(new ImageIcon(getClass().getResource("/images/"+owner+"_"+type+".png")));
    }
    
    public String ownedBy() {
        return owner;
    }
    
    public void setOwner(String o) {
        owner = o;
    }
    
    public String getOpponent() {
        if (owner == "human")
            return "computer";
        else
            return "human";
    }
    
    public void setOpponent(String o) {
        opponent = o;
    }
    
    public Point getPosition() {
        return position;
    }
    
    public void setPosition(Point p) {
        position = p;
    }
    
    public Point convertPosition(int x, int y) {
        x = (int)Math.floor(((double)x-15)/(570/8));
        y = (int)Math.floor(((double)y-15)/(570/8));
        
        return new Point(x, y);
    }
    

        
    
    public void addMoveIfPossible(Point to) {
        // 1. Cannot do a null move
        // 2. Cannot move outside the chessboard
        // 3. The path has to be clear
        // 4. The player can only move a pawn to a null square or to one of his opponent pawn
        
        if (!to.equals(position) && Chessboard.isOnChessboard(to) && chessboard.noPawnsBetween(position, to) && (chessboard.getPawnAt(to) == null || chessboard.getPawnAt(to).ownedBy().equals(opponent))) {
            possibleMoves.add(to);
        }
    }
    
    public void updatePossibleMoves() {
        possibleMoves.clear();
        
        int currentX = (int)position.getX();
        int currentY = (int)position.getY();
        Pawn p;
        
        if (type.equals("pawn")) {
            int OneStepForward = (owner.equals("human")) ? -1 : 1;
            int TwoStepsForward = (owner.equals("human")) ? -2 : 2;
            
            // Normal move
            p = chessboard.getPawnAt(new Point(currentX, currentY+OneStepForward));
            if (p == null)
                addMoveIfPossible(new Point(currentX, currentY+OneStepForward));

            // First move
            p = chessboard.getPawnAt(new Point(currentX, currentY+TwoStepsForward));
            if (p == null && movesCount == 0)
                addMoveIfPossible(new Point(currentX, currentY+TwoStepsForward));

            // Capture opponent pawn
            p = chessboard.getPawnAt(new Point(currentX-1, currentY+OneStepForward));
            if (p != null && p.ownedBy().equals(opponent))
                addMoveIfPossible(p.getPosition());

            p = chessboard.getPawnAt(new Point(currentX+1, currentY+OneStepForward));
            if (p != null && p.ownedBy().equals(opponent))
                addMoveIfPossible(p.getPosition());
            
            return; // Avoid checking the further conditions (rook, knight, ...)
        }
        
        if (type.equals("knight")) {
                addMoveIfPossible(new Point(currentX-2, currentY-1));
                addMoveIfPossible(new Point(currentX-1, currentY-2));
                
                addMoveIfPossible(new Point(currentX+1, currentY-2));
                addMoveIfPossible(new Point(currentX+2, currentY-1));
                
                addMoveIfPossible(new Point(currentX+2, currentY+1));
                addMoveIfPossible(new Point(currentX+1, currentY+2));
                
                addMoveIfPossible(new Point(currentX-1, currentY+2));
                addMoveIfPossible(new Point(currentX-2, currentY+1));
                
                return;
        }
        
        if (type.equals("rook") || type.equals("queen")) {
                for (int x=0; x<Chessgame.SIZE; x++)
                    addMoveIfPossible(new Point(x, currentY));
                
                for (int y=0; y<Chessgame.SIZE; y++)
                    addMoveIfPossible(new Point(currentX, y));
        }
                
        
        if (type.equals("bishop") || type.equals("queen")) {
                int startY = currentY-currentX;
                int startX = currentY+currentX;
                
                for (int y=startY; y<Chessgame.SIZE; y++)
                    addMoveIfPossible(new Point(y-startY, y));
                
                for (int x=startX; x>=0; x--)
                    addMoveIfPossible(new Point(x, startX-x));
        }
        
        if (type.equals("king")) {
                for (int x=-1; x<=1; x++)
                    for (int y=-1; y<=1; y++)
                        addMoveIfPossible(new Point(currentX+x, currentY+y));  
        }
    }
    
    
    public int moveTo(Point newPosition) {
        Chessboard clonedChessboard = chessboard.cloning(); //added for handling check
        
        
        Point oldPosition = new Point(position);
        position = newPosition;
        movesCount++;
        
        // Pawn promotion to queen
        if (type == "pawn" && ((owner.equals("human") && position.getY() == 0) || (owner.equals("computer") && position.getY() == 7))) {
            promote();
        }
        
        chessboard.setPawnAt(newPosition, new Pawn(chessboard.getPawnAt(oldPosition)));
        chessboard.setPawnAt(oldPosition, null);
        
        chessboard.updatePossibleMoves();
        
        if (chessboard.isInCheck(owner)) {  //added for handling check
            chessboard = clonedChessboard;  //added for handling check
            return 0;                         //added for handling check
        }
        
        return 1;
    }
    
    // Promote a pawn to a queen
    public void promote() {
        setType("queen");
    }
}
