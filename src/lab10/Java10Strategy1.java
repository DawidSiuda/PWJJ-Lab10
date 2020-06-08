package lab10;

public class Java10Strategy1 {
	static {
		System.loadLibrary("Java10Strategy1");
	}

	public static native int generateAnotherMoveInTheGame(int gameBoardint[], int sizeX, int sizeY, int sign);

	public static Coordinates getMove(RectangleWrapper gameBoard[][], int sizeX, int sizeY, int sign)
	{
		int[] array = new int [sizeX * sizeY];

		for(int x = 0; x < sizeX; x++) {
			for(int y = 0; y < sizeY; y++) {
				switch(gameBoard[x][y].getCurrentSign())
				{
				case O:
					array[y*sizeY+x] = 1;
					break;
				case X:
					array[y*sizeY+x] = 2;
					break;
				case NONE:
					array[y*sizeY+x] = 0;
					break;
				}
			}
		}

		int out = generateAnotherMoveInTheGame(array, sizeX, sizeY,1);
		return new Coordinates(out%sizeY, out/sizeY);
	}
}
