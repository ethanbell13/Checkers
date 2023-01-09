package application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.util.Pair;

class GameTests {

	@BeforeAll
	static void setUpBeforeClass() throws Exception 
	{
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception 
	{
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testScanForMovesWithJumps() 
	{
		Game testGame = new Game();
		Piece blackPiece1 = new Piece(1, 0, 0, PieceStatus.Stone, Team.Black);
		Piece blackPiece2 = new Piece(1, 2, 1, PieceStatus.King, Team.Black);
		Piece redPiece1 = new Piece(1, 1, 0, PieceStatus.King, Team.Red);
		Piece redPiece2 = new Piece(2, 1, 1, PieceStatus.King, Team.Red);
		testGame.addPiece(blackPiece1);
		testGame.addPiece(blackPiece2);
		testGame.addPiece(redPiece1);
		testGame.addPiece(redPiece2);
//		testGame.addPiece(new Piece(1, 7, 2, PieceStatus.Stone, Team.Red));
//		testGame.addPiece(new Piece(0, 6, 2, PieceStatus.Stone, Team.Black));
//		testGame.addPiece(new Piece(1, 6, 3, PieceStatus.Stone, Team.Black));
		testGame.setTurn(Team.Black);
		ArrayList<Pair<Piece, ArrayList<Integer>>> testJumps = new ArrayList<>();
		testJumps = testGame.scanForMoves();
		ArrayList<Pair<Piece, ArrayList<Integer>>> expectedJumps = new ArrayList<>();
		expectedJumps.add(new Pair<>(blackPiece1, new ArrayList<>(Arrays.asList(1, 0, 1, 1, 0, 2))));
		expectedJumps.add(new Pair<>(blackPiece1, new ArrayList<>(Arrays.asList(1, 0, 2, 1, 2, 2))));
		expectedJumps.add(new Pair<>(blackPiece2, new ArrayList<>(Arrays.asList(1, 2, 1, 1, 0, 0))));
		expectedJumps.add(new Pair<>(blackPiece2, new ArrayList<>(Arrays.asList(1, 2, 2, 1, 2, 0))));
		assertEquals(expectedJumps, testJumps);
	}
	@Test
	void testScanForMovesWithMoves() 
	{
		Game testGame = new Game();
		Piece redPiece = new Piece(0, 6, 0, PieceStatus.King, Team.Red);
		testGame.addPiece(redPiece);
		testGame.setTurn(Team.Red);
		ArrayList<Pair<Piece, ArrayList<Integer>>> testMoves = new ArrayList<>();
		testMoves = testGame.scanForMoves();
		ArrayList<Pair<Piece, ArrayList<Integer>>> expectedMoves = new ArrayList<>();
		expectedMoves.add(new Pair<>(redPiece, new ArrayList<>(Arrays.asList(0, 6, 0, 5))));
		expectedMoves.add(new Pair<>(redPiece, new ArrayList<>(Arrays.asList(0, 6, 1, 5))));
		expectedMoves.add(new Pair<>(redPiece, new ArrayList<>(Arrays.asList(0, 6, 0, 7))));
		expectedMoves.add(new Pair<>(redPiece, new ArrayList<>(Arrays.asList(0, 6, 1, 7))));
		assertEquals(expectedMoves, testMoves);
	}

}
