import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final boolean solvable;
    private final int numberOfMoves;
    private final Iterable<Board> solution;

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;
        private final int priority;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            priority = moves + board.manhattan();
        }

        public int compareTo(SearchNode that) {
            return Integer.compare(priority, that.priority);
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        MinPQ<SearchNode> searchQueue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        searchQueue.insert(new SearchNode(initial, 0, null));
        twinQueue.insert(new SearchNode(initial.twin(), 0, null));

        SearchNode goal;
        while (true) {
            goal = advance(searchQueue);
            if (goal != null) {
                solvable = true;
                numberOfMoves = goal.moves;
                solution = buildSolution(goal);
                return;
            }
            if (advance(twinQueue) != null) {
                solvable = false;
                numberOfMoves = -1;
                solution = null;
                return;
            }
        }
    }

    private SearchNode advance(MinPQ<SearchNode> queue) {
        SearchNode node = queue.delMin();
        if (node.board.isGoal()) {
            return node;
        }
        for (Board neighbor : node.board.neighbors()) {
            if (node.previous == null || !neighbor.equals(node.previous.board)) {
                queue.insert(new SearchNode(neighbor, node.moves + 1, node));
            }
        }
        return null;
    }

    private Iterable<Board> buildSolution(SearchNode goal) {
        List<Board> boards = new ArrayList<>();
        for (SearchNode node = goal; node != null; node = node.previous) {
            boards.add(node.board);
        }
        Collections.reverse(boards);
        return Collections.unmodifiableList(boards);
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return numberOfMoves;
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                tiles[row][col] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
