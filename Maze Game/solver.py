from game import Game
import sys

def gameSolveMove(game,move):
        '''Updates the attributes of game instance after a move is made,'e' cannot be used twice in a row.

        Arguments:
            move -- 'a','s','d','w','e'
            game instance
        Returns:
            None: if the move count has not increased
            game instance: an updated intance returned if a valid move has been made
        '''
        game.player.move(move)
        if game.temp_move=='e' and move=='e':
            return None
        game.temp_move=move      
        col=game.player.col
        row=game.player.row 
        if row>game.bounds[0] or col>game.bounds[1] or col<0 or row<0:
            return None   
        temp_move_count=game.move_count
        game.cells[row][col].step(game)       
        if game.move_count==temp_move_count +1 and game.state!='lose':
            if game.cells[row][col] not in game.traversed:
                game.traversed.append(game.cells[row][col])
                return game

def clone(game):
        '''Creates a copy of game instance. has different address in memory for: instance itself, player instance, grid and move list '''
        clone=game.__class__
        clone=clone.__new__(clone)
        clone.cells=[]
        clone.traversed=game.traversed
        for y in game.cells:
            row=[]
            for x in y:
                row.append(x)
            clone.cells.append(row)
        clone.moves=list(game.moves)
        clone.move_count=game.move_count
        clone.temp_move=game.temp_move
        clone.state=game.state
        clone.bounds=game.bounds
        
        clone.player=game.player.__class__
        clone.player=clone.player.__new__(clone.player)
        clone.player.game=clone
        clone.player.row=game.player.row
        clone.player.col=game.player.col
        clone.player.num_water_buckets=game.player.num_water_buckets
       
        return clone

def solve(mode,game):
    '''Finds a sequence of moves that can be used to win a game using BFS or DFS algorithm. if BFS algorithm is used this game has the least possible amount of moves to win.'''
    node_list=[game]
    while True:
        if len(node_list)==0:
            return "There is no possible path."
        if mode=='DFS':
            current=node_list.pop()
        elif mode=='BFS':
            current=node_list.pop(0)
        if current.state=='win':
            return "Path has {} moves.".format(current.move_count) +'\nPath: {}'.format(', '.join(current.moves))
        
        down=gameSolveMove(clone(current),'s')
        right=gameSolveMove(clone(current),'d')
        up=gameSolveMove(clone(current),'w')
        left=gameSolveMove(clone(current),'a')
        wait=gameSolveMove(clone(current),'e')

        if down!=None:
            node_list.append(down)
        if right!=None:
            node_list.append(right)
        if up!=None:
            node_list.append(up)
        if left!=None:
            node_list.append(left)
        if wait!=None:
            node_list.append(wait)
        

if __name__ == "__main__":
    mode=sys.argv[2]
    filename=sys.argv[1]
    output=solve(mode,Game(filename))
    print(output)

