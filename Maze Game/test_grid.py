from grid import grid_to_string
from player import Player
from game_parser import parse


config_1=['*X*************',
'*       1 *   *',
'* ***W** **** *',
'* 1********F  *',
'*************Y*']

config_2=['*X*',
'* *',
'* ****',
'*    Y',
'******']

def test_grid_normal():
    '''tests normal operation of grid_to_function. Should return single string which displays player position on board and water buckets'''
    player=Player.__new__(Player)
    player.display='A'
    player.col=1
    player.row=0
    player.num_water_buckets=0
    assert grid_to_string(parse(config_1),player)=='*A*************\n*       1 *   *\n* ***W** **** *\n* 1********F  *\n*************Y*\n\nYou have 0 water buckets.\n','Failed normal grid: wrong string returned'


def test_grid_water_buckets():
    '''tests behaviour of grid_to_function when player has non-zero water buckets'''    
    player=Player.__new__(Player)
    player.display='A'
    player.col=1
    player.row=1
    player.num_water_buckets=8
    assert grid_to_string(parse(config_1),player)=='*X*************\n*A      1 *   *\n* ***W** **** *\n* 1********F  *\n*************Y*\n\nYou have 8 water buckets.\n','Failed water buckets grid: wrong string returned'


def test_grid_player_not_air():
    '''tests behaviour of grid_to_function when player postion is on a non whitespace display cell.'A' should replace pre-existing display'''   
    player=Player.__new__(Player)
    player.display='A'
    player.col=5
    player.row=2
    player.num_water_buckets=0
    assert grid_to_string(parse(config_1),player)=='*X*************\n*       1 *   *\n* ***A** **** *\n* 1********F  *\n*************Y*\n\nYou have 0 water buckets.\n','Failed non Air grid: wrong string returned'


def test_grid_non_rectangular():
    '''tests normal operation of grid_to_function. Should return single string which displays player position on board and water buckets'''
    player=Player.__new__(Player)
    player.display='A'
    player.col=1
    player.row=2
    player.num_water_buckets=2
    assert grid_to_string(parse(config_2),player)=='*X*\n* *\n*A****\n*    Y\n******\n\nYou have 2 water buckets.\n','Failed non rectangular grid: wrong string returned'


def run_tests():
    test_grid_normal()
    test_grid_player_not_air()
    test_grid_water_buckets()
    test_grid_non_rectangular()

