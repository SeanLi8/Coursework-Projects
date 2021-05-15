from game_parser import read_lines
from grid import grid_to_string
from player import Player


class Game:
    def __init__(self, filename):
        self.cells=read_lines(filename)
        self.moves=[]
        self.move_count=0
        self.player=Player(self)
        self.temp_move=''
        self.state='running'
        self.traversed=[]
        self.bounds=[len(self.cells)-1,len(self.cells[0])-1]
        
    def gameMove(self, move):
        '''Sets player position, calls the step() method of a corresponding cell and then returns display of the game. If position is out of game bounds then a wall step() method is called'''
        try:
            if move=='q':
                self.state='quit'
                return
            self.player.move(move)
            self.temp_move=move
            if self.player.row>self.bounds[0] or self.player.col>self.bounds[1] or self.player.col<0 or self.player.row<0:
                msg=self.cells[0][0].step(self)
            else:
                msg=self.cells[self.player.row][self.player.col].step(self)
        except ValueError as e:
            msg=str(e)    
        
        game_display=(grid_to_string(self.cells,self.player))
        if msg!=None:
            game_display+='\n' + msg
        
        return game_display
