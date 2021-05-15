'''Tests run in this module use 'board_game_test.txt' as a configuration for board'''
from game import Game


def test_gameMove_normal():
    '''tests normal operation of gameMove. Should change player position and return game display'''
    game=Game('board_game_test.txt')
    assert game.gameMove('s')=='**X**\n**AW*\n**Y**\n\nYou have 0 water buckets.\n','Failed normal gameMove: incorrect display'
    assert game.player.col==2 ,'Failed normal gameMove: incorrect player col attribute'
    assert game.player.row==1 ,'Failed normal gameMove: incorrect player row attribute'


def test_gameMove_catch_input_error():
    '''tests normal operation of gameMove. Should change player position and return game display including error message'''
    game=Game('board_game_test.txt')
    assert game.gameMove('@!#$')=='**A**\n** W*\n**Y**\n\nYou have 0 water buckets.\n\nPlease enter a valid move (w, a, s, d, e, q).\n','Failed error gameMove: incorrect display'
    assert game.player.col==2,'Failed error gameMove: player col attribute should not change'
    assert game.player.row==0,'Failed error gameMove: player row attribute should not change'


def test_gameMove_bounds():
    '''tests normal operation of gameMove. Should change player position and return game display including walking into wall message'''
    game=Game('board_game_test.txt')
    assert game.gameMove('w')=='**A**\n** W*\n**Y**\n\nYou have 0 water buckets.\n\nYou walked into a wall. Oof!\n','Failed bounds gameMove: incorrect display'
    assert game.player.col==2, 'Failed bounds gameMove: player col attribute should not change'
    assert game.player.row==0, 'Failed bounds gameMove: player row attribute should not change'
    assert game.state!='win', 'Failed bounds gameMove: game state should be "win" '


def test_gameMove_quit():
    '''tests normal operation of gameMove. Should set game state to 'quit' and return nothing'''
    game=Game('board_game_test.txt')
    assert game.gameMove('q')==None, 'Failed quit gameMove: method should return None'
    assert game.state=='quit', 'Failed quit gameMove: game state should be "quit"'


def run_tests():
    test_gameMove_normal()
    test_gameMove_catch_input_error()
    test_gameMove_bounds()
    test_gameMove_quit()
    
run_tests()