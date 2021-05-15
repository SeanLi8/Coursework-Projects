from player import Player

def test_player_move():
    '''tests normal move operation. Should change player row attribute'''
    player=Player.__new__(Player)
    player.col=1
    player.row=1
    player.move('s')
    assert player.row==2, 'Failed normal move: expected player row attribute to be 2'


def test_player_move_invalid_1():
    '''tests behaviour of move when integer is passed as argument.Should raise an error'''
    player=Player.__new__(Player)
    player.col=1
    player.row=1
    try:
        player.move(34)
        raise AssertionError("Failed invalid move 1: no error was raised")
    except Exception as e:
        assert str(e)=="Please enter a valid move (w, a, s, d, e, q).\n",'Failed invalid move 1: wrong error was raised'


def test_player_move_invalid_2():
    '''tests behaviour of move when invalid string is passed as argument.Should raise an error'''
    player=Player.__new__(Player)
    player.col=1
    player.row=1
    try:
        player.move('@!%$')
        raise AssertionError("Failed invalid move 2: no error was raised")
    except Exception as e:
        assert str(e)=="Please enter a valid move (w, a, s, d, e, q).\n",'Failed invalid move 2: wrong error was raised'
                

def run_tests():
    test_player_move()
    test_player_move_invalid_1()
    test_player_move_invalid_2()

    