from cells import (
    Start,
    End,
    Air,
    Wall,
    Fire,
    Water,
    Teleport
)
import sys

def read_lines(filename):
    """Read in a file, process them using parse(),
    and return the contents as a list of list of cells."""
    try:
        f=open(filename,'r')
    except FileNotFoundError:
        print("{} does not exist!".format(filename))
        sys.exit()

    lines=f.readlines()
    return parse(lines)
    

def parse(lines):
    """Transform the input into a grid.

    Arguments:
        lines -- list of strings representing the grid

    Returns:
        list -- contains list of lists of Cells
    """
    output=[]
    good_letters=['A',' ','X','Y','*','1','2','3','4','5','6','7','8','9','W','F','\n']
    y_count=0
    x_count=0
    pad_counts={}
    
    for row in lines:
        temp=[]
        i=0
        
        while i<len(row):
            if row[i] not in good_letters:
                raise ValueError('Bad letter in configuration file: {}.'.format(row[i]))
            if row[i]=='X':
                temp.append(Start())
                x_count+=1
            if row[i]=='Y':
                temp.append(End())
                y_count+=1
            if row[i] in ['1','2','3','4','5','6','7','8','9']:
                if pad_counts.get(row[i])==None:
                    pad_counts[row[i]]=1
                else:
                    pad_counts[row[i]]+=1
                temp.append(Teleport(row[i]))
            if row[i]==' ':
                temp.append(Air())
            if row[i]=='*':
                temp.append(Wall())
            if row[i]=='W':
                temp.append(Water())
            if row[i]=='F':
                temp.append(Fire())
            if row[i]=='\n':
                i+=1
                continue
            i+=1
        
        output.append(temp)              
   
    if x_count!=1:
        raise ValueError('Expected 1 starting position, got {}.'.format(x_count))
    if y_count!=1:
        raise ValueError('Expected 1 ending position, got {}.'.format(y_count))
    for x in pad_counts.items():
        if x[1]!=2:
            raise ValueError('Teleport pad {} does not have an exclusively matching pad.'.format(x[0]))
    
    return output
