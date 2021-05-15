from player import Player
def grid_to_string(grid, player):
    """creates display for game

    Arguments:
        grid -- list of list of Cells
        player -- a Player with water buckets

    Returns:
        string: A string representation of the grid and player.
    """
    output=''
    i=0
    while i<len(grid):
        c=0
        while c<len(grid[i]):
            if c==player.col and i==player.row :
                output+=player.display
            else:
                output+=grid[i][c].display
            c+=1
        output+='\n'
        i+=1
    if player.num_water_buckets!=1:
        output+='\nYou have {} water buckets.\n'.format(player.num_water_buckets)
    else:
        output+='\nYou have {} water bucket.\n'.format(player.num_water_buckets)

    return output
