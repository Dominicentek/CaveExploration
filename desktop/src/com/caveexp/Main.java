package com.caveexp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.caveexp.assets.Loader;
import com.caveexp.game.Game;
import com.caveexp.game.SoundEvent;
import com.caveexp.game.achievements.Achievement;
import com.caveexp.game.achievements.AchievementToast;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.game.item.Item;
import com.caveexp.game.ores.Ore;
import com.caveexp.game.recipes.Recipe;
import com.caveexp.game.region.TileItem;
import com.caveexp.gui.component.GUIHoverTextComponent;
import com.caveexp.gui.font.Font;
import com.caveexp.gui.screens.CloseableScreen;
import com.caveexp.gui.screens.Screen;
import com.caveexp.gui.screens.TitleScreen;
import com.caveexp.gui.component.GUIComponent;
import com.caveexp.gui.masking.Mask;
import com.caveexp.util.*;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Main extends ApplicationAdapter {
	public static Renderer renderer;
	public static OrthographicCamera camera;
	public static FrameBuffer maskBuffer;
	public static ScreenStack screenStack = new ScreenStack();
	public static int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width * 3 / 4;
	public static int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height * 3 / 4;
	public static Mask mask;
	public static ArrayList<CheatCode> cheatCodes = new ArrayList<>();
	public static String hoverMessage = "";
	public static Queue<AchievementToast> toastQueue = new Queue<>();
	public static int prevWidth = 0;
	public static int prevHeight = 0;
	public void create() {
		renderer = new Renderer();
		camera = new OrthographicCamera(windowWidth, windowHeight);
		camera.position.set(windowWidth / 2f, windowHeight / 2f, 0);
		mask = new Mask();
		Loader.register();
		Loader.load();
		Font.loadFromBinaryData(Gdx.files.internal("assets/font.fnt").readBytes());
		SoundEvent.register();
		Item.register();
		Ore.register();
		Recipe.register();
		Achievement.register();
		screenStack.push(new TitleScreen());
		Input.parse();
		Gdx.input.setInputProcessor(new InputProcessor() {
			public boolean keyDown(int keycode) {
				Main.screenStack.peek().keyPress(keycode);
				return true;
			}
			public boolean keyUp(int keycode) { return true; }
			public boolean keyTyped(char character) {
				Main.screenStack.peek().key(character);
				for (CheatCode code : cheatCodes) {
					code.inputChar(Character.toLowerCase(character));
				}
				return true;
			}
			public boolean touchDown(int screenX, int screenY, int pointer, int button) { return true; }
			public boolean touchUp(int screenX, int screenY, int pointer, int button) { return true; }
			public boolean touchDragged(int screenX, int screenY, int pointer) { return true; }
			public boolean mouseMoved(int screenX, int screenY) { return true; }
			public boolean scrolled(float amountX, float amountY) {
				Input.scroll = (int)amountY;
				return true;
			}
		});
		cheatCodes.add(new CheatCode("croc", () -> {
			Game.grantAchievement(Registry.ACHIEVEMENTS.get("croc_pickaxe"));
			if (Game.inventory.hasEnough(new InventoryItem("pickaxe_croc"))) return;
			Game.inventory.addItem(new InventoryItem("pickaxe_croc"));
		}));
		cheatCodes.add(new CheatCode("sandbox", () -> {
			Game.sandboxMode = !Game.sandboxMode;
		}));
		cheatCodes.add(new CheatCode("noclip", () -> {
			Game.noCollision = !Game.noCollision;
		}));
		cheatCodes.add(new CheatCode("potion", () -> {
			Game.inventory.addItem(new InventoryItem("potion", 100));
		}));
		cheatCodes.add(new CheatCode("spider", () -> {
			Game.currentRegion.tiles[(int)Game.currentRegion.playerX][(int)Game.currentRegion.playerY].item = TileItem.SPIDER;
		}));
		cheatCodes.add(new CheatCode("gameover", () -> {
			Game.health = 0;
		}));
		cheatCodes.add(new CheatCode("can i get an achievement pls", () -> {
			Game.grantAchievement(Registry.ACHIEVEMENTS.get("how_did_you_find_this"));
		}));
	}
	public void render() {
		windowWidth = Gdx.graphics.getWidth();
		windowHeight = Gdx.graphics.getHeight();
		camera.viewportWidth = windowWidth;
		camera.viewportHeight = windowHeight;
		camera.position.set(windowWidth / 2f, windowHeight / 2f, 0);
		camera.update();
		renderer.setProjectionMatrix(camera.combined);
		mask.setProjectionMatrix(camera.combined);
		update();
		renderGame();
	}
	public static void renderGame() {
		int clearColor = screenStack.peek().getBackgroundColor();
		ScreenUtils.clear(((clearColor >> 24) & 0xFF) / 255f, ((clearColor >> 16) & 0xFF) / 255f, ((clearColor >> 8) & 0xFF) / 255f, (clearColor & 0xFF) / 255f);
		maskBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, windowWidth, windowHeight, false);
		maskBuffer.bind();
		mask.render();
		maskBuffer.end();
		renderer.begin();
		for (Screen screen : screenStack) {
			screen.render(renderer);
			renderer.setColor(0xFFFFFFFF);
			for (GUIComponent component : screen.getComponents()) {
				component.render(renderer);
			}
		}
		for (Screen screen : screenStack) {
			for (GUIComponent component : screen.getComponents()) {
				if (component instanceof GUIHoverTextComponent) {
					GUIHoverTextComponent hoverText = (GUIHoverTextComponent)component;
					if (!hoverText.isInBounds()) continue;
					String[] lines = hoverText.hoverText.split("\n");
					int width = 0;
					for (String line : lines) {
						width = Math.max(Font.stringWidth(line), width);
					}
					int height = lines.length * Font.getHeight();
					renderer.setColor(0x0000007F);
					renderer.rectfill(Input.mouseX + 8, Input.mouseY + 8, width + 16, height + 16);
					renderer.setColor(0xFFFFFFFF);
					for (int i = 0; i < lines.length; i++) {
						Font.render(renderer, Input.mouseX + 16, Input.mouseY + 16 + i * Font.getHeight(), lines[i]);
					}
				}
			}
		}
		if (!hoverMessage.isEmpty()) {
			String[] lines = hoverMessage.split("\n");
			int width = 0;
			for (String line : lines) {
				width = Math.max(Font.stringWidth(line), width);
			}
			int height = lines.length * Font.getHeight();
			renderer.setColor(0x0000007F);
			renderer.rectfill(Input.mouseX + 8, Input.mouseY + 8, width + 16, height + 16);
			renderer.setColor(0xFFFFFFFF);
			for (int i = 0; i < lines.length; i++) {
				Font.render(renderer, Input.mouseX + 16, Input.mouseY + 16 + i * Font.getHeight(), lines[i]);
			}
		}
		for (AchievementToast toast : toastQueue) {
			String title = "$b$n$c000000You got an achievement!";
			int width = 10 + Math.max(Font.stringWidth(title), Font.stringWidth(toast.name));
			int x;
			if (toast.timeout < 50) {
				x = 1 - (int)((50 - toast.timeout) / 50.0 * (width + 1));
			}
			else if (toast.timeout > 250) {
				x = 1 - (int)((toast.timeout - 250) / 50.0 * (width + 1));
			}
			else {
				x = 1;
			}
			renderer.setColor(0xFFFFFF7F);
			int height = 10 + Font.getHeight() * 2;
			int y = Main.windowHeight - (height + 1) * (toast.yIndex + 1);
			renderer.rectfill(x, y, width, height);
			renderer.setColor(0x000000FF);
			renderer.rectdraw(x, y, width, height);
			renderer.setColor(0xFFFFFFFF);
			Font.render(renderer, x + 5, y + 5, title);
			Font.render(renderer, x + 5, y + 5 + Font.getHeight(), "$n$c000000" + toast.name);
		}
		String fps = "FPS: " + Gdx.graphics.getFramesPerSecond();
		Font.render(renderer, Main.windowWidth - Font.stringWidth(fps) - 4, Main.windowHeight - 1 - Font.getHeight(), fps);
		renderer.end();
		maskBuffer.dispose();
	}
	public static void update() {
		Input.mouseX = Gdx.input.getX();
		Input.mouseY = Gdx.input.getY();
		Input.isMousePressed = Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT);
		Input.mouseClicked = Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT);
		if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.LEFT)) screenStack.peek().buttonPress(0);
		if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.RIGHT)) screenStack.peek().buttonPress(1);
		if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.MIDDLE)) screenStack.peek().buttonPress(2);
		if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.BACK)) screenStack.peek().buttonPress(3);
		if (Gdx.input.isButtonJustPressed(com.badlogic.gdx.Input.Buttons.FORWARD)) screenStack.peek().buttonPress(4);
		if (Input.FULLSCREEN.isJustPressed()) {
			if (Gdx.graphics.isFullscreen()) {
				Gdx.graphics.setWindowedMode(prevWidth, prevHeight);
			}
			else {
				prevWidth = Gdx.graphics.getWidth();
				prevHeight = Gdx.graphics.getHeight();
				Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			}
		}
		for (Screen screen : screenStack) {
			for (GUIComponent component : screen.getComponents()) {
				component.updatePos();
			}
		}
		if (screenStack.peek() instanceof CloseableScreen && Input.CLOSE_MENU.isJustPressed()) {
			screenStack.pop();
		}
		ScreenStack screens = screenStack.copy();
		for (Screen screen : screens) {
			screen.update();
			for (GUIComponent component : screen.getComponents()) {
				component.update();
			}
		}
		for (CheatCode code : cheatCodes) {
			code.update();
		}
		Queue<AchievementToast> toastQueue = new Queue<>(Main.toastQueue);
		for (AchievementToast toast : toastQueue) {
			toast.timeout--;
			if (toast.timeout == 0) Main.toastQueue.pop();
		}
		Input.scroll = 0;
	}
	public static void renderMask() {
		renderer.draw(maskBuffer.getColorBufferTexture(), 0, 0);
	}
	public static void achievementToast(Achievement achievement) {
		int yIndex = 0;
		for (int i = 0; i < toastQueue.size(); i++) {
			if (toastQueue.get(i).yIndex == yIndex) {
				i = -1;
				yIndex++;
			}
		}
		toastQueue.push(new AchievementToast(300, achievement.name, yIndex));
	}
	public void dispose() {
		renderer.dispose();
		Loader.dispose();
		Font.disposeCache();
	}
}
