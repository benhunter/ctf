import string
from pprint import pprint


EQUATIONSSTR = '''B9 + B8 + C1 + H4 + H4 = 23
A5 + D7 + I5 + G8 + B3 + A5 = 19
I2 + I3 + F2 + E9 = 15
I7 + H8 + C2 + D9 = 26
I6 + A5 + I3 + B8 + C3 = 20
I7 + D9 + B6 + A8 + A3 + C4 = 27
C7 + H9 + I7 + B2 + H8 + G3 = 31
D3 + I8 + A4 + I6 = 27
F5 + B8 + F8 + I7 + F1 = 33
A2 + A8 + D7 + E4 = 21
C1 + I4 + C2 + I1 + A4 = 20
F8 + C1 + F6 + D3 + B6 = 25'''


def gridstring(gridstr):
    gridstr = gridstr.replace('.', '0')

    position = 0
    grid = []
    for row in range(9):
        grid.append([])
        # grid[row] = []
        for column in range(9):
            grid[row].append(int(gridstr[position]))
            position += 1
    return grid


def test_gridstring():
    pprint(gridstring("123456789123456789123456789123456789123456789123456789123456789123456789123456789"))
    pprint(gridstring("123456789789123456456789123312845967697312845845697312231574698968231574574968231"))


def make_equations(equationsstr):
    equationsstr = equationsstr.splitlines()
    for pos, e in enumerate(equationsstr):
        equationsstr[pos] = [x.strip() for x in e.split('=')]
        equationsstr[pos][0] = [x.strip() for x in equationsstr[pos][0].split('+')]
        equationsstr[pos][1] = int(equationsstr[pos][1])
        items = []
        for item in equationsstr[pos][0]:
            # print(item[0], string.ascii_uppercase.find(item[0]), item[1])
            items.append([string.ascii_uppercase.find(item[0]), int(item[1])-1])
        equationsstr[pos][0] = items
            
        # print(equationsstr[pos][0])
    # print(equationsstr)
    return equationsstr


def test_make_equations():
    pprint(make_equations(EQUATIONSSTR))

def checksections(grid):
    # test all the 3x3 sections
    print('Check 3x3')
    for column in range(0, 9, 3):
        for row in range(0, 9, 3):
            # print(column, row)
            section = grid[row][column:column+3] + grid[row+1][column:column+3] + grid[row+2][column:column+3]            
            for i in range(1, 9):
                if i not in section:
                    print(False, i, column, row, section)
                    return False
    return True


def checkrows(grid):
    print('Check rows')
    for row in range(9):
        for i in range(1, 9):
            if i not in grid[row]:
                print(False, i, row, grid[row])
                return False
    return True


def checkcolumns(grid):
    print('Check columns')

    for column in range(9):
        columndata = []
        for row in range(9):
            columndata.append(grid[row][column])
        for i in range(1, 9):
            if i not in columndata:
                print(False, i, row, column, columndata)
                return False
    return True


def checkequations(grid, equations=make_equations(EQUATIONSSTR)):
    print("Check equations")

    for e in equations:
        # print(e)
        # print(e[0])
        sum = 0
        for coord in e[0]:
            # print(coord, grid[coord[0]][coord[1]])
            sum += grid[coord[0]][coord[1]]
        if sum != e[1]:
            return False
    return True


def checkgrid(grid):
    result = checksections(grid) and checkrows(grid) and checkcolumns(grid) and checkequations(grid)
    return result


def solve(grid):
    # add a bool to see whether we increment that position
    givengrid = []
    for row in range(9):
        givengrid.append([])
        for column in range(9):
            if grid[row][column] == 0:
                givengrid[row].append(False)  # False means we don't know this location
                grid[row][column] = 1  # change all 0 to 1

            else:
                givengrid[row].append(True)  # True means this location was given


    ## TODO ## resume working solver here
    # increment all the 0 positions, one by one and check solution
    checkgrid(grid)
    for row in range(9):
        for column in range(9):
            
            if givengrid[row][column]:
                # This position must not change
                continue

            next = grid[row][column] + 1
            for i in range(grid[row][column], 10):

            
            grid[row][column] += 1
            if checkgrid(grid):
                return(grid)
    return None


# grid = gridstring("123456789789123456456789123312845967697312845845697312231574698968231574574968231")
# print(checkgrid(grid))

grid = gridstring("........1.12............2..........2.2......................12......1......1.....")
pprint(grid)
print(solve(grid))



# sudoku grid formats
'''

.........
.........
.........
.........
.........
.........
.........
.........
.........

........1
.12......
......2..
........2
.2.......
.........
......12.
.....1...
...1.....


'''

