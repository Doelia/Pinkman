package edu.turtlekit3.warbot.gui.viewer;




import edu.turtlekit3.warbot.game.Game;
import edu.turtlekit3.warbot.gui.viewer.screens.WarViewerPauseScreen;
import edu.turtlekit3.warbot.gui.viewer.screens.WarViewerScreen;


public class WarViewerGdx extends com.badlogic.gdx.Game {

	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;/*(int) ((Toolkit.getDefaultToolkit()
			.getScreenSize().getHeight() / Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth()) * WIDTH);*/
	
	private WarViewerScreen screen;
	private WarViewerPauseScreen pauseScreen;
		
	private edu.turtlekit3.warbot.game.Game game;
	
	public WarViewerGdx() {
		game = Game.getInstance();
	}

	@Override
	public void create() {
		screen = new WarViewerScreen(this);
		pauseScreen = new WarViewerPauseScreen(this);
		setScreen(screen);
	}

	@Override
	public void dispose() {
		super.dispose();
		screen.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
	
	public edu.turtlekit3.warbot.game.Game getGame() {
		return game;
	}

	public WarViewerScreen getScreen() {
		return screen;
	}

	public WarViewerPauseScreen getPauseScreen() {
		return pauseScreen;
	}
}
