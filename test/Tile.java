
package test;

import java.util.Arrays;
import java.util.Random;


public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public static Tile createTile(char letter, int score) {
        return new Tile(letter, score);
    }

    

    public static class Bag {
        private int[] tilesNum = new int[26]; // array to hold counts of each letter
	    private Tile[] tiles = new Tile[26];
	    private int[] scores = new int[26]; // array to hold scores of each letter
	    private Random random = new Random();
        int[] letterCountsData = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        int[] scoresData = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
        private static Bag instance = null;

	    private Bag() {

            for (int i = 0; i < 26; i++) {
                tilesNum[i] = letterCountsData[i];
                scores[i] = scoresData[i];
            }
            
            char letter = 'A';
            for (int i = 0; i < 26; i++) {
                tiles[i] = createTile(letter++, scores[i]);
            }

        }
        

        public static Bag getBag() {
            if (instance == null) {
                instance = new Bag();
            }
            return instance;
        }

        public Tile getRand() {
            int totalTiles = 0;
            for (int count : tilesNum) {
                totalTiles += count;
            }
            if (totalTiles == 0) {
                return null;
            }
            Random random = new Random();
            int randIndex = random.nextInt(26);

            while (tilesNum[randIndex] - 1 == 0) {
                randIndex = random.nextInt(26);
            }

            tilesNum[randIndex]--;

            return tiles[randIndex];
        }

        public Tile getTile(char letter) {
            int index = -1;
            for (int i = 0; i < 26; i++) {
                if (tiles[i].letter == letter &&tilesNum[i] > 0) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return null;
            }
            tilesNum[index]--;
            return tiles[index];
        }

        public void put(Tile tile) {
			if (tile == null) {
				return;
			}
			int index = tile.letter - 'A';
			if (index < 0 || index > 25) {
				return;
			
		}
			if (tilesNum[index] < letterCountsData[index]) { // to check is it's legal to increase the specific tile amount
				tilesNum[index]++;
			}
		}

        public int size() {
            int totalTiles = 0;
            for (int count : tilesNum) {
                totalTiles += count;
            }
            return totalTiles;
        }

        public int[] getQuantities() {
            return Arrays.copyOf(tilesNum, tilesNum.length);
        }
        

    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + letter;
        result = prime * result + score;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tile other = (Tile) obj;
        if (letter != other.letter)
            return false;
        if (score != other.score)
            return false;
        return true;
    }

}
