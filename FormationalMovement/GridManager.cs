using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GridManager : MonoBehaviour
{
    private NodeManager[,] grid;
    private int nodeSize;
    private int rows;
    private int cols;

    void Start()
    {
        nodeSize = 1;
        rows = 50;
        cols = 50;

        GenerateGrid();
        DecomposeWorld();
    }

    public void GenerateGrid()
    {
        grid = new NodeManager[rows, cols];

        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                grid[row, col] = new NodeManager(row, col, true, null);
    }

    public void DecomposeWorld()
    {
        float startX = 0;
        float startZ = 0;
        float nodeCenterOffset = nodeSize / 2f;

        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
            {
                float x = startX + nodeCenterOffset + (nodeSize * row);
                float z = startZ + nodeCenterOffset + (nodeSize * col);

                Vector3 startPos = new Vector3(x, 20f, z);

                // Does our raycast hit anything at this point in the map
                RaycastHit hit;

                // Bit shift the index of the layer (8) to get a bit mask
                int layerMask = 1 << 8;

                // This would cast rays only against colliders in layer 8.
                // But instead we want to collide against everything except layer 8. The ~ operator does this, it inverts a bitmask.
                layerMask = ~layerMask;

                // Does the ray intersect any objects excluding the player layer
                if (Physics.Raycast(startPos, Vector3.down, out hit, Mathf.Infinity, layerMask))
                {
                    Debug.DrawRay(startPos, Vector3.down * 20, Color.red, 50000);
                    grid[row, col].SetNavigable(false);
                    Debug.Log("Decomposing [" + row + ", " + col + "]: " + grid[row, col].GetNavigable());
                }
                else
                {
                    // Debug.DrawRay(startPos, Vector3.down * 20, Color.green, 50000);
                    grid[col, row].SetNavigable(true);
                }
            }
    }
    public void SetNodesToNull()
    {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i, j].SetParent(null);
    }

    public List<NodeManager> GetNeighbors(NodeManager node)
    {
        List<NodeManager> neighbors = new List<NodeManager>();

        // access and store x and z position for neighbor finding
        int x = node.GetXPosition();
        int z = node.GetZPosition();

        // check to see if out of bounds, if not check to see if it is navigable, if not add it to the neighbors list
        // down
        if (x + 1 < rows)
            if (grid[x + 1, z].GetNavigable())
                neighbors.Add(grid[x + 1, z]);

        // up
        if (x - 1 >= 0)
            if (grid[x - 1, z].GetNavigable())
                neighbors.Add(grid[x - 1, z]);

        // left
        if (z - 1 >= 0)
            if (grid[x, z - 1].GetNavigable())
                neighbors.Add(grid[x, z - 1]);

        // right
        if (z + 1 < cols)
            if (grid[x, z + 1].GetNavigable())
                neighbors.Add(grid[x, z + 1]);

        // up-right
        if (x - 1 >= 0 && z + 1 < cols)
            if (grid[x - 1, z + 1].GetNavigable())
                neighbors.Add(grid[x - 1, z + 1]);

        // up-left
        if (x - 1 >= 0 && z - 1 >= 0)
            if (grid[x - 1, z - 1].GetNavigable())
                neighbors.Add(grid[x - 1, z - 1]);

        // down-left
        if (x + 1 < rows && z - 1 >= 0)
            if (grid[x + 1, z - 1].GetNavigable())
                neighbors.Add(grid[x + 1, z - 1]);

        // down-right
        if (x + 1 < rows && z + 1 < cols)
            if (grid[x + 1, z + 1].GetNavigable())
                neighbors.Add(grid[x + 1, z + 1]);

        return neighbors;
    }

    public NodeManager GetNode(int x, int z) { return grid[x, z]; }
    public int GetNodeSize() { return nodeSize; }
}
