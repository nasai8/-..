import java.util.*;

public class BFSPathFinder {

    char[][] map;
    int rows, cols;

    public BFSPathFinder(char[][] map) {
        this.map = map;
        rows = map.length;
        cols = map[0].length;
    }

    public List<Node> findPath(int sx, int sy, int tx, int ty) {

        Queue<Node> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.add(new Node(sx, sy, null));
        visited[sx][sy] = true;

        Node target = null;
        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!queue.isEmpty()) {
            Node cur = queue.poll();

            if (cur.x == tx && cur.y == ty) {
                target = cur;
                break;
            }

            for (int[] d : dirs) {
                int nx = cur.x + d[0];
                int ny = cur.y + d[1];

                if (nx >= 0 && ny >= 0 &&
                        nx < rows && ny < cols &&
                        !visited[nx][ny] &&
                        map[nx][ny] != '#') {

                    visited[nx][ny] = true;
                    queue.add(new Node(nx, ny, cur));
                }
            }
        }

        if (target == null) return null;

        Stack<Node> stack = new Stack<>();
        while (target != null) {
            stack.push(target);
            target = target.parent;
        }

        List<Node> path = new ArrayList<>();
        while (!stack.isEmpty()) {
            path.add(stack.pop());
        }
        return path;
    }
}
