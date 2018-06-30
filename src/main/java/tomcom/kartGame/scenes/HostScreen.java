package tomcom.kartGame.scenes;

import tomcom.kartGame.config.GameConfig;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.ResourceManager;
import tomcom.kartGame.game.resources.TextureKeys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * 
 * Choose between Maps and display IP-Address.
 * 
 * @author Tommy
 *
 */
public class HostScreen implements Screen {

	private static final int MAP_PREVIEW_IMAGE_SIZE = 350;

	private static final int SMALL_BUTTON_SIZE = 100;

	private static final int OFFSET = 30;

	private static final Vector2 LEVEL_IMAGE_POS = new Vector2(
			GameConfig.SCREEN_WIDTH / 2 - MAP_PREVIEW_IMAGE_SIZE / 2,
			GameConfig.SCREEN_HEIGHT / 2 - 2 * OFFSET);

	private Stage stage;

	private Sprite level1Preview = new Sprite(
			ResourceManager.getTexture(TextureKeys.MAP));
	private Sprite level2Preview = new Sprite(
			ResourceManager.getTexture(TextureKeys.LEVEL2_PREVIEW));
	private Sprite level3Preview = new Sprite(
			ResourceManager.getTexture(TextureKeys.LEVEL3_PREVIEW));

	private int selectedLevel = 1;

	public HostScreen(GameMain game) {
		stage = new Stage();

		Skin mySkin = ResourceManager.getSkin();

		Gdx.input.setInputProcessor(stage);

		TextButton lastLevel = new TextButton("<", mySkin);
		lastLevel.setBounds(LEVEL_IMAGE_POS.x - SMALL_BUTTON_SIZE - OFFSET,
				LEVEL_IMAGE_POS.y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE);
		lastLevel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedLevel - 1 >= 1) {
					selectedLevel--;
				}
			}
		});
		stage.addActor(lastLevel);

		TextButton nextLevel = new TextButton(">", mySkin);
		nextLevel.setBounds(
				LEVEL_IMAGE_POS.x + MAP_PREVIEW_IMAGE_SIZE + OFFSET,
				LEVEL_IMAGE_POS.y, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE);
		nextLevel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (selectedLevel + 1 <= 3) {
					selectedLevel++;
				}
			}
		});
		stage.addActor(nextLevel);

		TextButton play = new TextButton("PLAY", mySkin);
		play.setBounds(GameConfig.SCREEN_WIDTH / 2 - 150, LEVEL_IMAGE_POS.y
				- SMALL_BUTTON_SIZE - OFFSET, 300, SMALL_BUTTON_SIZE);
		play.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				switch (selectedLevel) {
				case 1:
					// TODO: LOAD IN HOST MODE
					game.setScreen(new TestLevel(game));
					break;
				case 2:
					// game.setScreen(new MenuScreen(game));
					break;
				case 3:
					// game.setScreen(new MenuScreen(game));
					break;
				}
			}
		});
		stage.addActor(play);

		TextButton back = new TextButton("Back", mySkin);
		back.setBounds(GameConfig.SCREEN_WIDTH - SMALL_BUTTON_SIZE - OFFSET,
				OFFSET, SMALL_BUTTON_SIZE, SMALL_BUTTON_SIZE);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});
		stage.addActor(back);

		initSprites();

	}

	private void initSprites() {
		level1Preview.setBounds(LEVEL_IMAGE_POS.x, LEVEL_IMAGE_POS.y,
				MAP_PREVIEW_IMAGE_SIZE, MAP_PREVIEW_IMAGE_SIZE);
		level2Preview.setBounds(LEVEL_IMAGE_POS.x, LEVEL_IMAGE_POS.y,
				MAP_PREVIEW_IMAGE_SIZE, MAP_PREVIEW_IMAGE_SIZE);
		level3Preview.setBounds(LEVEL_IMAGE_POS.x, LEVEL_IMAGE_POS.y,
				MAP_PREVIEW_IMAGE_SIZE, MAP_PREVIEW_IMAGE_SIZE);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		Gdx.gl.glClearColor(0, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.getBatch().begin();

		drawMapPreviewPictures();

		stage.getBatch().end();

		stage.draw();
	}

	private void drawMapPreviewPictures() {
		switch (selectedLevel) {
		case 1:
			level1Preview.draw(stage.getBatch());
			break;
		case 2:
			level2Preview.draw(stage.getBatch());
			break;
		case 3:
			level3Preview.draw(stage.getBatch());
			break;
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
