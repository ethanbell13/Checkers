package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTests {
	private Game testGame = new Game();
	private Game resultGame = new Game();

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
		testGame.setPosition(1, 7, Status.Red);
		testGame.movePiece(1, 7, 0, 6);
		resultGame.setPosition(0, 6, Status.Red);
		assertArrayEquals(resultGame.getPositions(), testGame.getPositions());
	}
	@Test
	void testMovePieceWithKingResult() 
	{
		testGame.setPosition(0, 1, Status.Red);
		testGame.setPosition(0, 6, Status.Black);
		testGame.movePiece(0, 1, 0, 0);
		testGame.movePiece(0, 6, 0, 7);
		resultGame.setPosition(0, 0, Status.RedKing);
		resultGame.setPosition(0, 7, Status.BlackKing);
		assertArrayEquals(resultGame.getPositions(), testGame.getPositions());
	}
	@Test
	void testRemovePieceWithRedPieceAndBlackPiece() 
	{
		testGame.setPosition(0, 0, Status.Red);
		testGame.setRedCount(1);
		testGame.removePiece(0, 0);
		testGame.setPosition(0, 1, Status.Black);
		testGame.setBlackCount(1);
		testGame.removePiece(0, 1);
		boolean result = resultGame.equals(testGame);
		assertEquals(result, true);
	}
	@Test
	void testScanForMovesWithJumpsBlacksTurn() 
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
	@Test void testScanForMovesWithJumpsRedsTurn() 
	{
		testGame.setPosition(1, 0, Status.RedKing);
		testGame.setPosition(1, 2, Status.Red);
		testGame.setPosition(1, 1, Status.Black);
		testGame.setPosition(2, 1, Status.Black);
		testGame.setTurn(Team.Red);
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
	void testScanForMovesRedsTurn() 
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
	@Test
	void testScanForMovesBlacksTurn() 
	{
		testGame.setPosition(0, 6, Status.BlackKing);
		testGame.setTurn(Team.Black);
		ArrayList<ArrayList<Integer>> testMoves = new ArrayList<>();
		testMoves = testGame.scanForMoves();
		ArrayList<ArrayList<Integer>> resultMoves = new ArrayList<>();
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 0, 5)));
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 1, 5)));
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 0, 7)));
		resultMoves.add(new ArrayList<>(Arrays.asList(0, 6, 1, 7)));
		assertEquals(resultMoves, testMoves);
	}
	@Test
	void testScanForMovesWithEdgeCasesWithRed() 
	{
		testGame.setPosition(0, 1, Status.Red);
		testGame.setPosition(3, 2, Status.Red);
		testGame.setPosition(1, 1, Status.Red);
		testGame.setPosition(3, 6, Status.RedKing);
		testGame.setPosition(0, 5, Status.RedKing);
		testGame.setPosition(2, 6, Status.RedKing);
		testGame.setPosition(0, 0, Status.Black);
		testGame.setPosition(3, 7, Status.Black);
		testGame.setTurn(Team.Red);
		ArrayList<ArrayList<Integer>> testResult;
		testResult = testGame.scanForMoves();
		ArrayList<ArrayList<Integer>> expectedResult = new ArrayList<>();
		expectedResult.add(new ArrayList<>(Arrays.asList(0, 5, 0, 4)));
		expectedResult.add(new ArrayList<>(Arrays.asList(0, 5, 0, 6)));
		expectedResult.add(new ArrayList<>(Arrays.asList(1, 1, 1, 0)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 6, 2, 5)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 6, 3, 5)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 6, 2, 7)));
		expectedResult.add(new ArrayList<>(Arrays.asList(3, 2, 3, 1)));
		expectedResult.add(new ArrayList<>(Arrays.asList(3, 6, 3, 5)));
		assertEquals(expectedResult, testResult);
	}
	@Test
	void testScanForMovesWithEdgeCasesBlackTurn()
	{
		testGame.setPosition(2, 6, Status.Black);
		testGame.setPosition(3, 6, Status.Black);
		testGame.setPosition(0, 5, Status.Black);
		testGame.setPosition(0, 1, Status.BlackKing);
		testGame.setPosition(1, 1, Status.BlackKing);
		testGame.setPosition(3, 2, Status.Black);
		testGame.setPosition(0, 0, Status.Red);
		testGame.setPosition(3, 7, Status.Red);
		testGame.setTurn(Team.Black);
		ArrayList<ArrayList<Integer>> testResult;
		testResult = testGame.scanForMoves();
		ArrayList<ArrayList<Integer>> expectedResult = new ArrayList<>();
		expectedResult.add(new ArrayList<>(Arrays.asList(0, 1, 0 , 2)));
		expectedResult.add(new ArrayList<>(Arrays.asList(0, 5, 0, 6)));
		expectedResult.add(new ArrayList<>(Arrays.asList(1, 1, 1, 0)));
		expectedResult.add(new ArrayList<>(Arrays.asList(1, 1, 0, 2)));
		expectedResult.add(new ArrayList<>(Arrays.asList(1, 1, 1, 2)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 6, 2, 7)));
		expectedResult.add(new ArrayList<>(Arrays.asList(3, 2, 3, 3)));

		assertEquals(expectedResult, testResult);
	}
	@Test 
    void testScanPieceForJumps()
	{
		testGame.setPosition(2, 4, Status.BlackKing);
		testGame.setPosition(2, 3, Status.Red);
		testGame.setPosition(3, 3, Status.Red);
		testGame.setPosition(3, 5, Status.Red);
		testGame.setPosition(2, 5, Status.Red);
		testGame.setTurn(Team.Black);
		ArrayList<ArrayList<Integer>> testResult = testGame.scanPieceForJumps(2, 4);
		ArrayList<ArrayList<Integer>> expectedResult = new ArrayList<>();
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 4, 2, 3, 1, 2)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 4, 3, 3, 3, 2)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 4, 2, 5, 1, 6)));
		expectedResult.add(new ArrayList<>(Arrays.asList(2, 4, 3, 5, 3, 6)));
		assertEquals(expectedResult, testResult);
	}
	@Test
	void testScanPieceForJumpsWithNoMoreJumps()
	{
		testGame.setPosition(2, 4, Status.RedKing);
		testGame.setTurn(Team.Red);
		ArrayList<ArrayList<Integer>> testResult = testGame.scanPieceForJumps(2, 4);
		ArrayList<ArrayList<Integer>> expectedResult = new ArrayList<>();
		expectedResult.add(new ArrayList<>(Arrays.asList(0)));
		assertEquals(expectedResult, testResult);
		
	}

}
