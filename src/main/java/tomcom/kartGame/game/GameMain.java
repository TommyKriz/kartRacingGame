package tomcom.kartGame.game;

import tomcom.kartGame.scenes.TestLevel;

import com.badlogic.gdx.Game;

public class GameMain extends Game {

	@Override
	public void create() {
		setScreen(new TestLevel(this));
	}

}
