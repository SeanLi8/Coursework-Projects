class Start:
    def __init__(self):
        self.display = 'X'
            
    def step(self, game):
        game.moves.append(game.temp_move)
        game.move_count+=1


class End:
    def __init__(self):
        self.display = 'Y'
        
    def step(self, game):
        '''Ends thae game with the player winning'''
        game.state='win'
        game.moves.append(game.temp_move)
        game.move_count+=1
        if game.move_count==1:
            return '''
You conquer the treacherous maze set up by the Fire Nation and reclaim the Honourable Furious Forest Throne, restoring your hometown back to its former glory of rainbow and sunshine! Peace reigns over the lands.

You made {} move.
Your move: {}

=====================
====== YOU WIN! =====
====================='''.format(game.move_count,', '.join(game.moves))
        else:
            return '''
You conquer the treacherous maze set up by the Fire Nation and reclaim the Honourable Furious Forest Throne, restoring your hometown back to its former glory of rainbow and sunshine! Peace reigns over the lands.

You made {} moves.
Your moves: {}

=====================
====== YOU WIN! =====
====================='''.format(game.move_count,', '.join(game.moves))

class Air:
    def __init__(self):
        self.display = ' '
        
    def step(self, game):
        game.moves.append(game.temp_move)
        game.move_count+=1


class Wall:
    def __init__(self):
        self.display = '*'
        
    def step(self, game):
        '''Sets player position back to what it originally was'''
        last_move=game.temp_move
        if last_move=='w':
            game.player.move('s')
        if last_move=="a":
            game.player.move('d')
        if last_move=="s":
            game.player.move('w')
        if last_move=='d':
            game.player.move('a')
        return "You walked into a wall. Oof!\n"
        
        


class Fire:
    def __init__(self):
        self.display = 'F'

    def step(self, game):
        '''If the player has no water buckets then the game ends with player losing, if they do then fire is replaced with an air cell'''
        if game.player.num_water_buckets>0:
            game.player.num_water_buckets-=1
            game.moves.append(game.temp_move)
            game.move_count+=1
            game.cells[game.player.row][game.player.col]=Air()
            return "With your strong acorn arms, you throw a water bucket at the fire. You acorn roll your way through the extinguished flames!\n"
        else:
            game.moves.append(game.temp_move)
            game.move_count+=1
            game.state="lose"
            if game.move_count==1:
                return '''
You step into the fires and watch your dreams disappear :(.

The Fire Nation triumphs! The Honourable Furious Forest is reduced to a pile of ash and is scattered to the winds by the next storm... You have been roasted.

You made {} move.
Your move: {}

=====================
===== GAME OVER =====
====================='''.format(game.move_count,', '.join(game.moves))
            else:
                return'''
You step into the fires and watch your dreams disappear :(.

The Fire Nation triumphs! The Honourable Furious Forest is reduced to a pile of ash and is scattered to the winds by the next storm... You have been roasted.

You made {} moves.
Your moves: {}

=====================
===== GAME OVER =====
====================='''.format(game.move_count,', '.join(game.moves))

            


class Water:
    def __init__(self):
        self.display = 'W'  

    def step(self, game):
        game.player.num_water_buckets+=1
        game.moves.append(game.temp_move)
        game.move_count+=1
        game.cells[game.player.row][game.player.col]=Air()
        game.traversed=[]
        return "Thank the Honourable Furious Forest, you've found a bucket of water!\n"


class Teleport:
    def __init__(self,num):
        self.display = num  
        self.num=num
        
    def step(self, game):
        '''Sets player postion to the matching teleport pad'''
        game.moves.append(game.temp_move)
        game.move_count+=1
        
        for i in game.cells:
            for j in i:
                if type(j)==Teleport:
                    if j.num==self.num and j!=self:
                        game.player.col=i.index(j)
                        game.player.row=game.cells.index(i)
                        return 'Whoosh! The magical gates break Physics as we know it and opens a wormhole through space and time.\n'
        
