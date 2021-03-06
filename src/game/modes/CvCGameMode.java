package game.modes;

import game.logic.Board;
import game.logic.Logic;
import game.logic.Player;
import game.logic.Position;

import java.util.ArrayList;

/**
 * Computer vs computer gamemode with a basic minimax algorithm.
 * (This class is very messy atm, I am aware of this.)
 * @author Bram Pulles
 */
public class CvCGameMode extends XvX {

	public CvCGameMode(){
		super();
		play();
	}

	/**
	 * Play a game.
	 */
	private void play(){
		System.out.println("NEW GAME:");

		while(!Logic.gameIsOver(board, onTurn)){
			move(findBestMove());
			System.out.println(board);
		}
		System.out.println("PLAYER: " + Logic.winner(board, onTurn) + " WON!");
	}

	/**
	 * @return the best available move.
	 */
	private int findBestMove(){
		int bestMove = -1;
		int bestMoveValue = (isMaximizingPlayer(onTurn))? Integer.MIN_VALUE : Integer.MAX_VALUE;

		for(int i = 0; i < board.size(); i++){
			if (Logic.validMove(board, onTurn, i, lastCellNumber, nextPos)) {
				Player onTurnSave = onTurn;
				Position nextPosSave = nextPos;
				int lastCellNumberSave = lastCellNumber;
				Player lastOccupy = board.getCell(i).getOccupy();
				move(i);

				int value = minimax(0);
				if ((isMaximizingPlayer(onTurnSave) && value > bestMoveValue) ||
						(!isMaximizingPlayer(onTurnSave) && value < bestMoveValue)) {
					bestMove = i;
					bestMoveValue = value;
				}

				nextPos = nextPosSave;
				board.getCell(i).setOccupy(lastOccupy);
				lastCellNumber = lastCellNumberSave;
				changeTurn();
			}
		}

		if(bestMove == -1){ // If there is no good move (i.e. guaranteed loss) then take a random move.
			for(int i = 0; i < board.size(); i++){
				if(Logic.validMove(board, onTurn, i, lastCellNumber, nextPos)){
					bestMove = i;
				}
			}
		}
		return bestMove;
	}

	/**
	 * @return the heuristic value of the current board.
	 */
	private int heuristic(){
		if(Logic.gameIsOver(board, onTurn)){
			return (Logic.winner(board, onTurn) == Player.RED)? Integer.MAX_VALUE : Integer.MIN_VALUE;
		}

		int value = 0;
		for(int i = 0; i < board.size(); i++){
			if(board.getCell(i).getOccupy() == Player.RED){
				value++;
			}else if(board.getCell(i).getOccupy() == Player.YELLOW){
				value--;
			}
		}
		return value;
	}

	/**
	 * A basic minimax algortihm.
	 * @param depth
	 * @return the value of the current board.
	 */
	private int minimax(int depth){
		if(Logic.gameIsOver(board, onTurn) || depth >= 8){
			return heuristic();
		}

		if(isMaximizingPlayer(onTurn)){
			int bestValue = Integer.MIN_VALUE;
			for(int i : availableMoves()){
				Position nextPosSave = nextPos;
				int lastCellNumberSave = lastCellNumber;
				Player lastOccupy = board.getCell(i).getOccupy();
				move(i);

				int value = minimax(depth+1);
				bestValue = Integer.max(bestValue, value);

				nextPos = nextPosSave;
				board.getCell(i).setOccupy(lastOccupy);
				lastCellNumber = lastCellNumberSave;
				changeTurn();
			}
			return bestValue;
		}else{
			int bestValue = Integer.MAX_VALUE;
			for(int i : availableMoves()){
				Position nextPosSave = nextPos;
				int lastCellNumberSave = lastCellNumber;
				Player lastOccupy = board.getCell(i).getOccupy();
				move(i);

				int value = minimax(depth+1);
				bestValue = Integer.min(bestValue, value);

				nextPos = nextPosSave;
				board.getCell(i).setOccupy(lastOccupy);
				lastCellNumber = lastCellNumberSave;
				changeTurn();
			}
			return bestValue;
		}
	}

	/**
	 * @return all the available moves.
	 */
	private ArrayList<Integer> availableMoves(){
		ArrayList<Integer> moves = new ArrayList<>();
		for(int i = 0; i < board.size(); i++) {
			if (Logic.validMove(board, onTurn, i, lastCellNumber, nextPos)) {
				moves.add(i);
			}
		}
		return moves;
	}

	/**
	 * @param player
	 * @return if the player is the maximizing player.
	 */
	private boolean isMaximizingPlayer(Player player){
		return player == Player.RED;
	}

	@Override
	protected Position firstMoveIsDouble(int cellNumber) {
		// TODO: Do no always choose the centre square.
		return Logic.getPos(cellNumber, Position.CENTER);
	}

	@Override
	protected void gameOver() {
	}
}
