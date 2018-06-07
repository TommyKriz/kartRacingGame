package tomcom.kartGame.game;

import tomcom.kartGame.config.GameConfig;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Kart Game Physics Spike";
		cfg.width = GameConfig.SCREEN_WIDTH;
		cfg.height = GameConfig.SCREEN_HEIGHT;
		cfg.resizable = true;
		new LwjglApplication(new GameMain(), cfg);
	}

}
