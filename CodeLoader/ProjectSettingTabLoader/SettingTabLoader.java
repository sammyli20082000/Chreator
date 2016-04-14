package Chreator.CodeLoader.ProjectSettingTabLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Chreator.CodeLoader.CodeLoader;
import Chreator.UIModule.ProjectSettingPanel.GameType;
import Chreator.UIModule.UIHandler;

public class SettingTabLoader {
	
	String type;
	File gameJava;

	public SettingTabLoader() {
		gameJava = new File(CodeLoader.baseDir+"\\src\\Executable\\Game.java");
	}
	
	public void loadToProjectSettingTab() throws FileNotFoundException, IOException {
		loadGameType();
		
		UIHandler uiHandler = UIHandler.getInstance(null);
		
		GameType type = null;
		switch (this.type) {
		case "ADD_TYPE":
			type = GameType.ADD_TYPE;
			break;
		case "MOVE_TYPE":
			type = GameType.MOVE_TYPE;
			break;
		case "ADD_MOVE_TYPE":
			type = GameType.ADD_MOVE_TYPE;
			break;
		default:
			type = GameType.ADD_MOVE_TYPE;
			break;
		}
		
		uiHandler.setGameType(type);
	}
	
	private void loadGameType() throws FileNotFoundException, IOException {
		if (!gameJava.exists()) {
			return;
		}
		
		try (BufferedReader br = new BufferedReader(new FileReader(gameJava))) {
			String line;
			String[] temp;
			while ((line = br.readLine()) != null) {
				if (line.contains("GameType:")) {
					temp = line.split(": ");
					type = temp[1];
					break;
				}
			}
		}
	}
}
