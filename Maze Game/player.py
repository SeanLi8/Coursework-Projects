from cells import Start
class Player:
    def __init__(self,game):
        self.display = 'A'
        self.game=game
        self.num_water_buckets = 0
        for row in game.cells:
            for column in row:
                if type(column)==Start:
                    self.col=row.index(column)
                    self.row=game.cells.index(row)

    def move(self, move):
        '''Sets new player position dependent on direction determined by move parameter'''
        if move not in ['w','a','s','d','e','q']:
            raise ValueError("Please enter a valid move (w, a, s, d, e, q).\n")
        if move=='w':
            self.row-=1
        if move=="a":
            self.col-=1
        if move=="s":
            self.row+=1
        if move=='d':
            self.col+=1
        if move=='e':
            pass


