#include "virtual_alloc.h"
#include "virtual_sbrk.h"
uint64_t pow_2(uint8_t power){
    return (1 << power);
}


struct block* smallest_valid_finder(void * heapstart, uint64_t adjusted_size){
    /*
     * Function:  smallest_valid_finder 
     * --------------------
     * finds the left most block which can be split such that it's size is a targeted size
     * this block will also be the smallest such block currently existing in the heap.
     *
     *  adjusted_size: size of block that is being created
     *
     *  returns: the address of the appropriate block struct, 
     *           if none are found then NULL is returned
     */
    struct allocator* allocator = heapstart;
    uint64_t temp_size = adjusted_size;
    struct block* target_block = NULL;

    //starts looking for a size block which is the smallest possible block that could be split to adjusted size,
    //each iteration the size increases until the size is larges than the entire heap memory
    while (temp_size <= pow_2(allocator -> size)){
        struct block* temp_block = (struct block*)(heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size)); //first block struct
        
        //look through each block struct to find a free and suitable size block
        while ((void*)temp_block < virtual_sbrk(0)){
            if (pow_2(temp_block -> size) == temp_size && temp_block -> status == FREE){
                target_block = temp_block;
                return target_block;
            }
            temp_block += 1;
        }
        temp_size=temp_size << 1;
    }   
    return NULL;
}


struct block* split_to_size(struct block * target_block, uint8_t target_size){
    /*
     * Function:  split_to_size 
     * --------------------
     * recursively splits a block until it is the target size
     *  
     *  target_block: the block being split
     *  target_size: target block size
     *
     *  returns: the address of left buddy block struct which got split
     *           
     */
    
    if (target_block -> size == target_size){
        return target_block;
    }
    else{
        virtual_sbrk(1); //since a block is inserted increase memory break
        target_block -> size -= 1;
        memmove(target_block + 2, target_block + 1, virtual_sbrk(0) - (void*)(target_block + 2)); //shift original blocks to the right to make space for new block
        struct block right_block={target_block->size, FREE}; //create the right buddy of the split block
        *(struct block*)((uint8_t*)target_block + 1) = right_block; //assignt it next to it's buddy block
    }
    return split_to_size(target_block, target_size);
}


void init_allocator(void * heapstart, uint8_t initial_size, uint8_t min_size) {
    virtual_sbrk(pow_2(initial_size) + 2 + 1); //creating space for the heap memory, allocator structure and first header structure
    struct allocator allocator = {initial_size, min_size}; 
    *(struct allocator*)heapstart= allocator; //place allocator before heap memory 
    struct block starting_block = {initial_size, FREE};
    *(struct block*)(heapstart + 2 + pow_2(initial_size)) = starting_block; //place first block struct after heap memory

}


void* malloc_at(void * heapstart, uint8_t size, void * ptr){
    /*
     * Function:  malloc_at 
     * --------------------
     * works like virtual malloc but creates block such that it starts at a certain heap memory address and has a certain size
     *  
     *  size: targeted size of allocated block, the power of 2
     *  ptr: targeted heap memory address
     *
     *  returns: the address in heap memory of the allocated block,
     *           if the initial address is invalid then returns NULL
     */
    void* cursor = heapstart + 2; //start of heap memory
    struct block* temp_block = (struct block*)(heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size)); //first block struct
    
    if (ptr > (void*)temp_block || ptr < heapstart + 2){
        return NULL;
    }

    //given the ptr is a power of 2 offset from the start of heap, loop will find first block which contains it.
    //when found loop will continue to execute the if statement which splits the block until the targeted block is reached
    while ((void*)temp_block < virtual_sbrk(0)){

        //if the targeted pointer is in the curent block
        if (cursor + pow_2(temp_block -> size) > ptr){
            //reached the original memory block with original size
            if (cursor == ptr && temp_block -> size == size){
                break;
            }
            split_to_size(temp_block, temp_block -> size - 1);//split the block
            //if the target is in the right splitted block update cursor and block struct, else just repeat on curent block
            if (cursor + pow_2(temp_block -> size) <= ptr){
                cursor += pow_2(temp_block -> size);
                temp_block += 1;
            }
            
            continue;
        }
        cursor += pow_2(temp_block -> size);
        temp_block+=1;
    }
    //set the targeted block to allocated
    temp_block -> status = 1;
    return temp_block;
}


void * virtual_malloc(void * heapstart, uint32_t size) {
    struct block* temp_block = (struct block*)(heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size)); //first block struct
    struct allocator* allocator = heapstart;
    void* ret_address = heapstart + 2; //running calculation of the memory address of an associated block struct
    
    //adjust size so that is is rounded up to closest power of 2 which is above a minimum amount
    uint64_t adjusted_size = size;
    uint8_t temp_power = allocator -> min_size;
    if(size == 0){
        return NULL;
    }
    else if (adjusted_size > pow_2(allocator -> size)){
        return NULL;
    }
    else if (adjusted_size <= pow_2(temp_power)){
        adjusted_size = pow_2(temp_power);
    }
    else{
        while (adjusted_size > pow_2(temp_power)){
            temp_power++;
        }
        adjusted_size = pow_2(temp_power);
    }
    
    //begin allocation algorithm

    //check if a suitable block already exists
    while ((void*)temp_block < virtual_sbrk(0)){
        if (pow_2(temp_block -> size) == adjusted_size && temp_block -> status == FREE){
            temp_block -> status = ALLOCATED;
            return ret_address;
        }
        ret_address += pow_2(temp_block -> size);
        temp_block += 1;
    }

    //since block was not found, find the block which will be split
    struct block* target_block = smallest_valid_finder(heapstart, adjusted_size);
    
    //if no block was found then return null
    if (target_block == NULL){
        return NULL;
    }

    //split the block and make it allocated
    split_to_size(target_block, temp_power);
    target_block -> status = ALLOCATED;

    //find the memory address for this block
    temp_block = (struct block*)(heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size));//first block struct
    ret_address = heapstart + 2;
    while (temp_block < target_block){
        ret_address += pow_2(temp_block -> size);
        temp_block += 1;
    }

    return ret_address;
}


int virtual_free(void * heapstart, void * ptr) {
    //find what block struct this memory address refers to
    void* cursor=heapstart + 2;
    struct block* temp_block = (struct block*)(heapstart +2 + pow_2(((struct allocator*)heapstart) -> size));
    struct block* target_block = NULL;
    
    if (ptr==NULL){
        return 1;
    }

    while ((void*)temp_block < virtual_sbrk(0)){
        if (cursor == ptr){
            target_block = temp_block;
            break;
        }
        cursor += pow_2(temp_block -> size);
        temp_block += 1;
    }

    //if the address is not an existing memory block return non-zero, otherwise free the block
    if (target_block == NULL){
        return 1;
    }
    else{
        target_block -> status = FREE;
    }
    
    //recursively coalesce the blocks 

    //decide which buddy side the block is
    //if it is a block of size n, and it's start address in the heap is a multiple of 2n, then it is left
    uint8_t buddy_side = 0;
    if ((ptr - (heapstart + 2)) % (pow_2(target_block -> size) << 1) == 0){
        buddy_side = FREE;
    }
    else{
        buddy_side = ALLOCATED;
    }

    //if target is a right buddy
    if (buddy_side == ALLOCATED){
        
        struct block* buddy_block=target_block - 1;
        if ((void*)buddy_block < (heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size))){
            return 1;
        }
        if (buddy_block -> size == target_block -> size && buddy_block -> status == FREE){
            buddy_block -> size += 1;
            memmove(buddy_block + 1, buddy_block + 2, virtual_sbrk(0) - (void*)(buddy_block + 2)); //shift blocks to the left, deleting buddy in process
            virtual_sbrk(-1); //since a block is deleted decrease memory break
            virtual_free(heapstart, ptr - pow_2(buddy_block -> size - 1));
        }

    }
    //if target is a left buddy
    else{
        struct block* buddy_block = target_block + 1;
        if ((void*)buddy_block > virtual_sbrk(0)){
            return 1;
        }
        if (buddy_block -> size == target_block -> size && buddy_block -> status == FREE){
            target_block -> size += 1; 
            memmove(target_block + 1, target_block + 2, virtual_sbrk(0) - (void*)(target_block + 2)); //shift blocks to the left, deleting buddy in process
            virtual_sbrk(-1); //since a block is deleted decrease memory break
            virtual_free(heapstart,ptr);
        }
    }
    return 0;
}

void * virtual_realloc(void * heapstart, void * ptr, uint32_t size) {
    //find what block struct the ptr corresponds with
    void* cursor = heapstart + 2;
    struct block* temp_block = (struct block*)(heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size));
    struct block* target_block = NULL;
    
    //return null if realloc size is too large
    if (size > pow_2(((struct allocator*)(heapstart)) -> size)){
        return NULL;
    }
    //behave like virtual_free(ptr) was called
    else if (size==0){
        virtual_free(heapstart,ptr);
        return NULL;
    }
    //behave like virtual_malloc(size) was called
    else if (ptr==NULL){
        return virtual_malloc(heapstart,size);
    }

    while ((void*)temp_block < virtual_sbrk(0)){
        if (cursor == ptr){
            target_block = temp_block;
            break;
        }
        cursor += pow_2(temp_block->  size);
        temp_block += 1;
    }

    //if it does not correspond with any block return NULL
    if (target_block == NULL){
        return NULL;
    }

    //reallocation algorithm 
    struct block original_block = *target_block;
    virtual_free(heapstart, ptr);
    void* new_allocation = virtual_malloc(heapstart, size);
    
    //if not sucessful use malloc_at to restore original block
    if (new_allocation == NULL){
        malloc_at(heapstart, original_block.size, ptr);
        //dont need to copy contents since memory allocation is back to original place
        return NULL;
    }
    //copy the contents over to new allocations if successful
    else{
        if (size > pow_2(original_block.size)){
            memmove(new_allocation, ptr, pow_2(original_block.size));
        }
        else{
            memmove(new_allocation, ptr, size);
        }
        return new_allocation;
    }
}

void virtual_info(void * heapstart) {
    struct block* temp_block = (struct block*)(heapstart + 2 + pow_2(((struct allocator*)heapstart) -> size));
    uint8_t min_size = ((struct allocator*)heapstart) -> min_size;
    //iterate through each block struct printing it's size and status
    while ((void*)temp_block < virtual_sbrk(0) && temp_block -> size >= min_size){
        if (temp_block -> status == ALLOCATED){
            printf("allocated ");
        }
        else{
            printf("free ");
        }
        printf("%lu\n",pow_2(temp_block -> size));
        temp_block += 1;  
    }
}
