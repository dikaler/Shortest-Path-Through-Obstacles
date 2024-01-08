import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ShortestPath {

    public static void main(String[] args) {
        /*
        Based on research the Swing framework should be used
        on the Event Dispatch Thread, and in order to ensure Swing
        components are accessed safely using invokeLater() seems
        to be standard practice.
         */
        SwingUtilities.invokeLater(() -> {
            // Initialize grid
            int[][] grid = {{0, 0, 0, 0, 0, 0}, {0, 1,0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}};

            // Find path from the starting position (top-left cell) to the end position (bottom-right cell)
            findShortestPath(grid);
        });
    }

    // This function will find whether or not there is a path from the starting position to the ending position
    // If there is a path, it will find the optimal path and send the cells that make up the path to the display function
    private static void findShortestPath(int[][] grid) {
        // Find boundaries to stay within
        int row = grid.length;
        int col = grid[0].length;

        // Seen set will keep track of previously seen cells so an infinite loop is avoided
        Set<Point> seen = new HashSet<>();

        // The queue will allow travel between cells
        Queue<int[]> queue = new LinkedList<>();

        // Add origin to seen
        seen.add(new Point(0, 0));

        // Add origin, starting length, and the path to the queue
        queue.add(new int[]{0, 0, 1, 0, 0});

        // currentPath keeps track of the cells that make up travelling to the current position
        int[][] currentPath = new int[grid.length * grid.length][2];

        // newPath keeps track of new cells
        int[][] newPath = new int[grid.length * grid.length][2];

        // optimalPath will store only the necessary coordinates that makeup the optimal path
        // optimalPath will be passed to the display function
        Set<Point> optimalPath = new HashSet<>();

        // If there is no way from the starting position to the end
        if (grid[0][0] == 1 || grid[row - 1][col - 1] == 1) {
            int[][] path = null;
            display(grid, path, seen);
            return;
        }

        // While there are still non-obstacle cells that can be travelled to
        while (!queue.isEmpty()) {
            // group is the queue entry that keeps track of the following information
            int[] group = queue.poll();

                // The (x,y) coordinates
            int r = group[0];
            int c = group[1];

                // The current length of the path
            int length = group[2];

                // Appending the (x,y) coordinates to the currentPath list
            currentPath[length] = new int[]{r, c};

            // If the ending position is reached successfully
            if (r == row - 1 && c == col - 1) {
                for (int z = 0; z <= length; z++) {
                    // Create a Point pair to add to the optimalPath set
                    Point coordinate = new Point(currentPath[z][0], currentPath[z][1]);
                    optimalPath.add(coordinate);
                }

                // Pass path and optimalPath to the display function
                int[][] path = Arrays.copyOf(currentPath, length + 1);
                display(grid, path, optimalPath);

                // Clear the optimalPath set
                optimalPath.clear();
                return;
            }

            // Every possible directions --> up/down, left/right, diagonally in each direction
            int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
            for (int[] direction : directions) {
                int rowDir = direction[0];
                int colDir = direction[1];

                // Check if the next cell to travel to is in bounds and not an obstacle
                if (r + rowDir < 0 || r + rowDir >= row || c + colDir < 0 || c + colDir >= col || grid[r + rowDir][c + colDir] == 1 || seen.contains(new Point(r + rowDir, c + colDir))) {
                    continue;
                }

                // If the next cell is okay to travel to add it to the seen set
                // Also add it to the queue along with the path
                newPath[length] = new int[]{r + rowDir, c + colDir};
                seen.add(new Point(r + rowDir, c + colDir));
                queue.add(new int[]{r + rowDir, c + colDir, length + 1});
            }
        }

        // If the end of the obstacle course cannot be reached, display the course in red
        seen.clear();
        int[][] path = null;
        display(grid, path, seen);
    }

    private static void display(int[][] grid, int[][] path, Set<Point> optimalPath) {
        // Create window to be displayed
        JFrame frame = new JFrame("Shortest Path Through Obstacles");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Create panel to display the obstacle course path on
        JPanel panel = new JPanel(new GridLayout(grid.length, grid[0].length));

        // If there is a path from the starting position to the ending position
        if (path != null) {

            // Iterate through cell on the grid
            // If the cell is in the optimalPath make the cell green
            // Otherwise make the cell white
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    Color bgColor = optimalPath.contains(new Point(i, j)) ? Color.GREEN : Color.WHITE;
                    JLabel label = new JLabel(String.valueOf(grid[i][j]), SwingConstants.CENTER);
                    label.setPreferredSize(new Dimension(80, 60));
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    label.setFont(new Font("Helvetica", Font.PLAIN, 20));
                    label.setOpaque(true);
                    label.setBackground(bgColor);
                    panel.add(label);
                }
            }

        // If there isn't a path from the starting position to the ending position
        } else {
            // Make every cell red to indicate no path found
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    Color bgColor = Color.RED;
                    JLabel label = new JLabel(String.valueOf(grid[i][j]), SwingConstants.CENTER);
                    label.setPreferredSize(new Dimension(80, 60));
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    label.setFont(new Font("Helvetica", Font.PLAIN, 20));
                    label.setOpaque(true);
                    label.setBackground(bgColor);
                    panel.add(label);
                }
            }
        }

        // Add the completed panel to the display window
        frame.add(panel);

        // Make the display window visible
        frame.setVisible(true);
    }
}