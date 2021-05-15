'''Tests run in this module use 'board_cells_test.txt' as a configuration for board.'''
from game import Game, Player,read_lines
from cells import (
    Start,
    End,
    Air,
    Wall,
    Fire,
    Water,
    Teleport
)

def test_wall_step():
    '''tests normal operation of Wall step method. Player postion should be set to what it was before the last move.'''
    game=Game.__new__(Game)
    game.player=Player.__new__(Player)
    game.player.col=1
    game.player.row=1
    game.temp_move='s'
    Wall().step(game)
    assert game.player.col==1 , 'Failed Wall step: player col attribute should not change'
    assert game.player.row==0 , 'Failed Wall step: player row attribute should not change'


def test_water_step():
    '''tests normal operation of Water step method. Player water buckets should increase, cell should change to Air, last move should be recorded.'''
    game=Game.__new__(Game)
    game.cells=read_lines('board_cells_test.txt')
    game.cells[1][1]=Water()
    game.moves=[]
    game.move_count=0
    game.temp_move='s'
    game.player=Player.__new__(Player)
    game.player.num_water_buckets=0
    game.player.col=1
    game.player.row=1
    Water().step(game)
    assert game.player.num_water_buckets==1 , 'Failed Water step: player num_water_buckets attribute should increase'
    assert type(game.cells[1][1])==Air , 'Failed Water step: Water cell should be replaced with Air cell'
    assert game.moves==['s'] , 'Failed Water step: Last move should be recorded'
    assert game.move_count==1  , 'Failed Water step: Last move should be recorded'


def test_fire_lose_step():
    '''tests normal operation of fire step method with no water buckets. Game state should set to 'lose', last move should be recorded.'''
    game=Game.__new__(Game)
    game.cells=read_lines('board_cells_test.txt')
    game.cells[1][1]=Fire()
    game.state='running'
    game.moves=[]
    game.move_count=0
    game.temp_move='s'
    game.player=Player.__new__(Player)
    game.player.num_water_buckets=0
    game.player.col=1
    game.player.row=1
    Fire().step(game)
    assert game.player.num_water_buckets==0 , 'Failed Fire lose step: player num_water_buckets should not change'
    assert type(game.cells[1][1])==Fire ,'Failed Fire lose step: cell should remain as Fire cell'
    assert game.moves==['s'] , 'Failed Fire lose step: Last move should be recorded'
    assert game.move_count==1  , 'Failed Fire  lose step: Last move should be recorded'
    assert game.state=='lose' , 'Failed Fire lose step: game state should be "lose"'


def test_fire_step():
    '''tests normal operation of Fire step method. Player water buckets should decrease, cell should change to Air, last move should be recorded.'''
    game=Game.__new__(Game)
    game.cells=read_lines('board_cells_test.txt')
    game.cells[1][1]=Fire()
    game.state='running'
    game.moves=[]
    game.move_count=0
    game.temp_move='s'
    game.player=Player.__new__(Player)
    game.player.num_water_buckets=1
    game.player.col=1
    game.player.row=1
    Fire().step(game)
    assert game.player.num_water_buckets==0  , 'Failed Fire step: player num_water_buckets should be decreased'
    assert type(game.cells[1][1])==Air  , 'Failed Fire step: Fire cell should be replaced with Air cell'
    assert game.moves==['s']  , 'Failed Fire step: Last move should be recorded'
    assert game.move_count==1  , 'Failed Fire step: Last move should be recorded'
    assert game.state=='running' , 'Failed Fire step: game state should be "running"'


def test_end_step():
    '''tests normal operation of End step method. Game state should be set to 'win', last move should be recorded.'''
    game=Game.__new__(Game)
    game.moves=[]
    game.state='running'
    game.move_count=0
    game.temp_move='s'
    End().step(game)
    assert game.state=='win', 'Failed End step: game state should be "win'
    assert game.move_count==1 , 'Failed End step: Last move should be recorded'
    assert game.moves==['s'] , 'Failed End step: Last move should be recorded'


def test_start_step():
    '''tests normal operation of Start step method. last move should be recorded.'''
    game=Game.__new__(Game)
    game.moves=[]
    game.move_count=0
    game.temp_move='s'
    Start().step(game)
    assert game.move_count==1 , 'Failed Start step: Last move should be recorded'
    assert game.moves==['s'] , 'Failed Start step: Last move should be recorded'


def test_teleport_step():
    '''tests normal operation of End step method. Player position should be set to corresponding teleporter, last move should be recorded.'''
    game=Game.__new__(Game)
    game.cells=read_lines('board_cells_test.txt')
    game.cells[1][1]=Teleport('8')
    game.cells[1][3]=Teleport('8')
    game.moves=[]
    game.move_count=0
    game.temp_move='a'
    game.player=Player.__new__(Player)
    game.player.col=1
    game.player.row=1
    game.cells[1][1].step(game)
    assert game.player.col==3 , 'Failed Teleport step: incorrect player col attribute'
    assert game.player.row==1 , 'Failed Teleport step: incorrect player row attribute'
    assert game.move_count==1 , 'Failed Teleport step: Last move should be recorded'
    assert game.moves==['a'] , 'Failed Teleport step: Last move should be recorded'


def run_tests():
 test_wall_step()
 test_water_step()
 test_teleport_step()
 test_fire_lose_step()
 test_fire_step()
 test_end_step()
 test_start_step()
