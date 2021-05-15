import sys

def determine_bits(line):
    temp=""
    if "RET" in line:
        temp="010"
    elif "MOV" in line: 
        temp="000"+temp
        args=line.split(" ")
        if args[1]=="REG":
            temp="01"+temp
            temp=str(format(int(args[2]),"03b"))+temp
        elif args[1]=="STK":
            temp="10"+temp
            temp=stack_symbol(args[2])+temp
        elif args[1]=="PTR":
            temp="11"+temp
            temp=stack_symbol(args[2])+temp
        
        if args[3]=="REG":
            temp="01"+temp
            temp=str(format(int(args[4]),"03b"))+temp
        elif args[3]=="STK":
            temp="10"+temp
            temp=stack_symbol(args[4])+temp
        elif args[3]=="PTR":
            temp="11"+temp
            temp=stack_symbol(args[4])+temp
        else:
            temp="00"+temp
            temp=str(format(int(args[4]),"08b"))+temp

    elif "ADD" in line:
        temp="100"+temp
        args=line.split(" ")
        temp="01"+temp
        temp=str(format(int(args[2]),"03b"))+temp
        temp="01"+temp
        temp=str(format(int(args[4]),"03b"))+temp   
    elif "FUNC" in line:
        args=line.split(" ")
        temp=str(format(int(args[-1]),"03b"))+temp
    elif "CAL" in line:
        temp="001"+temp
        args=line.split(" ")
        temp="00"+temp
        temp=str(format(int(args[2]),"08b"))+temp
    elif "REF" in line:
        temp="011"+temp
        args=line.split(" ")
        if args[1]=="REG":
            temp="01"+temp
            temp=str(format(int(args[2]),"03b"))+temp
        elif args[1]=="PTR":
            temp="11"+ temp
            temp=stack_symbol(args[2])+temp
        else:
            temp="10"+ temp
            temp=stack_symbol(args[2])+temp
        
        if args[3]=="PTR":
            temp="11"+ temp
            temp=stack_symbol(args[4])+temp
        elif args[3]=="STK":
            temp="10"+ temp
            temp=stack_symbol(args[4])+temp
        
    elif "PRINT" in line:
        temp="101"+temp
        args=line.split(" ")
        if args[1]=="REG":
            temp="01"+temp
            temp=str(format(int(args[2]),"03b"))+temp
        elif args[1]=="VAL":
            temp="00"+ temp
            temp=str(format(int(args[2]),"08b"))+temp
        elif args[1]=="STK":
            temp="10"+ temp
            temp=stack_symbol(args[2])+temp
        elif args[1]=="PTR":
            temp="11"+ temp
            temp=stack_symbol(args[2])+temp
    elif "NOT" in line:
        temp="110"+temp
        args=line.split(" ")
        temp="01"+temp
        temp=str(format(int(args[2]),"03b"))+temp
    elif "EQU" in line:
        temp="111"+temp
        args=line.split(" ")
        temp="01"+temp
        temp=str(format(int(args[2]),"03b"))+temp

    return temp


def stack_symbol(symbol):
    ascii=ord(symbol)
    if ascii>=65 and ascii<=90:
        return str(format((ascii-65),"05b"))
    elif ascii>=97 and ascii<=122:
        return str(format((ascii-97+26),"05b"))

asm=open(sys.argv[1],"r")
lines=asm.readlines()
lines.reverse()
asm.close()
temp=[]
functions=[]
i=0
while(i<len(lines)):
    temp.append(lines[i].strip())
    if (i==len(lines)-1 or "FUNC" in lines[i]):
        if (len(temp)!=0):
            functions.insert(0,temp)
            temp=[]
    i+=1


functions.reverse()


bitString=""

for function in functions:
    bitString=str(format(len(function)-1,"05b"))+bitString
    for line in function:
        bitString=determine_bits(line)+bitString

while len(bitString)%8!=0:
    bitString="0"+bitString



i = 0
buffer = bytearray()
while i < len(bitString):
    buffer.append( int(bitString[i:i+8], 2) )
    i += 8

with open(sys.argv[1][0:-4]+".x2017", 'bw') as f:
    f.write(buffer)