# General Projects
This repository was created for any general projects that I have decided to take on.

Examples of projects you will find in here are: A* pathfinding. Game projects in Unity. Web scraping scripts to gather data. Programs of that nature.

---

## Directory
* [A* in Java](/AStar)
  1. Main.java - contains the code to set up a basic frame to allow the user to choose between console or grid visualization.
  2. Grid.java - contains grid constructors and helpful functions for the grid to help generate, block, etc..
  3. Node.java - contains node constructors and multiple getter/setter functions.
  4. Pathfinder.java - contains the A* constructors and algorithm.
  5. GridPanel.java - custom JPanel class that has a modified paint component and node/obstacle placement.
  6. Frame.java - custom JFrame class that sets up the frame for the grid visualization.
* [A* in Unity/C#](/FormationalMovement)
  1. PlayerMovement.cs - this script utilizes A* to move around in the game world and to create obstacles. 
  2. FormationMovement.cs - this script allows the user to add entities that will follow an invisible leader. Only the leader uses A*.
  3. Pathfinder.cs - A* script.
  4. GridManager.cs - Grid script with helpful functions to generate the grid, determine if a node is blocked, etc..
  5. NodeManager.cs - Node script with getter/setter functions.
* [Web Scraper Scripts](/Scraper%20Scripts) - All of these scripts use Selenium Webdriver to gather information from the html source code.
  1. AEW_WebScraper.py
  2. WWE_WebScraper.py
  3. WrestleTalk_WebScraper.py
