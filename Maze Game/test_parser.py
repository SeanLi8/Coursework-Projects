from game_parser import parse
import cells


def test_parse_normal():
    '''Test normal parse operation. Should return a list of list of cells (4x5) and should not have any strings.'''
    lines=['***X*','*88 *','*WF *','*Y***']
    grid=parse(lines)
    contains_string=False
    for row in grid:
        for col in row:
            if type(col)==str:
                contains_string=True
    assert (len(grid)==4 and len(grid[0])==len(grid[1])==len(grid[2])==5) , 'Failed normal parse: list of lists of cells parsed was not 4x5'
    assert not contains_string , "Failed normal parse: list of lists of cells contained a string"
    assert isinstance(grid[0][0],cells.Wall),'Failed normal parse: expected cell type Wall'
    assert isinstance(grid[1][1],cells.Teleport),'Failed normal parse: expected cell type Teleport'
    assert isinstance(grid[2][2],cells.Fire),'Failed normal parse: expected cell type Fire'
    assert isinstance(grid[2][1],cells.Water),'Failed normal parse: expected cell type Water'
    assert isinstance(grid[1][3],cells.Air),'Failed normal parse: expected cell type Air'
    assert isinstance(grid[0][3],cells.Start),'Failed normal parse: expected cell type Start'
    assert isinstance(grid[3][1],cells.End),'Failed normal parse: expected cell type End'

def test_parse_teleport():
    '''Tests the behaviour of parse when invalid teleport pads are used in configuration. Should raise an error'''
    lines=['***X*','*1  *','*Y***']
    try:
        parse(lines)
        raise AssertionError("Failed invalid teleporter parse: no error raised for invalid teleport pads")
    except Exception as e:
        assert str(e)=='Teleport pad 1 does not have an exclusively matching pad.' ,'Failed invalid teleporter parse: wrong error raised for invalid teleport pads'
    

def test_parse_bad_letter():
    '''Tests the behaviour of parse when invalid letters are used in configuration. Should raise an error'''
    lines=['***X*','*Z  *','*Y***']
    try:
        parse(lines)
        raise AssertionError("Failed invalid letter parse: no error raised for bad letter in configuration")
    except Exception as e:
        assert str(e)=='Bad letter in configuration file: Z.','Failed invalid letter parse: wrong error raised for bad letter in configuration'


def test_parse_start():
    '''Tests the behaviour of parse when invalid start position is used in configuration. Should raise an error'''
    lines=['*****','*   *','*Y***']
    try:
        parse(lines)
        raise AssertionError("Failed invalid start parse: no error raised for invalid start position")
    except Exception as e:
        assert str(e)=='Expected 1 starting position, got 0.','Failed invalid start parse: wrong error raised for invalid start position'


def test_parse_end():
    '''Tests the behaviour of parse when invalid ending position is used in configuration. Should raise an error'''
    lines=['*Y*X*','*   *','*Y***']
    try:
        parse(lines)
        raise AssertionError("Failed invalid end parse: no error raised for invalid ending position")
    except Exception as e:
        assert str(e)=='Expected 1 ending position, got 2.','Failed invalid end parse: wrong error raised for invalid ending position'


def test_parse_error_order():
    '''Tests the behaviour of parse when multiple invalid elements are used in configuration. Should raise an error'''
    lines=['*YXX*','*1 @*','*Y***']
    try:
        parse(lines)
        raise AssertionError("Failed multiple error parse: no error raised when multiple elements are")
    except Exception as e:
        assert str(e)=='Bad letter in configuration file: @.','Failed multiple error parse: wrong error raised whe multiple elements are invalid'


def run_tests():
    test_parse_normal()
    test_parse_teleport()
    test_parse_bad_letter()
    test_parse_end()
    test_parse_start()
    test_parse_error_order
