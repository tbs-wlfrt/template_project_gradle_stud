package OurCode.Helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import java.util.Arrays;

public class WarehouseBlueprint {
    /** current_blueprint
      *   Legend: N: north, S: south, E: east, W: west
      *   Nodes: 1,2,3,4,5,6,7
      *   Visual:                  7 --- 1 --- 2
      *                           /           /
      *                          6 --------- 3
      *                         /           /
      *                        5-----------4
      *   Relations:  {
      *       1: {E: 2, W: 7}
      *       2: {W: 1, S: 3}
      *       3: {N: 2, W: 6, S: 4}
      *       4: {N: 3, W: 5}
      *       5: {N: 6, E: 4}
      *       6: {N: 7, E: 3, S: 5}
      *       7: {E: 1, S: 6}
      *   }
     */
    private static final Map<Integer, Map<String, Integer>> relations = new HashMap<>();
    static {
        relations.put(1, new HashMap<String, Integer>() {{ put("E", 2); put("W", 7); }});
        relations.put(2, new HashMap<String, Integer>() {{ put("W", 1); put("S", 3); }});
        relations.put(3, new HashMap<String, Integer>() {{ put("N", 2); put("W", 6); put("S", 4); }});
        relations.put(4, new HashMap<String, Integer>() {{ put("N", 3); put("W", 5); }});
        relations.put(5, new HashMap<String, Integer>() {{ put("N", 6); put("E", 4); }});
        relations.put(6, new HashMap<String, Integer>() {{ put("N", 7); put("E", 3); put("S", 5); }});
        relations.put(7, new HashMap<String, Integer>() {{ put("E", 1); put("S", 6); }});
    }
    /**
     * BFS function that calculates the next directions given:
     * currentNode
     * nextNode
     * linksToAvoid: a set of links to avoid, for example: (1, 2) this means avoid the link from 1 to 2
     */
    public String bfs(int currentNode, int nextNode, Set<Set<Integer>> linksToAvoid){
        Set<Integer> seen = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, String> paths = new HashMap<>();

        queue.add(currentNode);
        paths.put(currentNode, "");

        while (!queue.isEmpty()) {
            int node = queue.poll();
            String path = paths.get(node);

            if (node == nextNode) {
                return path;
            }

            if (!seen.contains(node)) {
                seen.add(node);
                for (Map.Entry<String, Integer> neighbor : relations.get(node).entrySet()) {
                    if (!seen.contains(neighbor.getValue())) {
                        // Check if the link from node to neighbor is in the linksToAvoid set
                        if (!linksToAvoid.contains(new HashSet<>(Arrays.asList(node, neighbor.getValue())))) {
                            queue.add(neighbor.getValue());
                            paths.putIfAbsent(neighbor.getValue(), path + neighbor.getKey());
                        }
                    }
                }
            }
        }

        return null;
    }
    public String calcNextPath(int currentNode, int nextNode, Set<Set<Integer>> linksToAvoid ) {
        // check if linksToAvoid is null, if so, set it to an empty set
        if (linksToAvoid == null) {
            linksToAvoid = new HashSet<>();
        }
        String path = bfs(currentNode, nextNode, linksToAvoid);
        /**
         * Now we have path, which contains the directions necessary to get to the destination node
         * the problem is that this is in the format of "EESW" for example, which is not the format we want
         * We want the format to be "RFRF" for example, which is the format that the robot understands
         * To do this, we need to convert the path to the format that the robot understands
         * this needs to take into account the robot's current orientation
         */

        String robotPath = "";
        String robotOrientation = "N";
        char[] pathArray = path.toCharArray();
        for (char direction : pathArray) {
            switch (robotOrientation) {
                case "N":
                    switch (direction) {
                        case 'N':
                            robotPath += "F";
                            break;
                        case 'E':
                            robotPath += "R";
                            robotOrientation = "E";
                            break;
                        case 'S':
                            robotPath += "B";
                            robotOrientation = "S";
                            break;
                        case 'W':
                            robotPath += "L";
                            robotOrientation = "W";
                            break;
                    }
                    break;
                case "E":
                    switch (direction) {
                        case 'N':
                            robotPath += "L";
                            robotOrientation = "N";
                            break;
                        case 'E':
                            robotPath += "F";
                            break;
                        case 'S':
                            robotPath += "R";
                            robotOrientation = "S";
                            break;
                        case 'W':
                            robotPath += "B";
                            robotOrientation = "W";
                            break;
                    }
                    break;
                case "S":
                    switch (direction) {
                        case 'N':
                            robotPath += "B";
                            robotOrientation = "N";
                            break;
                        case 'E':
                            robotPath += "L";
                            robotOrientation = "E";
                            break;
                        case 'S':
                            robotPath += "F";
                            break;
                        case 'W':
                            robotPath += "R";
                            robotOrientation = "W";
                            break;
                    }
                    break;
                case "W":
                    switch (direction) {
                        case 'N':
                            robotPath += "R";
                            robotOrientation = "N";
                            break;
                        case 'E':
                            robotPath += "B";
                            robotOrientation = "E";
                            break;
                        case 'S':
                            robotPath += "L";
                            robotOrientation = "S";
                            break;
                        case 'W':
                            robotPath += "F";
                            break;
                    }
                    break;
            }
        }
        return robotPath;  // Return null if there is no path to nextNode
    }

    public static void main(String[] args) {
        WarehouseBlueprint blueprint = new WarehouseBlueprint();
        int curr = 1;
        int next = 5;
        // avoid (7, 6) link
        Set<Set<Integer>> linksToAvoid = new HashSet<>();
        linksToAvoid.add(new HashSet<>(Arrays.asList(7, 6)));
        linksToAvoid.add(new HashSet<>(Arrays.asList(3, 4)));
        String path = blueprint.calcNextPath(curr, next, linksToAvoid);
        System.out.println("Path from node  " + curr + "  to  " + next +"   : " +  path);
    }
};


