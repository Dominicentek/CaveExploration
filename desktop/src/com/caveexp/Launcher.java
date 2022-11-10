package com.caveexp;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl3.*;

public class Launcher {
	public static void main(String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Cave Exploration");
		config.setWindowedMode(Main.windowWidth, Main.windowHeight);
		//config.setWindowPosition((Main.windowWidth * 4 / 3) / 2 - Main.windowWidth / 2, (Main.windowHeight * 4 / 3) / 2 - Main.windowHeight / 2);
		config.setWindowedMode(1280, 720);
		config.setResizable(true);
		config.setWindowIcon(Files.FileType.Internal, "assets/icon.png");
		new Lwjgl3Application(new Main(), config);
	}
}
