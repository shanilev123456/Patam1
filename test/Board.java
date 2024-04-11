
 package test;
import java.util.ArrayList;
import java.util.Iterator;
import static java.lang.System.out;

public class Board {
    private static Board instance = null;
    private Tile[][] tiles;
    public int wordsCounter = 0;
    private ArrayList<Word> allNewWords = new ArrayList<>();

    private Board() {
        tiles = new Tile[15][15];
    }

    public static synchronized Board getBoard() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public Tile[][] getTiles() {
        Tile[][] copyTiles = new Tile[15][15];
        for (int i = 0; i < 15; i++) {
            System.arraycopy(tiles[i], 0, copyTiles[i], 0, 15);

        }
        return copyTiles;
    }

    public boolean boardLegal(Word word) { // check if the board is currently legal to place the new word
		boolean flagCheck = false;
		for (Tile checkTile : word.getTiles()) {
			if (checkTile != null) {
				flagCheck = true;
				break; // if there is one tile that is not null so the word is possible

			}
		}
		if (!flagCheck) {
			return false; // means all the tiles are null so the word is null
		}
		if (!isInsideBoard(word)) {
			return false;
		}
		// Check if the first word rests on the star slot
		if (!isFirstWordPlacementOnStar(word)) {
			return false;
		}
		// Check if the word rests on one of the existing tiles on the board
		if (!restsOnExistingTile(word) && wordsCounter > 0) { // the first if it passed the previous conditions word
																// must be legal
			return false;
		}
		if (isRequiredReplacement(word)) {
			return false;
		}
		return true; // means the word i legal.
	}

	private boolean isInsideBoard(Word word) {
		if (word.getRow() < 0 || word.getRow() > 14 || word.getCol() < 0 || word.getCol() > 14) {
			return false;
		}
		if ((word.isVertical()) && ((word.getRow() + word.getTiles().length - 1) > 14
				|| (word.getRow() + word.getTiles().length) < 0)) {
			return false;
		}
		if ((!word.isVertical()) && ((word.getCol() + word.getTiles().length - 1) >14
				|| (word.getCol() + word.getTiles().length) < 0)) {
			return false;
		}
		return true;
	}

	private boolean isFirstWordPlacementOnStar(Word word) {
		if (wordsCounter > 0 && tiles[7][7] != null) { // means the there is already first word is not on a star
			return true;
		} else if (wordsCounter == 0) {
			// check if the current first word can be placement on a star
			if ((word.isVertical()) && (tiles[7][7] == null && word.getCol() != 7
					|| word.getCol() == 7 && word.getRow() < 7 && (word.getRow() + word.getTiles().length) < 7)) {

				return false;
			}
			if ((!word.isVertical()) && (tiles[7][7] == null && word.getRow() != 7
					|| word.getRow() == 7 && word.getCol() < 7 && (word.getCol() + word.getTiles().length) < 7)) {

				return false;
			}
		}

		return true;

	}

	private boolean restsOnExistingTile(Word word) {

		int row = word.getRow();
		int col = word.getCol();
		int firstRow = word.getRow();
		int firstCol = word.getCol();
		int lastCol = word.getTiles().length + firstCol - 1;
		int lastRow = word.getTiles().length + firstRow - 1;
		int i = 0;
		Tile[] tempTiles = word.getTiles();

		if (word.isVertical()) { // check the relevant situations, if the word is vertical or not
			while ((tempTiles.length + firstRow) > row) {
				if (row > 14 || row < 0 || col < 0 || col > 14 || lastCol < 0 || lastRow > 14 || lastRow < 0) {
					return false;
				} else if (i < tempTiles.length && tempTiles[i] == null && tiles[row][col] == null) { // if the current	                    // check it
					i++;
					row++;
					continue;
				} else if ((col == 0) && (tiles[row][col + 1] != null)) {
					return true;
				} else if ((col == 14) && (tiles[row][col - 1] != null)) {
					return true;
				} else if ((col > 0 && col < 14) && (tiles[row][col - 1] != null || tiles[row][col + 1] != null)) {
					return true;
				}
				row += 1;
			}
		} else {
			while ((word.getTiles().length + firstCol) > col) {
				if (row > 14 || row < 0 || col < 0 || col > 14 || lastCol > 14 || lastCol < 0 || lastRow < 0) {
					return false;
				} else if (i < tempTiles.length && tempTiles[i] == null && tiles[row][col] == null) {
					i++;
					col++;
					continue;
				} else if ((row == 0) && (tiles[row + 1][col] != null)) {
					return true;
				} else if ((row == 14) && (tiles[row - 1][col] != null)) {
					return true;
				} else if ((row > 0 && row < 14) && (tiles[row - 1][col] != null || tiles[row + 1][col] != null)) {
					return true;
				}
				col += 1;
			}

		}
		return false;
	}

	private boolean isRequiredReplacement(Word word) {
		int row = word.getRow();
		int col = word.getCol();
		boolean vertical = word.isVertical();

		for (Tile checkTile : word.getTiles()) {
			if (checkTile != null && tiles[row][col] != null) {
				return true;
			}
			if (vertical) {
				row += 1;
			} else {
				col += 1;
			}
		}
		return false;
	}


    public boolean dictionaryLegal(Word word) {
        return true;
    }

    public ArrayList<Word> getWords(Word word) {

        if (!dictionaryLegal(word)) {
            return allNewWords;
        }

        allNewWords.add(word);
        return allNewWords;
    }

    public int getScore(Word word) {
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();
        int temp = 0;
        int totalScore = 0;
        int wordBonus = 1;

        for (int i = 0; i < word.getTiles().length; i++) {
            if (row < 0 || row > 14 || col < 0 || col > 14) {
                return 0;
            }
            if (tiles[row][col] != null) {
                temp += (tiles[row][col].score * checkLetterBonus(row, col));
            }
            wordBonus *= checkWordBonus(row, col);
            if (vertical) {
                row += 1;
            } else {
                col += 1;
            }
        }
        totalScore += (temp * wordBonus);
        return totalScore;
    }

    public int checkWordBonus(int row, int col) {
        if (row < 0 || row > 14 || col < 0 || col > 14) {
            return 0; // the word is illegal
        } else if (row == 7 && col == 7 && wordsCounter == 1) { // star bonus for the first word
            return 2;
        } else if ((row == 0 && col == 0) || (row == 0 && col == 7) || (row == 0 && col == 14) ||
                (row == 7 && col == 0) || (row == 7 && col == 14) ||
                (row == 14 && col == 0) || (row == 14 && col == 7) || (row == 14 && col == 14)) {
            return 3; // 3 word bonus for a red tile
        } else if ((row == 1 && col == 1) || (row == 1 && col == 13)
                || (row == 2 && col == 2) || (row == 2 && col == 12)
                || (row == 3 && col == 3) || (row == 3 && col == 11)
                || (row == 4 && col == 4) || (row == 4 && col == 10)
                || (row == 10 && col == 4) || (row == 10 && col == 10)
                || (row == 11 && col == 3) || (row == 11 && col == 11)
                || (row == 12 && col == 2) || (row == 12 && col == 12)
                || (row == 13 && col == 1) || (row == 13 && col == 13)) {

            return 2; // word bonus for a yellow tile
        }

        return 1; // no bonus
    }

    public int checkLetterBonus(int row, int col) {
        if (row < 0 || row > 14 || col < 0 || col > 14) {
            return 0; // word is illegal
        } else if ((row == 0 && col == 3) || (row == 0 && col == 11)
                || (row == 2 && col == 6) || (row == 2 && col == 8)
                || (row == 3 && col == 0) || (row == 3 && col == 7) || (row == 3 && col == 14)
                || (row == 6 && col == 2) || (row == 6 && col == 6) || (row == 6 && col == 8) || (row == 6 && col == 12)
                || (row == 7 && col == 3) || (row == 7 && col == 11)
                || (row == 8 && col == 2) || (row == 8 && col == 6) || (row == 8 && col == 8) || (row == 8 && col == 12)
                || (row == 11 && col == 0) || (row == 11 && col == 7) || (row == 11 && col == 14)
                || (row == 12 && col == 6) || (row == 12 && col == 8)
                || (row == 14 && col == 3) || (row == 14 && col == 11)) {

            return 2; // double the letter score
        } else if ((row == 1 && col == 5) || (row == 1 && col == 9)
                || (row == 5 && col == 2) || (row == 5 && col == 5) || (row == 5 && col == 9) || (row == 5 && col == 13)
                || (row == 9 && col == 2) || (row == 9 && col == 5) || (row == 9 && col == 9) || (row == 9 && col == 13)
                || (row == 13 && col == 5) || (row == 13 && col == 9)) {

            return 3; // triple the letter score
        }
        return 1; // no bonus
    }

    private void createNewVerticalWord(Word word, int row, int col) {
        int calcSize = 0;
        int tempRow = row;
        int startIndex = 0;
        if (row < 0 || row > 14 || col < 0 || col > 14) {
            return;
        }
        while ((tempRow > 0) && tiles[tempRow][col] != null) {
            if (tiles[tempRow - 1][col] != null) {
                tempRow -= 1;
                calcSize += 1;
            } else {
                break;
            }

        }
        startIndex = tempRow;
        calcSize = 0;

        while ((tempRow < 14) && tiles[tempRow][col] != null) {
            if (row < 0 || row > 14 || col < 0 || col > 14 || tempRow < 0) {
                return;
            }
            if (tiles[tempRow + 1][col] != null) {
                tempRow += 1;
                calcSize += 1;
            } else {
                break;
            }
        }

        tempRow = startIndex;
        Tile[] newWord = new Tile[calcSize + 1];
        for (int j = 0; j < newWord.length; j++) {
            if (tempRow <= 14) {
                newWord[j] = tiles[tempRow][col];
            }
            tempRow += 1;

        }
        Word w = new Word(newWord, startIndex, col, true);
        allNewWords = getWords(w);

    }

    private void createNewNotVerticalWord(Word word, int row, int col) {
        int calcSize = 0;
        int tempCol = col;
        int startIndex = 0;
        if (row < 0 || row > 14 || col < 0 || col > 14) {
            return;
        }
        while ((tempCol > 0) && tiles[row][tempCol] != null) {
            if (tempCol < 0 || tempCol > 15) {
                break;
            }
            if (tiles[row][tempCol - 1] != null) {
                tempCol -= 1;
                calcSize += 1;
            } else {
                break;
            }

        }
        startIndex = tempCol;
        calcSize = 0;

        while ((tempCol < 14 && tempCol >= 0) && tiles[row][tempCol] != null) {
            if (row < 0 || row > 14 || col < 0 || col > 14) {
                return;
            }
            if (tiles[row][tempCol + 1] != null) {
                tempCol += 1;
                calcSize += 1;
            } else {
                break;
            }
        }

        tempCol = startIndex;
        Tile[] newWord = new Tile[calcSize + 1];
        for (int j = 0; j < newWord.length; j++) {
            newWord[j] = tiles[row][tempCol];
            tempCol += 1;

        }
        Word w = new Word(newWord, row, col, false);
        allNewWords = getWords(w);
    }

    void RemoveCurrWords() {
        Iterator<Word> iterator = allNewWords.iterator();
        while (iterator.hasNext()) {
            Word w = iterator.next();
            iterator.remove();
        }
    }

    public int tryPlaceWord(Word word) {
        int scores = 0;

        if (!boardLegal(word)) {
            return 0; // Word placement is not legal on the board
        } else {
            if (!dictionaryLegal(word)) {
                return 0;
            }
            wordsCounter += 1;
            allNewWords = getWords(word); // create a new array list of all words that can be created
            int row = word.getRow();
            int col = word.getCol();
            boolean vertical = word.isVertical();

            Tile[] wordTiles = word.getTiles();
            for (Tile wordTile : wordTiles) {

                if (row < 0 || row > 14 || col < 0 || col > 14) {
                    return 0;
                }

                if (tiles[row][col] == null) {
                    tiles[row][col] = wordTile;
                }

                if (wordTile != null && (!vertical) && ((row > 0 && tiles[row - 1][col] != null)
                        || ((row < 14) && (tiles[row + 1][col] != null)))) {
                    createNewVerticalWord(word, row, col); // create a new word to the dictionary and calc the new score
                }
                if (wordTile != null && (vertical) && ((col > 0 && tiles[row][col - 1] != null)
                        || ((col < 14) && (tiles[row][col + 1] != null)))) {
                    createNewNotVerticalWord(word, row, col);
                }

                if (vertical) {
                    row += 1;
                } else {
                    col += 1;
                }

            }

        }

        for (Word w : allNewWords) {
            scores += getScore(w); 
        }
        RemoveCurrWords();
        return scores;
    }
}
