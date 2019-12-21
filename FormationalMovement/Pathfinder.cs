using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Pathfinder : MonoBehaviour
{
    private GridManager grid;
    private List<NodeManager> frontier;
    private List<NodeManager> fringe;
    private List<NodeManager> neighbors;
    private Stack<Vector3> path;

    // Start is called before the first frame update
    void Start()
    {
        grid = GetComponent<GridManager>();
        frontier = new List<NodeManager>();
        fringe = new List<NodeManager>();
        neighbors = new List<NodeManager>();
        path = new Stack<Vector3>();
    }

    public Stack<Vector3> AStar(Vector3 StartVector, Vector3 GoalVector)
    {
        // decompose the world in case anymore objects appeared and get the node size to make math easier
        grid.DecomposeWorld();
        int nodeSize = grid.GetNodeSize();

        // get the starting node based on the players current transform
        int StartX = (int) StartVector.x / nodeSize;
        int StartZ = (int) StartVector.z / nodeSize;
        NodeManager startNode = grid.GetNode(StartX, StartZ);

        // get the goal node based on where the player clicks (the target position)
        int EndX = (int) GoalVector.x / nodeSize;
        int EndZ = (int) GoalVector.z / nodeSize;
        NodeManager goalNode = grid.GetNode(EndX, EndZ);

        Debug.Log("Start Node: [" + StartX + ", " + StartZ + "]");
        Debug.Log("Goal Node: [" + EndX + ", " + EndZ + "]");

        // if on goal node already or goal node can not be traversed, don't do anything
        if (startNode == goalNode || goalNode.GetNavigable() == false)
            return ConstructPath(startNode, nodeSize);

        // clear the list whenever AStar is called again to ensure a fresh start
        frontier.Clear();
        fringe.Clear();
        neighbors.Clear();

        // set all nodes parents to null (NOT doing this was causing an OutOfMemoryException)
        grid.SetNodesToNull();

        // set first nodes costs
        startNode.SetHeuristicCost(System.Math.Max(System.Math.Abs(StartX - EndX), System.Math.Abs(StartZ - EndZ)));
        startNode.SetPathCost(0);
        startNode.SetSumCost(startNode.GetHeuristicCost() + startNode.GetPathCost());

        // add start node to frontier
        frontier.Add(startNode);

        while (frontier.Count > 0)
        {
            // get best move and remove it from the frontier
            NodeManager currentNode = GetBestNode();
            fringe.Add(currentNode);
            Debug.Log("Adding Node: [" + currentNode.GetXPosition() + ", " + currentNode.GetZPosition() + "] - Walkable = " + currentNode.GetNavigable());

            // get neighbors
            neighbors = grid.GetNeighbors(currentNode);

            // loop through neighbors to see what you can expand on (The beef of A*)
            foreach (NodeManager node in neighbors)
            {
                // if node you iterate over is the goal, construct the path to follow
                if (node == goalNode)
                {
                    node.SetParent(currentNode);
                    Debug.Log("Adding Node: [" + node.GetXPosition() + ", " + node.GetZPosition() + "]");
                    return ConstructPath(node, nodeSize);
                }

                // skip over the node if we have expanded on it already or its not traversable
                if (fringe.Contains(node) || !node.GetNavigable())
                    continue;

                // set the nodes costs
                int heuristicCost = System.Math.Max(System.Math.Abs(node.GetXPosition() - EndX), System.Math.Abs(node.GetZPosition() - EndZ));
                int pathCost = fringe.Count + 1;
                
                node.SetHeuristicCost(heuristicCost);
                node.SetPathCost(pathCost);
                node.SetSumCost(node.GetHeuristicCost() + node.GetPathCost());

                // if the node is on the frontier already (aka, most likely in the wrong direction), then check to see if its closer to the goal, if not skip over it
                if (frontier.Contains(node))
                    if (node.GetSumCost() > currentNode.GetSumCost())
                        continue;

                // if it passes all of our checks and is a viable node, add it to the frontier and set it's parent
                frontier.Add(node);
                node.SetParent(currentNode);
            }
        }

        return null;
    }
    private NodeManager GetBestNode()
    {
        // initialize a worst node and worst cost
        NodeManager best = null;
        int bestSumCost = int.MaxValue;

        // loop through each node in the frontier and determine which one is the best to expand on
        foreach (NodeManager x in frontier)
        {
            if (x.GetSumCost() < bestSumCost)
            {
                bestSumCost = x.GetSumCost();
                best = x;
            }
        }

        // clear the frontier for incoming better nodes and return the best current node
        frontier.Clear();
        return best;
    }
    private Stack<Vector3> ConstructPath(NodeManager node, int nodeSize)
    {
        // clear path when constructing a new path
        path.Clear();

        // translate nodes coordinates in grid to world coordinates
        float x = node.GetXPosition() * nodeSize + (nodeSize / 2f);
        float z = node.GetZPosition() * nodeSize + (nodeSize / 2f);
        path.Push(new Vector3(x, 0.5f, z));

        NodeManager currentNode = node;

        // while a node has a parent, push it onto the stack
        while (currentNode.GetParent() != null)
        {
            currentNode = currentNode.GetParent();
            x = currentNode.GetXPosition() * nodeSize + (nodeSize / 2f);
            z = currentNode.GetZPosition() * nodeSize + (nodeSize / 2f);
            path.Push(new Vector3(x, 0.5f, z));
        }

        return path;
    }
    public void ShowNodes(Stack<Vector3> path)
    {
        Debug.Log("Length of path: " + path.Count);
        foreach (Vector3 node in path)
            Debug.Log("Nodes X Position: " + node.x + ", Nodes Z Position: " + node.z);
    }
}
