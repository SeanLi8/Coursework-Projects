#include <stdio.h>
#define BYTE unsigned char
#define RAMSIZE 256
#define REGSIZE 8
#define MAXSIZE 450 
#define RUNNING 1

/* 
 * Stores parsed instructions intended for vm_x2017.c into bitfields which may contain padding in the param field
 */
struct instruction{
        unsigned int param :17;
        unsigned int opcode :3;
};

/* 
 * Possible operations represented by each opcode valued 0-7 in decimal
 */
enum opcodes{
    MOV,
    CAL,
    RET,
    REF,
    ADD,
    PRINT,
    NOT,
    EQU
};

/* 
 * Possible types of paramaters which may be in the instruction struct
 */
enum types{
    value=0,
    reg_address=1,
    stk_symbol=2,
    ptr=3
};

/* 
 * Special register adresses which serve the named purposes
 */
enum registers{
    FRM_PTR=5,
    STK_PTR=6,
    PC=7
};

/* 
 * Reads instruction and does an operation, possibly with paramaters. One of the following operations will be executed depending on the opcode field of the instruction.
 *
 * 000 - [MOV A B] - Copies the value at some point B in memory to another point A in memory
 *  (register or stack). The destination may not be value typed.
 * 001 - [CAL A] - Calls another function the first argument is a single byte (using the VALUE type)
 *  containing the label of the calling function.
 * 010 - [RET] - Terminates the current function, this is guaranteed to always exist at the end of each
 *  function. There may be more than one RET in a function. If this function is the entry-point,
 *  then the program terminates.
 * 011 - [REF A B] - Takes a stack symbol B and stores its corresponding stack address in A.
 * 100 - [ADD A B] - Takes two register addresses and ADDs their values, storing the result in the first
 *  listed register.
 * 101 - [PRINT A] - Takes any address type and prints the contents to a new line of standard output
 *  as an unsigned integer.
 * 110 - [NOT A] - Takes a register address and performs a bitwise not operation on the value at that
 *  address. The result is stored in the same register
 * 111 - [EQU A] - Takes a register address and tests if it equals zero. The value in the register will be
 *  set to 1 if it is 0, or 0 if it is not. The result is stored in the same register.
 */
int execute(struct instruction x,BYTE* ram,BYTE* reg);
/* 
 *Reads n bits from a buffer in reverse order given the position of which bit to start reading from in the buffer relative to a specific byte which is the 
 *first non garbage value byte in the buffer, the first_byte paramater.
 */
BYTE read_n_bits(int n, BYTE* buffer, int* position, int first_byte);
/* 
 *Reads n bits from an instuction struct in reverse order given the position of which bit to start reading from in the param bitfield 
 */
BYTE read_n_instruction_bits(int n, struct instruction x, int* position);
/* 
 *Reads a 2 bit bitfield to determine type of value to be read then reads a corresponding number of bits. The position tells the function which bit to start reading from in the buffer relative to a specific byte which is the 
 *first non garbage value byte in the buffer, the first_byte paramater.
 */
BYTE read_value(BYTE* buffer, int* position, int first_byte, BYTE type);
/* 
 *Reads a 2 bit bitfield to determine type of value to be read then reads a corresponding number of bits. The position tells the function which bit to start reading from in the instruction struct
 */
BYTE read_instruction_value(struct instruction x, int* position, BYTE type);