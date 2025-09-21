# Minesweeper-Game-in-Java
A classic Minesweeper game built in Java that runs on the console.  
Players can choose difficulty levels, reveal cells, place flags, and even use hints to find safe moves.  

Features
- Difficulty Levels:
  - Easy (6x6 board, 4 mines)  
  - Medium (12x12 board, 10 mines)  
  - Hard (18x18 board, 20 mines)  
- Core Gameplay:
  - Reveal cells and avoid mines  
  - Flag suspected mine locations  
  - Hint system to auto-reveal a safe cell  
- Game End:
  - Win when all safe cells are revealed  
  - Lose if a mine is triggered  
  - Tracks play time and displays results at the end  

How It Works
1. The board is generated based on chosen difficulty.  
2. Mines are randomly placed and numbers are calculated around them.  
3. Player takes actions by:
   - `R row col` → Reveal a cell  
   - `F row col` → Flag/unflag a cell  
   - `H` → Use a hint to reveal a safe cell  
4. The game continues until the player wins or hits a mine.  

Sample Gameplay
Choose Difficulty:
1. Easy (6x6, 4 mines)
2. Medium (12x12, 10 mines)
3. Hard (18x18, 20 mines)
Enter row and column (e.g., R 2 3 to reveal, F 2 3 to flag, H for hint.
