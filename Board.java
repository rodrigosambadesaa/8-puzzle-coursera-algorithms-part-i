import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int n;
    private final int[][] tiles;
    private final int blankRow;
    private final int blankCol;
    private final int hamming;
    private final int manhattan;

    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        int foundBlankRow = 0;
        int foundBlankCol = 0;
        int calculatedHamming = 0;
        int calculatedManhattan = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int tile = tiles[row][col];
                this.tiles[row][col] = tile;
                if (tile == 0) {
                    foundBlankRow = row;
                    foundBlankCol = col;
                } else {
                    int goalRow = (tile - 1) / n;
                    int goalCol = (tile - 1) % n;
                    if (tile != row * n + col + 1) {
                        calculatedHamming++;
                    }
                    calculatedManhattan += Math.abs(row - goalRow)
                            + Math.abs(col - goalCol);
                }
            }
        }
        blankRow = foundBlankRow;
        blankCol = foundBlankCol;
        hamming = calculatedHamming;
        manhattan = calculatedManhattan;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(n).append('\n');
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] < 10) {
                    result.append(' ');
                }
                result.append(tiles[row][col]).append(' ');
            }
            result.append('\n');
        }
        return result.toString();
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return manhattan == 0;
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (n != that.n) {
            return false;
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (tiles[row][col] != that.tiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        List<Board> result = new ArrayList<>();
        addNeighbor(result, blankRow - 1, blankCol);
        addNeighbor(result, blankRow + 1, blankCol);
        addNeighbor(result, blankRow, blankCol - 1);
        addNeighbor(result, blankRow, blankCol + 1);
        return result;
    }

    private void addNeighbor(List<Board> boards, int row, int col) {
        if (row < 0 || row >= n || col < 0 || col >= n) {
            return;
        }
        int[][] copy = copyTiles();
        copy[blankRow][blankCol] = copy[row][col];
        copy[row][col] = 0;
        boards.add(new Board(copy));
    }

    public Board twin() {
        int[][] copy = copyTiles();
        int firstRow = -1;
        int firstCol = -1;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (copy[row][col] != 0) {
                    if (firstRow < 0) {
                        firstRow = row;
                        firstCol = col;
                    } else {
                        int temporary = copy[firstRow][firstCol];
                        copy[firstRow][firstCol] = copy[row][col];
                        copy[row][col] = temporary;
                        return new Board(copy);
                    }
                }
            }
        }
        return new Board(copy);
    }

    private int[][] copyTiles() {
        int[][] copy = new int[n][n];
        for (int row = 0; row < n; row++) {
            System.arraycopy(tiles[row], 0, copy[row], 0, n);
        }
        return copy;
    }

    public static void main(String[] args) {
        int[][] tiles = {
            { 0, 1, 3 },
            { 4, 2, 5 },
            { 7, 8, 6 }
        };
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println(board.dimension());
        System.out.println(board.hamming());
        System.out.println(board.manhattan());
        System.out.println(board.isGoal());
        System.out.println(board.twin());
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
        }
    }
}
