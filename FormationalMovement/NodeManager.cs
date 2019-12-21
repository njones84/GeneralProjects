using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NodeManager
{
    // variables for position on grid and cost to traverse
    private int path;
    private int heuristic;
    private int f;
    private int x;
    private int z;

    private bool navigable;
    private NodeManager parent;

    // constructor for when making grid of nodes
    public NodeManager(int x, int z, bool navigable, NodeManager parent)
    {
        this.x = x;
        this.z = z;
        this.navigable = navigable;
        this.parent = parent;
    }

    // getter and setter (helper) functions
    public int GetSumCost() { return f; }
    public int GetPathCost() { return path; }
    public int GetHeuristicCost() { return heuristic; }
    public int GetXPosition() { return x; }
    public int GetZPosition() { return z; }
    public NodeManager GetParent() { return parent; }
    public bool GetNavigable() { return navigable; }
    public void SetHeuristicCost(int other) { heuristic = other; }
    public void SetPathCost(int other) { path = other; }
    public void SetSumCost(int other) { f = other; }
    public void SetNavigable(bool other) { navigable = other; }
    public void SetParent(NodeManager other) { parent = other; }
}
