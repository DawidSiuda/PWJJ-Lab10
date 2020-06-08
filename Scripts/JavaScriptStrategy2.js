function generateAnotherMoveInTheGame(array, sizeX, sizeY, sign) {
	while (true)
	{
		i = Math.floor(Math.random() * sizeX * sizeY); 
		if (array[i] == 0)
		{
			return parseInt(i);
		}
	}
  return 0;
}