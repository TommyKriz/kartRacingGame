package tomcom.kartGame.game;

import tomcom.kartGame.scenes.HostScreen;
import tomcom.kartGame.scenes.SplashScreen;

import com.badlogic.gdx.Game;

public class GameMain extends Game {

	@Override
	public void create() {
		setScreen(new HostScreen(this));
	}

}
