package tomcom.kartGame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import tomcom.kartGame.game.resources.AssetManagerHolder;
import tomcom.kartGame.scenes.menus.MenuScreen;
import tomcom.kartGame.systems.Network.ClientSystem;
import tomcom.kartGame.systems.Network.ServerSystem;

public class GameMain extends Game {

	private AssetManagerHolder assetManagerHolder;

	@Override
	public void create() {
		assetManagerHolder = new AssetManagerHolder();
		setScreen(new MenuScreen(this));
	}

	public void switchScreen(Screen screen) {
		Screen oldScreen = getScreen();
		setScreen(screen);
		oldScreen.dispose();
	}

	@Override
	public void dispose() {
		assetManagerHolder.dispose();
		ServerSystem.onDispose.dispatch(null);
		ClientSystem.onDispose.dispatch(null);
	}

	public Texture getTexture(String filePath) {
		return assetManagerHolder.getTexture(filePath);
	}

	public Skin getSkin() {
		return assetManagerHolder.getSkin();
	}

	public Music getBackgroundMusic() {
		return assetManagerHolder.getBackgroundMusic();
	}

}
