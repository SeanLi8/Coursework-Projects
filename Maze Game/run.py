'''Handles all user input for the game "Acorn Runner", responsible for printing display of the game to terminal'''
from game import Game,grid_to_string
import os
import sys

filename=sys.argv[1]
game=Game(filename)

print(grid_to_string(game.cells,game.player))
while True:
    move=input("Input a move: ").lower()    
    game_display=game.gameMove(move)
    
    if game_display!=None:
        print(game_display)
    if game.state=="lose" or game.state=='win':
        break
    if game.state=='quit':
        print('\nBye!')
        break

sys.exit()


