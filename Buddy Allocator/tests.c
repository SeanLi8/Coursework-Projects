#include "virtual_alloc.h"
#include "virtual_sbrk.h"
#include <stdlib.h>
#include <assert.h>

void * heapstart = NULL;
void * mem_brk=NULL;

void * virtual_sbrk(int32_t increment) {
    void* old_break=mem_brk;
    mem_brk+=increment;
    return old_break;
}

int create_heap(int max_memory){
    heapstart=malloc(max_memory);
    mem_brk=heapstart;
    return 0;
}

int count_blocks(void * heapstart){
    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    int count=0;
    while ((void*)temp_block < mem_brk){
        temp_block++;
        count++;
    }
    return count;
}

int test_init_allocator(){
    create_heap(100000);
    init_allocator(heapstart,15,10);
    struct allocator* allocator=heapstart;
    struct block* starting_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    assert(allocator -> min_size == 10 && allocator -> size == 15 && "test if allocator struct was initialised properly");
    assert(count_blocks(heapstart)==1 && "test if there is a single block struct when init_allocator is called");
    assert(starting_block -> size == 15 && starting_block -> status == FREE && "test if starting block was initialised properly");
    assert((mem_brk - heapstart) == (2 + 1 + pow_2(15)) && "test that memory break is at correct offset from heapstart");
    
    free(heapstart);
    printf("passed\n");
    return 0;
}

int test_split_to_size(){
    create_heap(100000);
    init_allocator(heapstart,15,10);
    
    //splitting the starting block 
    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes[6] = {10, 10 ,11 , 12, 13, 14};
    split_to_size(temp_block, 10);
    assert(count_blocks(heapstart)==6 && "fucntion splits the correct number of blocks starting from 1 block");
    for (int i = 0; i < 5; i++){
        assert(temp_block -> size == expected_sizes[i] && "all sizes of splitted blocks are correct");
        assert(temp_block -> status == FREE && "all splitted blocks are free");
        temp_block++;
    }

    //targeting a specific block to split after some splitting has been done
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_2[7] = {10, 10 ,11 , 11, 11, 13, 14};
    split_to_size(temp_block + 3, 11);
    assert(count_blocks(heapstart)==7 && "fucntion splits the correct number of blocks when targeting a block that has been split");
    for (int i = 0; i < 6; i++){
        assert(temp_block -> size == expected_sizes_2[i] && "all sizes of blocks are correct when splitting a block in middle of heap");
        assert(temp_block -> status == FREE && "all splitted blocks are free when splitting a block in middle of heap");
        temp_block++;
    }

    //note that we do not have to check if block can be split since block is either found by smallest_valid_finder() or is a reallocation
    free(heapstart);
    printf("passed\n");
    return 0;
}

int test_smallest_valid_finder(){
    create_heap(100000);
    init_allocator(heapstart,16,10);

    //create heap memory with state 7 blocks with sized: 10, 10 ,11 , 11, 11, 13, 13, 13, 15 and all FREE status
    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    split_to_size(temp_block, 10);
    split_to_size(temp_block + 3, 11);
    split_to_size(temp_block + 6, 13);

    assert(smallest_valid_finder(heapstart, pow_2(15)) == temp_block + 8 && "function finds a valid block when there is only one valid");
    assert(smallest_valid_finder(heapstart, pow_2(13)) == temp_block + 5 && "function finds a the smallest and leftmost valid block when there is multiple valid");
    assert(smallest_valid_finder(heapstart, pow_2(11)) == temp_block + 2 && "function finds a the smallest and leftmost valid block when there is multiple valid");
    assert(smallest_valid_finder(heapstart, pow_2(16)) == NULL && "function returns null if no block is found");    
    
    free(heapstart);
    printf("passed\n");
    return 0;
}

int test_virtual_malloc(){
    create_heap(100000);
    init_allocator(heapstart,15,10);

    //create heap memory with state 1 blocks with sized: 15 and  FREE status
    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;

    //case where block exists
    assert(virtual_malloc(heapstart, pow_2(15)) == heapstart + 2 && "returns correct address when existing block is suitable and max heap size is requested");
    assert(count_blocks(heapstart)==1 && "no blocks are split when existing block is suitable and max heap size is requested");
    assert(temp_block -> status == ALLOCATED && "correctly allocates block when an existing block is suitable and max heap size is requested");
    
    //case where block exists but size needed to be adjusted
    temp_block -> status = FREE;
    assert(virtual_malloc(heapstart, pow_2(15)-888) == heapstart + 2 && "returns correct address when existing block is suitable and size is adjusted");
    assert(count_blocks(heapstart)==1 && "no blocks are split when existing block is suitable and size is adjusted");
    assert(temp_block -> status == ALLOCATED && "correctly allocates block when an existing block is suitable and size is adjusted");

    //case where requested size is too big
    temp_block -> status = FREE;
    assert(virtual_malloc(heapstart, pow_2(16)) == NULL && "returns null if allocation cannot be made, size was too big");
    assert(count_blocks(heapstart)==1 && "no blocks are split when requested size is too big");
    assert(temp_block -> status == FREE && "block does not get allocated when size is too big");

    //case where requested size is less than or equal to 0
    assert(virtual_malloc(heapstart, 0) == NULL && "returns null if allocation cannot be made, size is less than or equal to 0");
    assert(count_blocks(heapstart)==1 && "no blocks are split when size is less than or equal to 0");
    assert(temp_block -> status == FREE && "block does not get allocated when size is less than or equal to 0");

    //case where block needs to be split, and there is a valid block to split
    temp_block -> status = FREE;
    assert(virtual_malloc(heapstart, pow_2(14)) == heapstart +2 && "returns correct address when splitting was needed and size is power of 2");
    assert(count_blocks(heapstart)==2 && "blcoks are split once in first splitting test");
    assert(temp_block -> status == ALLOCATED && "correctly allocates block when splitting is needed and size is a power of 2");
    assert((temp_block + 1) -> status == FREE && "correctly allocates block when splitting is needed and size is a power of 2");

    //case when split needs to be made in middle of heap
    uint8_t expected_sizes[5] = {14, 11, 11, 12, 13};
    uint8_t expected_status[5] = {1, 1, 0, 0 ,0};
    assert(virtual_malloc(heapstart, pow_2(11)) == heapstart + 2 + pow_2(14) && "returns correct address splitting is needed in middle of heap");
    for (int i = 0; i < 5; i++){
        assert(temp_block -> status == expected_status[i] && "all status correct after allocation where splitting in middle of heap was needed");
        assert(temp_block -> size == expected_sizes[i] && "all sizes correct after allocation where splitting in middle of heap was needed");
        temp_block++;
    }

    //case where a suitable block exists and it is some inner right buddy block
    //state of heap is 5 block with sized: 14, 11, 11, 12 ,13 with status: 1, 1, 0, 1, 0
    expected_status[3] = 1;
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    assert(virtual_malloc(heapstart, pow_2(12)) == heapstart + 2 + pow_2(14) + pow_2(11) + pow_2(11) && "returns correct address when suitable block exists as some inner right buddy block");
    assert(count_blocks(heapstart)==5 && "blocks are not split when suitable block exists as some inner right buddy block");
    for (int i = 0; i < 5; i++){
        assert(temp_block -> status == expected_status[i] && "all status correct after allocation when suitable block exists as some inner right buddy block");
        assert(temp_block -> size == expected_sizes[i] && "all sizes correct after allocation when suitable block exists as some inner right buddy block");
        temp_block++;
    }

    //case where split is needed between allocated blocks
    //state of heap is 6 block with sized: 14, 11, 10, 10, 12 ,13 with status: 1, 1, 1, 0, 1, 0
    uint8_t expected_sizes_2[6] = {14, 11, 10, 10, 12, 13};
    uint8_t expected_status_2[6] = {1, 1, 1, 0, 1 ,0};
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    assert(virtual_malloc(heapstart, pow_2(10)) == heapstart + 2 + pow_2(14) + pow_2(11)  && "returns correct address when split is needed between allocated blocks");
    assert(count_blocks(heapstart)==6 && "blocks are split when split is needed between allocated blocks");
    for (int i = 0; i < 6; i++){
        //printf("size: %u, status %u\n", temp_block->size, temp_block->status);
        assert(temp_block -> status == expected_status_2[i] && "all status correct after allocation when split is needed between allocated blocks");
        assert(temp_block -> size == expected_sizes_2[i] && "all sizes correct after allocation when split is needed between allocated blocks");
        temp_block++;
    }

    //case where size is not too big, but all blocks are allocated
    //state of heap is 6 block with sized: 14, 11, 10, 10, 12 ,13 with status: 1, 1, 1, 1, 1, 1
    expected_status_2[5] = 1;
    expected_status_2[3] = 1;
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    virtual_malloc(heapstart, pow_2(13));
    virtual_malloc(heapstart, pow_2(10));
    assert(virtual_malloc(heapstart, pow_2(10)) == NULL  && "returns NULL when all blocks are allocated");
    assert(count_blocks(heapstart)==6 && "blocks are not split when all blocks are allocated");
    for (int i = 0; i < 6; i++){
        //printf("size: %u, status %u\n", temp_block->size, temp_block->status);
        assert(temp_block -> status == expected_status_2[i] && "all status correct after allocation when all blocks are allocated");
        assert(temp_block -> size == expected_sizes_2[i] && "all sizes correct after allocation when all blocks are allocated");
        temp_block++;
    }

    free(heapstart);
    printf("passed\n");
    return 0;
}

int test_virtual_free(){
    create_heap(100000);
    init_allocator(heapstart,15,10);

    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    
    //case where first block is allocated and should coalesce until only original start block remains
    //blocks have sizes 10, 10 , 11, 12, 13, 14 with only first one free
    virtual_malloc(heapstart, 10);
    assert(count_blocks(heapstart) == 6 && "blocks where properly split before coalescing");
    assert(temp_block -> status == ALLOCATED && "block was properly allocated before freeing");
    virtual_free(heapstart, heapstart +2);
    assert(count_blocks(heapstart) == 1 && "blocks where properly coalesced when only first block is allocated");
    assert(temp_block -> status == FREE && "block was properly freed when only first blockn is allocated");

    //case where 1st and some middle block are allocated, only first is freed
    //same state as last case but block size 13 is allocated
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_1[3] = {13, 13, 14};
    uint8_t expected_status_1[3] = {0, 1, 0};
    virtual_malloc(heapstart, pow_2(10));
    virtual_malloc(heapstart, pow_2(13));
    assert(count_blocks(heapstart) == 6 && "blocks where properly split before coalescing 1st and some middle block are allocated, only first is freed");
    virtual_free(heapstart, heapstart + 2);
    assert(count_blocks(heapstart) == 3 && "blocks coalesce upto the allocated block");
    for (int i = 0; i < 3; i++){
        assert(temp_block -> status == expected_status_1[i] && "all status correct after allocation when first 2 blocks are allocated but only 1 is freed");
        assert(temp_block -> size == expected_sizes_1[i] && "all sizes correct after allocation when first 2 blocks are allocated but only 1 is freed");
        temp_block++;
    }

    //case where a memory address is not a block
    //current state is 3 blocks size: 13, 13, 14 , only middle allocated
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    assert(virtual_free(heapstart, heapstart +'b' + 'r' + 'u' + 'h') == 1 && "free returns 1 if memory address is not a block"); 
    assert(count_blocks(heapstart) == 3 && "blocks do not coalesce since memory address is not a block");
    for (int i = 0; i < 3; i++){
        assert(temp_block -> status == expected_status_1[i] && "all status correct after allocation when memory address is not a block");
        assert(temp_block -> size == expected_sizes_1[i] && "all sizes correct after allocation when memory address is not a block");
        temp_block++;
    }

    //case where a single allocated block is freed between free blocks
    //current state is 3 blocks size: 13, 13, 14 , only middle allocated
    virtual_free(heapstart, heapstart + 2 + pow_2(13));
    assert(count_blocks(heapstart) == 1 && "blocks coalesce when a single allocated block is freed between free blocks");
    assert(temp_block -> status == FREE && "block was properly freed when a single allocated block is freed between free blocks");

    //case where fiorst 2 blocks are allocated but only 1 is freed
    //same state as last case but first 2 blocks are allocated
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_2[6] = {10, 10, 11, 12, 13, 14};
    uint8_t expected_status_2[6] = {1, 0, 0, 0 ,0};
    virtual_malloc(heapstart, 10);
    virtual_malloc(heapstart, 10);
    assert(count_blocks(heapstart) == 6 && "blocks where properly split before coalescing when first 2 blocks are allocated");
    assert(temp_block -> status == ALLOCATED && (temp_block + 1) -> status == ALLOCATED && "first 2 blocks properly allocated before freeing");
    virtual_free(heapstart, heapstart +2 + pow_2(10));
    assert(count_blocks(heapstart) == 6 && "blocks did not coalesce since buddy block was not free");
    assert(temp_block -> status == ALLOCATED && (temp_block + 1) -> status == FREE && "targetted block was freed");
    for (int i = 0; i < 6; i++){
        assert(temp_block -> status == expected_status_2[i] && "all status correct after allocation when first 2 blocks are allocated but only 1 is freed");
        assert(temp_block -> size == expected_sizes_2[i] && "all sizes correct after allocation when first 2 blocks are allocated but only 1 is freed");
        temp_block++;
    }

    //case where first 2 blocks are allocated, then left buddy is freed, then right buddy is freed
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    virtual_free(heapstart, heapstart + 2);
    assert(count_blocks(heapstart) == 1 && "blocks did coalesce when right then left buddy where freed");
    assert(temp_block -> status == FREE && "targetted block was freed");

    //case where same size blocks are freed next to eachother but not buddys
    //state before freeing is blocks of size: 10, 10, 10, 10, 12, 13, 14 with first 4 allocated
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_3[7] = {10, 10, 10, 10, 12, 13, 14};
    uint8_t expected_status_3[7] = {1, 0, 0, 1 ,0 ,0, 0};
    virtual_malloc(heapstart, 10);
    virtual_malloc(heapstart, 10);
    virtual_malloc(heapstart, 10);
    virtual_malloc(heapstart, 10);
    virtual_free(heapstart, heapstart +2 + pow_2(10));
    virtual_free(heapstart, heapstart +2 + pow_2(10) + pow_2(10));
    assert(count_blocks(heapstart) == 7 && "blocks did not coalesce since buddy block was not free");
    assert(temp_block -> status == ALLOCATED && (temp_block + 1) -> status == FREE && "targetted block was freed");
    for (int i = 0; i < 7; i++){
        assert(temp_block -> status == expected_status_3[i] && "all status correct after allocation where same size blocks are freed next to eachother but not buddys");
        assert(temp_block -> size == expected_sizes_3[i] && "all sizes correct after allocation where same size blocks are freed next to eachother but not buddys");
        temp_block++;
    }

    free(heapstart);
    printf("passed\n");
    return 0;
}

int test_malloc_at(){
    create_heap(100000);
    init_allocator(heapstart,15,10);

    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    
    //case where allocating at first block
    malloc_at(heapstart, 14, heapstart + 2);
    assert(count_blocks(heapstart) == 2 && "blocks were properly split when allocating at first block");
    assert(temp_block -> status == ALLOCATED && (temp_block + 1) -> status == FREE && "block was properly allocating at first block");
    
    //case where block is allocated as right buddy when starting block is split in half
    //state of heap is 2 blocks of size 14 with second one being allocated
    virtual_free(heapstart, heapstart +2);
    malloc_at(heapstart, 14, heapstart + 2 + pow_2(14));
    assert(count_blocks(heapstart) == 2 && "blocks were properly split when block is allocated as right buddy when starting block is split in half");
    assert(temp_block -> status == FREE && (temp_block + 1) -> status == ALLOCATED && "block was properly allocated  as right buddy when starting block is split in half");

    //case where multiple splits need to happen to place allocation
    virtual_free(heapstart, heapstart + 2 + pow_2(14));
    uint8_t expected_sizes_1[6] = {14, 11, 10, 10, 12, 13};
    uint8_t expected_status_1[6] = {0, 0, 0,1 ,0, 0};
    malloc_at(heapstart, 10, heapstart + 2 + pow_2(14) + pow_2(11) + pow_2(10));
    assert(count_blocks(heapstart) == 6 && " block split properly when multiple splits need to happen to place allocation");
    assert((temp_block + 3) -> status == ALLOCATED &&  "targetted block was allocated when multiple splits need to happen to place allocation");
    for (int i = 0; i < 6; i++){
        assert(temp_block -> status == expected_status_1[i] && "all status correct after allocation where multiple splits need to happen to place allocation");
        assert(temp_block -> size == expected_sizes_1[i] && "all sizes correct after allocation where multiple splits need to happen to place allocation");
        temp_block++;
    }

    free(heapstart);
    printf("passed\n");
    return 0;
}

int test_virtual_realloc(){
    create_heap(100000);
    init_allocator(heapstart,15,10);

    struct block* temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;

    //case where the size is too big
    uint8_t expected_sizes_1[4] = {12, 12, 13, 14};
    uint8_t expected_status_1[4] = {1, 0, 0, 0 };
    virtual_malloc(heapstart, pow_2(12));
    assert(virtual_realloc(heapstart, heapstart + 2, pow_2(16)) == NULL && "function returns null if requested size is too large");
    for (int i = 0; i < 4; i++){
        assert(temp_block -> status == expected_status_1[i] && "all status correct when realloc fails");
        assert(temp_block -> size == expected_sizes_1[i] && "all sizes correct when realloc fails");
        temp_block++;
    }

    //case where ptr is null
    expected_status_1[1]=1;
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    assert(virtual_realloc(heapstart, NULL, pow_2(12)) == heapstart + 2 + pow_2(12) && "function return malloc return address if ptr is null");
    assert(count_blocks(heapstart) == 4 && " block split properly when realloc with ptr is null");
    for (int i = 0; i < 4; i++){
        assert(temp_block -> status == expected_status_1[i] && "all status correct when ptr is null");
        assert(temp_block -> size == expected_sizes_1[i] && "all sizes correct when ptr is null");
        temp_block++;
    }

    //case where size is 0 and ptr block can be freed
    expected_status_1[1]=0;
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    assert(virtual_realloc(heapstart, heapstart + 2 + pow_2(12), 0) == NULL && "function return if size is 0");
    assert(count_blocks(heapstart) == 4 && " block split properly when realloc with size is 0");
    for (int i = 0; i < 4; i++){
        assert(temp_block -> status == expected_status_1[i] && "all status correct when size is 0");
        assert(temp_block -> size == expected_sizes_1[i] && "all sizes correct when size is 0");
        temp_block++;
    }

    //case where the realloc fails and block is inserted in original location in middle of heap
    //state before realloc is 4 blocks with size: 12, 12, 13, 14 only second one allocated
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_2[4] = {12, 12, 13, 14};
    uint8_t expected_status_2[4] = {0, 1, 0, 0 };
    virtual_malloc(heapstart, pow_2(12));
    virtual_free(heapstart, heapstart +2);
    assert(virtual_realloc(heapstart, heapstart + 2, pow_2(16)) == NULL && "function returns null if requested size is too large");
    for (int i = 0; i < 4; i++){
        assert(temp_block -> status == expected_status_2[i] && "all status correct when realloc fails and block is inserted in original location in middle of heap");
        assert(temp_block -> size == expected_sizes_2[i] && "all sizes correct when realloc fails and block is inserted in original location in middle of heap");
        temp_block++;
    }
    //case where reallocation location was not found
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_3[4] = {12, 12, 13, 14};
    uint8_t expected_status_3[4] = {1, 1, 1, 1 };
    virtual_malloc(heapstart, pow_2(12));
    virtual_malloc(heapstart, pow_2(13));
    virtual_malloc(heapstart, pow_2(14));
    assert(virtual_realloc(heapstart, heapstart + 2, pow_2(13)) == NULL && "function returns null no suitable block is found");
    for (int i = 0; i < 4; i++){
        assert(temp_block -> status == expected_status_3[i] && "all status correct when reallocation location was not found");
        assert(temp_block -> size == expected_sizes_3[i] && "all sizes correct when reallocation location was not found");
        temp_block++;
    }

    free(heapstart);
    create_heap(100000);
    init_allocator(heapstart,15,10);
    
    //case where first block is allocated, bigger size requested
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_4[4] = {12, 12, 13, 14};
    uint8_t expected_status_4[4] = {0, 1, 1, 0 };
    virtual_malloc(heapstart, pow_2(12));
    virtual_malloc(heapstart, pow_2(12));
    //set some bytes in first block
    *(char*)(heapstart + 2 + 5) = 'b';
    *(char*)(heapstart + 2 + 6) = 'r';
    *(char*)(heapstart + 2 + 7) = 'u';
    *(char*)(heapstart + 2 + 8) = 'h';
    assert(virtual_realloc(heapstart, heapstart + 2, pow_2(13)) == heapstart + 2 + pow_2(13) && "function returns expected address when first block is allocated, bigger size requested");
    for (int i = 0; i < 4; i++){
        assert(temp_block -> status == expected_status_4[i] && "all status correct when first block is allocated, bigger size requested");
        assert(temp_block -> size == expected_sizes_4[i] && "all sizes correct when first block is allocated, bigger size requested");
        temp_block++;
    }
    assert(memcmp(heapstart + 2, heapstart + 2 + pow_2(13), pow_2(12)) == 0 && "all bits copied over after reallocations");

    //case where reallocation is only possible after coalesce
    //state before realloc is 4 blocks with size: 12, 12 , 13, 14 with first allocated only
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    virtual_free(heapstart, heapstart + 2 + pow_2(12));
    virtual_free(heapstart, heapstart + 2 + pow_2(13));
    uint8_t expected_sizes_5[3] = {13, 13, 14};
    uint8_t expected_status_5[3] = {1, 0, 0};
    virtual_malloc(heapstart, pow_2(12));
    assert(virtual_realloc(heapstart, heapstart + 2, pow_2(13)) == heapstart + 2  && "function returns expected address when reallocation is only possible after coalesce");
    for (int i = 0; i < 3; i++){
        assert(temp_block -> status == expected_status_5[i] && "all status correct when reallocation is only possible after coalesce");
        assert(temp_block -> size == expected_sizes_5[i] && "all sizes correct when reallocation is only possible after coalesce");
        temp_block++;
    }
    //dont need to test if values copied over since its the same place in memory, only block structs have changed


    //case where allocation is made smaller size
    temp_block = heapstart + pow_2(((struct allocator*) heapstart) -> size) + 2;
    uint8_t expected_sizes_6[6] = {10, 10, 11, 12, 13, 14};
    uint8_t expected_status_6[6] = {1, 0, 0, 0, 0, 0};
    assert(virtual_realloc(heapstart, heapstart + 2, pow_2(10)) == heapstart + 2  && "function returns expected address when allocation is made smaller size");
    for (int i = 0; i < 6; i++){
        assert(temp_block -> status == expected_status_6[i] && "all status correct when allocation is made smaller size");
        assert(temp_block -> size == expected_sizes_6[i] && "all sizes correct when allocation is made smaller size");
        temp_block++;
    }
    //dont need to test if values copied over since position did not change

    free(heapstart);
    printf("passed\n");
    return 0;

}
int test_virtual_info(){
    FILE* fp=freopen("test_virtual_info.txt", "w", stdout);
    create_heap(100000);
    init_allocator(heapstart,15,10);

    //case where there is a single start block
    virtual_info(heapstart);
    printf("\n"); 

    //case where memory has been assigned and blocks has been split
    virtual_malloc(heapstart, pow_2(11));
    virtual_info(heapstart);
    printf("\n");
    
    //case where multiple parts of memory have been allocated
    virtual_malloc(heapstart, pow_2(11));
    virtual_malloc(heapstart, pow_2(12));
    virtual_malloc(heapstart, pow_2(13));
    virtual_info(heapstart);
    fclose(fp);
    free(heapstart);
    return 0;
}

int main() {
    printf("Running init_allocator test: ");
    test_init_allocator();
    printf("Running split_to_size test: ");
    test_split_to_size();
    printf("Running smallest_valid_finder test: ");
    test_smallest_valid_finder();
    printf("Running virtual_malloc test: ");
    test_virtual_malloc();
    printf("Running virtual_free test: ");
    test_virtual_free();
    printf("Running malloc_at test: ");
    test_malloc_at();
    printf("Running virtual_realloc test: ");
    test_virtual_realloc();
    printf("Running virtual_info test: ");
    printf("(If there is no unix diff message this has passed)\n");
    test_virtual_info();

    
    return 0;
}
