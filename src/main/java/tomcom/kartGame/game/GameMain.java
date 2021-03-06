package tomcom.kartGame.game;

import tomcom.kartGame.game.resources.AssetManagerHolder;
import tomcom.kartGame.scenes.menus.HostScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameMain extends Game {

	private AssetManagerHolder assetManagerHolder;

	@Override
	public void create() {
		assetManagerHolder = new AssetManagerHolder();
		setScreen(new HostScreen(this));
	}

	public void switchScreen(Screen screen) {
		Screen oldScreen = getScreen();
		setScreen(screen);
		oldScreen.dispose();
	}

	@Override
	public void dispose() {
		assetManagerHolder.dispose();
	}

	public Texture getTexture(String filePath) {
		return assetManagerHolder.getTexture(filePath);
	}

	public Skin getSkin() {
		return assetManagerHolder.getSkin();
	}

}
