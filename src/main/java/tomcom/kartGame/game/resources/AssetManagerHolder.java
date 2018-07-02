package tomcom.kartGame.game.resources;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetManagerHolder {

	private static final String SKIN_FILEPATH = "skin/uiskin.json";

	private static final String BACKGROUND_MUSIC_FILEPATH = "audio/M43.ogg";

	private AssetManager manager;

	public AssetManagerHolder() {
		manager = new AssetManager();
		loadSkin();
		loadBackgroundMusic();
		loadTextures();
		manager.finishLoading();
	}

	private void loadBackgroundMusic() {
		manager.load(BACKGROUND_MUSIC_FILEPATH, Music.class);
	}

	public Music getBackgroundMusic() {
		return manager.get(BACKGROUND_MUSIC_FILEPATH, Music.class);
	}

	private void loadSkin() {
		manager.load(SKIN_FILEPATH, Skin.class);
	}

	public Skin getSkin() {
		return manager.get(SKIN_FILEPATH, Skin.class);
	}

	private void loadTextures() {
		loadTexture(TexturePaths.MAIN_MENU_BG);
		loadTexture(TexturePaths.KART_RED);
		loadTexture(TexturePaths.KART_GRAY);
		loadTexture(TexturePaths.KART_SILVER);
		loadTexture(TexturePaths.KART_BLUE);
		loadTexture(TexturePaths.WHEEL);
		loadTexture(TexturePaths.ROADBLOCK);
		loadTexture(TexturePaths.MAP1);
		loadTexture(TexturePaths.MAP2);
		loadTexture(TexturePaths.MAP3);
		loadTexture(TexturePaths.LAMBO);
	}

	public Texture getTexture(String filePath) {
		return manager.get(filePath, Texture.class);
	}

	private void loadTexture(String filePath) {
		manager.load(filePath, Texture.class);
	}

	public void dispose() {
		manager.clear();
	}

}
