function generateAnotherMoveInTheGame(array, sizeX, sizeY, sign) {
	for (i = 0; i < array.length; i++)
	{
		if (array[i] == 0)
		{
			return parseInt(i);
		}
	}
  return 0;
}