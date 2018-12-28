package game.modes;

import game.gui.GuiController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PvPGameMode extends XvX{

	public PvPGameMode(GuiController gui) {
		super(gui);
		setListener();
	}
	
	/**
	 * The circleNumber property from the GuiController is connected to this listener.
	 * Whenever a circle is clicked the method to make the move is called.
	 */
	private void setListener() {
		gui.getCircleNumberProperty().addListener(new ChangeListener<Object>(){
			@Override
			public void changed(ObservableValue<?> o, Object oldVal, Object newVal) {
		        if((int)newVal == -1)
		        	return; 
		        else
		        	move((int)newVal);
			}
		});
	}

	@Override
	protected int firstMoveIsDouble() {
		// TODO: Ask what square it belongs to and then return the cellNumber from the centre-cell.
		return 0;
	}
}




















