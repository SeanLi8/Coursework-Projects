#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
enum status{
    FREE = 0,
    ALLOCATED =1
};

enum buddy_side{
    LEFT = 0,
    RIGHT = 1
};

struct block{
        uint8_t size:7; //the power of 2 which the block is sized
        uint8_t status:1; //0 is free and 1 is allocated
};

struct allocator{
    uint8_t size; //the power of 2 which heap memory is sized
    uint8_t min_size; //the power of 2 which is the minimum sized block
};

uint64_t pow_2(uint8_t power); //raises 2 to the power of power

void* malloc_at(void * heapstart, uint8_t size, void * ptr); //makes memory allocation at specific spot in memory

struct block* smallest_valid_finder(void * heapstart, uint64_t adjusted_size); //finds smallest block to be split into adjusted size

struct block* split_to_size(struct block * target_block, uint8_t target_size); //splits a block until it is target size

void init_allocator(void * heapstart, uint8_t initial_size, uint8_t min_size); 

void * virtual_malloc(void * heapstart, uint32_t size);

int virtual_free(void * heapstart, void * ptr);

void * virtual_realloc(void * heapstart, void * ptr, uint32_t size);

void virtual_info(void * heapstart);
