import java.util.Scanner;
import java.util.Random;

public class Main
{
    private static int SIZE;
    private static int MINES;
    private static char[][] board;
    private static int[][] minefield;
    private static boolean[][] revealed;
    private static boolean gameRunning = true;
    private static long startTime;

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Ultimate Minesweeper!");

        while(true)
        {
            System.out.println("\nMenu:");
            System.out.println("1. New Game");
            System.out.println("2. Quit");
            System.out.println("Select an Option: ");
            int choice = scanner.nextInt();

            switch(choice)
            {
                case 1:
                    chooseDifficulty();
                    playGame();
                    break;
                case 2:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void chooseDifficulty()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy (6x6, 4 mines)");
        System.out.println("2. Medium (12x12, 10 mines)");
        System.out.println("3. Hard (18x18, 20 mines)");

        int choice = scanner.nextInt();
        switch(choice)
        {
            case 1:
                initializeGame(6, 4);
                break;
            case 2:
                initializeGame(12, 10);
                break;
            case 3:
                initializeGame(18, 20);
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Easy.");
                initializeGame(6, 4);
        }
    }

    private static void initializeGame(int size, int mines)
    {
        SIZE = size;
        MINES = mines;
        board = new char[SIZE][SIZE];
        minefield = new int[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        initializeBoard();
        placeMines();
        calculateNumbers();
    }

    private static void initializeBoard()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                board[i][j] = '-'; // Hidden cells
                minefield[i][j] = 0; // No mines initially
                revealed[i][j] = false; // No cells are revealed initially
            }
        }
    }

    private static void placeMines()
    {
        Random random = new Random();
        int placedMines = 0;
        while(placedMines < MINES)
        {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if(minefield[row][col] != -1)
            { // No mine in this cell
                minefield[row][col] = -1; // Place a mine
                placedMines++;
            }
        }
    }

    private static void calculateNumbers()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(minefield[i][j] == -1)
                {
                    continue;
                }
                int mineCount = 0;
                // Check surrounding cells
                for(int x=-1;x<=1;x++)
                {
                    for(int y=-1;y<=1;y++)
                    {
                        if(i+x>=0 && i+x<SIZE && j+y>=0 && j+y<SIZE)
                        {
                            if(minefield[i + x][j + y] == -1)
                            {
                                mineCount++;
                            }
                        }
                    }
                }
                minefield[i][j] = mineCount;
            }
        }
    }

    private static void playGame()
    {
        Scanner scanner = new Scanner(System.in);
        startTime = System.currentTimeMillis();
        gameRunning = true;

        while (gameRunning)
        {
            displayBoard();
            System.out.println("Enter your move (e.g., 'R 3 4' to reveal, 'F 2 2' to flag, 'H' for hint):");

            String input = scanner.nextLine().trim(); // Read the entire line of input
            String[] parts = input.split(" "); // Split the input into parts

            if (parts.length < 1 || parts.length > 3)
            {
                System.out.println("Invalid input format. Try again.");
                continue;
            }

            String action = parts[0]; // The first part is the action (R, F, or H)

            if (action.equalsIgnoreCase("H"))
            {
                hintSystem();
                continue; // Skip further processing for hint
            }

            if (parts.length < 3)
            {
                System.out.println("Incomplete input. Provide row and column numbers. Try again.");
                continue;
            }

            // Validate row and column
            int row, col;
            if (!isNumeric(parts[1]) || !isNumeric(parts[2]))
            {
                System.out.println("Row and column must be valid numbers. Try again.");
                continue;
            }

            row = Integer.parseInt(parts[1]) - 1; // Convert to zero-based index
            col = Integer.parseInt(parts[2]) - 1;

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE)
            {
                System.out.println("Row or column out of bounds. Try again.");
                continue;
            }

            // Process the action
            if (action.equalsIgnoreCase("R"))
            {
                if (minefield[row][col] == -1)
                {
                    gameOver(true); // Hit a bomb
                }
                else
                {
                    revealCell(row, col);
                }
            }
            else if (action.equalsIgnoreCase("F"))
            {
                flagCell(row, col);
            }
            else
            {
                System.out.println("Invalid action. Use 'R', 'F', or 'H'. Try again.");
            }

            checkWinCondition();
        }

        scanner.close();
    }


    // Helper method to check if a string is numeric
    private static boolean isNumeric(String str)
    {
        if(str == null || str.isEmpty()) return false;
        for(char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }


    private static void displayBoard()
    {
        System.out.print("    ");
        for(int i=0;i<SIZE;i++)
        {
            System.out.printf("%2d   ", i + 1); // Column headers
        }
        System.out.println();
        System.out.println("   +" + "---+".repeat(SIZE)); // Top boundary

        for(int i=0;i<SIZE;i++)
        {
            System.out.printf("%2d |", i + 1); // Row header
            for (int j=0;j<SIZE;j++)
            {
                char cell = board[i][j];
                switch (cell)
                {
                    case '-':
                        System.out.print(" ⬜ |");
                        break;
                    case 'F':
                        System.out.print(" 🚩 |");
                        break;
                    default:
                        System.out.printf(" %s |", cell);
                        break;
                }
            }
            System.out.println();
            System.out.println("   +" + "---+".repeat(SIZE)); // Row boundary
        }
    }

    private static void revealCell(int row, int col)
    {
        if(revealed[row][col])
        {
            return;
        }
        revealed[row][col] = true;
        board[row][col] = (minefield[row][col] == 0) ? ' ' : (char) ('0' + minefield[row][col]);

        if(minefield[row][col] == 0)
        {
            for(int i=-1;i<=1;i++)
            {
                for(int j=-1;j<=1;j++)
                {
                    if(row+i>=0 && row+i<SIZE && col+j>=0 && col+j<SIZE)
                    {
                        revealCell(row + i, col + j);
                    }
                }
            }
        }
    }

    private static void flagCell(int row, int col)
    {
        if(board[row][col] == '-')
        {
            board[row][col] = 'F'; // Place a flag
        }
        else if(board[row][col] == 'F')
        {
            board[row][col] = '-'; // Remove the flag
        }
    }

    private static void hintSystem()
    {
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(!revealed[i][j] && minefield[i][j] != -1)
                {
                    revealCell(i, j); // Reveal a safe cell
                    System.out.println("A safe cell has been revealed!");
                    return;
                }
            }
        }
    }

    private static void gameOver(boolean hitBomb)
    {
        gameRunning = false; // Ensure the game loop stops immediately

        if (hitBomb)
        {
            System.out.println("You hit a mine! Game Over.");
        }
        else
        {
            System.out.println("You won! Congratulations!");
        }

        // Reveal all cells (including bombs) on the final board
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (minefield[i][j] == -1)
                {
                    board[i][j] = '*'; // Show bombs
                }
                else if (!revealed[i][j])
                {
                    board[i][j] = (minefield[i][j] == 0) ? ' ' : (char) ('0' + minefield[i][j]);
                }
            }
        }

        displayBoard(); // Show the final board

        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) / 1000 + " seconds.");

        // Exit the program with a clean exit code of 0
        System.exit(0);
    }


    private static void checkWinCondition()
    {
        boolean allCellsRevealed = true;
        for(int i=0;i<SIZE;i++)
        {
            for(int j=0;j<SIZE;j++)
            {
                if(minefield[i][j] != -1 && !revealed[i][j])
                {
                    allCellsRevealed = false;
                    break;
                }
            }
        }

        if(allCellsRevealed)
        {
            gameOver(false); // Call gameOver() when all safe cells are revealed
        }
    }
}
