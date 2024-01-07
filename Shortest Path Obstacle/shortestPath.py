import tkinter as tk
from tkinter import ttk
from collections import deque

def display(grid, path, seen):
    # Create display window using TKinter
    root = tk.Tk()
    root.title(f"Shortest Path Through Obstacles")
    width = 800
    height = 600

    screenWidth = root.winfo_screenwidth()
    screenHeight = root.winfo_screenheight()

    xPos = (screenWidth/2) - (width/2)
    yPos = (screenHeight/2) - (height/2)

    root.geometry('%dx%d+%d+%d' % (width, height, xPos, yPos))
    root.resizable(False, False)

    # Create the frame that will be displayed in the window
    frame = tk.Frame(root)
    frame.pack(fill=tk.BOTH, expand=True)

    # If the end position was reached, the optimal path will be shown in green
    # The non-optimal or non-visited cells will remain white
    if path:
        for i, row in enumerate(grid):
            frame.grid_rowconfigure(i, weight=1)
            for j, cell_value in enumerate(row):
                frame.grid_columnconfigure(j, weight=1)
                bg_color = "white" if not path or (i, j) not in path else "green"
                label = tk.Label(frame, text=cell_value, width=5, height=2, borderwidth=1, relief="solid", font=("Helvetica", 100), bg=bg_color)
                label.grid(row=i, column=j, sticky="nsew")
    
    # If the end position couldn't be reached, display the obstacle course in red
    else:
        for i, row in enumerate(grid):
            frame.grid_rowconfigure(i, weight=1)
            for j, cell_value in enumerate(row):
                frame.grid_columnconfigure(j, weight=1)
                bg_color = "red" 
                label = tk.Label(frame, text=cell_value, width=5, height=2, borderwidth=1, relief="solid", font=("Helvetica", 100), bg=bg_color)
                label.grid(row=i, column=j, sticky="nsew")

    # Display the window
    root.mainloop()

def shortestPath(grid) -> int:
    # Find dimensions to stay within the grid
    row, col = len(grid), len(grid[0])

    # The set and queue allows us to keep track of previously visited cells 
    seen = set()
    queue = deque()

    # Add the starting position which is the top left cell
    seen.add((0, 0))

    # Add the x,y position of the cells, as well as the length which starts off as 1
    # The list will keep track of the optimal path, so it is initialized with the starting position
    queue.append((0, 0, 1, [(0, 0)])) 

    # If the starting or ending position has an obstacle, the obstacle course cannot be completed
    # Return false to the display function to display a red obstacle course
    if grid[0][0] == 1 or grid[-1][-1] == 1:
        path = False
        return display(grid, path, seen)

    # While there are still possible cells to travel to
    while queue:
        for i in range(len(queue)):
            # Keep track of the current cell and the length of the path so far
            r, c, length, current_path = queue.popleft()

            # If the ending position is reached, the path will be displayed in green
            if (r == row - 1) and (c == col - 1):
                path = current_path
                print(length)
                print(path)
                return display(grid, path, seen)

            # Every possible directions --> up/down, left/right, diagonally in each direction
            directions = [[0, 1], [0, -1], [1, 0], [-1, 0], [1, 1], [-1, 1], [1, -1], [-1, -1]]
            for rd, cd in directions:
                # Check if the next cell to travel to is in bounds and not an obstacle
                newR, newC = r + rd, c + cd
                if (newR not in range(row)) or (newC not in range(col)) or grid[newR][newC] == 1 or (newR, newC) in current_path or (newR, newC) in seen:
                    continue

                # If the next cell is okay to travel to add it to the seen set
                # Also add it to the queue along with the path
                newPath = current_path + [(newR, newC)]  
                queue.append((newR, newC, length + 1, newPath))
                seen.add((newR, newC)) 

    # If the end of the obstacle course cannot be reached, display the course in red
    path = False
    return display(grid, path, seen)

# Hit Run to find the shortest path in the obstacle course below
shortestPath(grid=[[0, 0, 0], [0, 1, 1], [0, 0, 0]])