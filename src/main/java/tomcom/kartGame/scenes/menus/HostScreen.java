package tomcom.kartGame.scenes.menus;

import java.net.InetAddress;
import java.net.UnknownHostException;

import tomcom.kartGame.config.GameConfig;
import tomcom.kartGame.game.GameMain;
import tomcom.kartGame.game.resources.TexturePaths;
import tomcom.kartGame.scenes.TestLevel;
import tomcom.kartGame.systems.Network.ServerSystem;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

	// TODO: replace Font with single-instantiation font
	private BitmapFont font;

	private Stage stage;

	private Engine engine;
	private Sprite level1Preview;
	private Sprite level2Preview;
	private Sprite level3Preview;

	private int selectedLevel = 1;

	private String myIpAddress;

	public HostScreen(GameMain game) {
		stage = new Stage();
		engine = new Engine();
		engine.addSystem(new ServerSystem());
		font = new BitmapFont();
		font.setColor(Color.BLACK);

		try {
			myIpAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			myIpAddress = "Local Host IP Address could not be retrieved :( ";
		}

		level1Preview = new Sprite(game.getTexture(TexturePaths.MAP1));
		level2Preview = new Sprite(game.getTexture(TexturePaths.MAP2));
		level3Preview = new Sprite(game.getTexture(TexturePaths.MAP3));

		positionSprites();

		Skin mySkin = game.getSkin();

		Gdx.input.setInputProcessor(stage);

		initButtons(game, mySkin);

	}

	private void positionSprites() {
		level1Preview.setBounds(LEVEL_IMAGE_POS.x, LEVEL_IMAGE_POS.y,
				MAP_PREVIEW_IMAGE_SIZE, MAP_PREVIEW_IMAGE_SIZE);
		level2Preview.setBounds(LEVEL_IMAGE_POS.x, LEVEL_IMAGE_POS.y,
				MAP_PREVIEW_IMAGE_SIZE, MAP_PREVIEW_IMAGE_SIZE);
		level3Preview.setBounds(LEVEL_IMAGE_POS.x, LEVEL_IMAGE_POS.y,
				MAP_PREVIEW_IMAGE_SIZE, MAP_PREVIEW_IMAGE_SIZE);
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

	private void initButtons(GameMain game, Skin mySkin) {
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
					game.switchScreen(new TestLevel(game, engine));
					break;
				case 2:
					// game.switchScreen(new MenuScreen(game));
					break;
				case 3:
					// game.switchScreen(new MenuScreen(game));
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
				game.switchScreen(new MenuScreen(game));
			}
		});
		stage.addActor(back);
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		stage.act(delta);

		Gdx.gl.glClearColor(0.914f, 0.933f, 0.957f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.getBatch().begin();

		stage.getBatch().setProjectionMatrix(stage.getCamera().combined);

		drawMapPreviewPictures();

		font.setColor(Color.BLACK);
		font.draw(stage.getBatch(), "Your IP Address is: " + myIpAddress,
				OFFSET, OFFSET);

		stage.getBatch().end();

		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getCamera().update();
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
		System.out.println(" Host Screen disposed()");
		stage.dispose();
	}

}
