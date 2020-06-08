package lab10;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JavaScriptStrategy {

	private String nameString;
	private ScriptEngineManager mgr;
	private ScriptEngine jsEngine;
	private Invocable inv;

	public JavaScriptStrategy(String path) {
		try {

			mgr = new ScriptEngineManager();
			jsEngine = mgr.getEngineByName("Nashorn");

			jsEngine.eval(Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8));
			inv = (Invocable) jsEngine;

			nameString = Paths.get(path).getFileName().toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Coordinates getMove(RectangleWrapper gameBoard[][], int sizeX, int sizeY, int sign) {
		try {
			int[] array = new int[sizeX * sizeY];

			for (int x = 0; x < sizeX; x++) {
				for (int y = 0; y < sizeY; y++) {
					switch (gameBoard[x][y].getCurrentSign()) {
					case O:
						array[y * sizeY + x] = 1;
						break;
					case X:
						array[y * sizeY + x] = 2;
						break;
					case NONE:
						array[y * sizeY + x] = 0;
						break;
					}
				}
			}

			Double res = (Double) inv.invokeFunction("generateAnotherMoveInTheGame", array, sizeX, sizeY, sign);

			return new Coordinates(res.intValue()%sizeY, res.intValue()/sizeY);

		} catch (Exception e) {
			System.out.print("ERROR: cannot call JS function.");
			e.printStackTrace();
			e.getMessage();
		}

		return new Coordinates(0, 0);
	}

	public String toString() {
		return nameString;
	}
}
