package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTests {
	private Game testGame = new Game();
	private Game resultGame = new Game();
//	private Status[][] testPositions;
//	private Status[][] resultPositions;
	

	@BeforeEach
	void setUp() throws Exception 
	{
		Game testGame = new Game();
		for(int x = 0; x < 4; x++) 
		{
			for(int y = 0; y < 8; y++)
			{
				testGame.setPosition(x, y, Status.Empty);
				resultGame.setPosition(x, y, Status.Empty);
			}
		}
	}

	@AfterEach
	void tearDown() throws Exception 
	{
	}
	@Test
	void testNewGame() 
	{
		testGame.NewGame();
		Status[][] testPositions = testGame.getPositions();
		Status[][] resultPositions = new Status[4][8];
		for(int x = 0; x < 4; x++) 
		{
			for(int y = 0; y < 8; y++) 
			{
				if(y < 3)
					resultPositions[x][y] = Status.Black;
				else if(y < 5)
					resultPositions[x][y] = Status.Empty;
				else
					resultPositions[x][y] = Status.Red;
			}
		}
		assertArrayEquals(resultPositions, testPositions);
	}
	@Test
	void testMovePiece() 
	{
		testGame.setPosition(1, 0, Status.Black);
		testGame.setPosition(1, 7, Status.Red);
		testGame.movePiece(1, 7, 0, 6, Status.Red);
		resultGame.setPosition(1, 0, Status.Black);
		resultGame.setPosition(0, 6, Status.Red);
		assertArrayEquals(resultGame.getPositions(), testGame.getPositions());
	}
	@Test
	void testRemovePiece() 
	{
		testGame.setPosition(0, 0, Status.Red);
		testGame.removePiece(0, 0, Status.Red);
		assertArrayEquals(resultGame.getPositions(), testGame.getPositions());
	}

	@Test
	void testScanForMovesWithJumps() 
	{
		testGame.setPosition(1, 0, Status.Black);
		testGame.setPosition(1, 2, Status.BlackKing);
		testGame.setPosition(1, 1, Status.RedKing);
		testGame.setPosition(2, 1, Status.RedKing);
		testGame.setTurn(Team.Black);
		ArrayList<ArrayList<Integer>> testJumps = new ArrayList<>();
		testJumps = testGame.scanForMoves();
		ArrayList<ArrayList<Integer>> resultJumps = new ArrayList<>();
		resultJumps.add(new ArrayList<>(Arrays.asList(1, 0, 1, 1, 0, 2)));
		resultJumps.add(new ArrayList<>(Arrays.asList(1, 0, 2, 1, 2, 2)));
		resultJumps.add(new ArrayList<>(Arrays.asList(1, 2, 1, 1, 0, 0)));
		resultJumps.add(new ArrayList<>(Arrays.asList(1, 2, 2, 1, 2, 0)));
		assertEquals(resultJumps, testJumps);
	}
	@Test
	void testScanForMovesWithMoves() 
	{
		testGame.setPosition(0, 6, Status.RedKing);
		testGame.setTurn(Team.Red);
		ArrayList<ArrayList<Integer>> testMoves = new ArrayList<>();
		testMoves = testGame.scanForMoves();
		ArrayList<ArrayList<Integer>> resultMoves = new ArrayList<>();
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 0, 5)));
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 1, 5)));
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 0, 7)));
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 1, 7)));
		assertEquals(resultMoves, testMoves);
	}

}
