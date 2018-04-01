/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Lancelot
 */
public class MinimaxAlgorithm {
    
    public final static int COMPUTER_LEVEL = 2;
    public final static int SEARCH_TREE_DEPTH = COMPUTER_LEVEL;
    public final static int PLUS_INFINITY = 2000000000;
    public final static int MINUS_INFINITY = -PLUS_INFINITY;
    
    public static HashMap<Integer, Chessboard> rootChildren = new HashMap<>();
    
    
    public static Chessboard AlphaBetaSearch(Chessboard state) {
        int v = MaxValue(state, MINUS_INFINITY, PLUS_INFINITY, 0);
        return rootChildren.get(v);
    }
    
    public static int MaxValue(Chessboard state, int alpha, int beta, int depth) {
        if (depth == SEARCH_TREE_DEPTH)
            return Utility(state);
        
        int v = MINUS_INFINITY;
        

        LinkedList<Pawn> pawnsList = state.getPawnsFrom("computer");

        
        for (Pawn pawn : pawnsList) {
            LinkedList<Point> pointsList = pawn.getPossibleMoves();

            for (Point point : pointsList) {

                Chessboard childState = state.cloning();
                Pawn tempPawn = childState.getPawnAt(pawn.getPosition());
                
                if (tempPawn.moveTo(point) == 0)
                    continue;
                
                int minValue = MinValue(childState, alpha, beta, depth+1);
                
                rootChildren.put(minValue, childState);
        
                v = Math.max(v, minValue);

                if (v >= beta)
                    return v;

                alpha = Math.max(alpha, v);
            }
        }
        
        return v;
    }
    
            
    public static int MinValue(Chessboard state, int alpha, int beta, int depth) {
        if (depth == SEARCH_TREE_DEPTH)
            return Utility(state);
        
        int v = PLUS_INFINITY;
        
        LinkedList<Pawn> pawnsList = state.getPawnsFrom("human");

        
        for (Pawn pawn : pawnsList) {
            LinkedList<Point> pointsList = pawn.getPossibleMoves();

            for (Point point : pointsList) {

                Chessboard childState = state.cloning();
                Pawn tempPawn = childState.getPawnAt(pawn.getPosition());
                
                if (tempPawn.moveTo(point) == 0)
                    continue;
                
                v = Math.min(v, MaxValue(childState, alpha, beta, depth+1));

                if (v <= alpha)
                    return v;

                beta = Math.min(beta, v);
            }
        }
        
        return v;
    }
        
        
    public static int Utility(Chessboard chessboard) {
        Pawn[][] pawns = chessboard.getPawns();
        Pawn pawn;
        int K=0, K2=0, Q=0, Q2=0, R=0, R2=0, B=0, B2=0, H=0, H2=0, P=0, P2=0;
        int D=0, D2=0, S=0, S2=0, I=0, I2=0;
        int M=0, M2=0;
        
        for (int x=0; x<Chessgame.SIZE; x++) {
            int tempD = 0, tempD2 = 0;
            
            for (int y=0; y<Chessgame.SIZE; y++) {
                pawn = pawns[x][y];
                
                if (pawn == null)
                    continue;
                
                if (pawn.ownedBy().equals("computer")) {
                    M += pawn.getPossibleMovesCount();
                    
                    if (pawn.getType().equals("king")) K++;
                    if (pawn.getType().equals("queen")) Q++;
                    if (pawn.getType().equals("rook")) R++;
                    if (pawn.getType().equals("bishop")) B++;
                    if (pawn.getType().equals("knight")) H++;
                    if (pawn.getType().equals("pawn")) {P++; tempD++;}
                }
                else {
                    M2 += pawn.getPossibleMovesCount();
                    
                    if (pawn.getType().equals("king")) K2++;
                    if (pawn.getType().equals("queen")) Q2++;
                    if (pawn.getType().equals("rook")) R2++;
                    if (pawn.getType().equals("bishop")) B2++;
                    if (pawn.getType().equals("knight")) H2++;
                    if (pawn.getType().equals("pawn")) {P2++; tempD2++;}
                }
            }
            
            if (tempD > 1)
                D += tempD;
            
            if (tempD2 > 1)
                D2 += tempD2;
        }
        
        int f = 200*(K-K2) + 9*(Q-Q2) + 5*(R-R2) + 3*(B-B2) + 3*(H-H2) + (P-P2);
        f -= 0.5*(D-D2+S-S2+I-I2);
        f += 0.1*(M-M2);
        return f;
    }
}
