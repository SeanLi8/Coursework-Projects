#include <stdio.h>
#include <string.h>
#define BYTE unsigned char
#define MAXSIZE 584 
#define MAXSTRING 5376 


int string_compare(const char* w1, const char* w2, unsigned int len); //compares 2 strings up to len chars
int reset_mappings(char* mappings); //resets a char array with known length of 32 to all value 0
int reorder_mappings(char* mappings, int value); //sets a specific symbol in the mappings array to first and increments all other defined symbols
BYTE read_n_bits(int n, BYTE* buffer, int* position, int first_byte); //reads bits in reverse order from a given buffer, given a bit position and starting point defined by first_byte
BYTE read_value(BYTE* buffer, int* position, int first_byte, BYTE type); //reads a 2 bit type field and then reads a corrsponding amount of bits for a value, returning the value
int opcode_string(char* buffer, int opcode); //inserts a string into a given position of a string buffer corresponding to an opcode
int value_string(char* buffer, int value_type, int value, int* symbol_count, char* mapping); //inserts a string into a given position of a string buffer corresponding to an value and uses symbol mappings if type is a stk symbol
int label_string(char* buffer, const int label); //inserts string into given position of a string buffer corresponding to a function label
