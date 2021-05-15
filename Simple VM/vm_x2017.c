#include "vm_x2017.h"

int reset_array(struct instruction* array, int size){
    for (int i=0; i<size; i++){
        array[i].opcode=0;
        array[i].param=0;

    }
}

BYTE read_n_bits(int n, BYTE* buffer, int* position, int first_byte){
    int i=0;
    BYTE result=0;
    while (i<n){
        result=result | ((buffer[first_byte - (*position)/8]>>(*position)%8) & 1)<<i;
        *position+=1;
        i++;
    }
    return result;
}

BYTE read_n_instruction_bits(int n, struct instruction x, int* position){
    int i=0;
    BYTE result=0;
    while (i<n){
        result=result | ((x.param>>(*position)%17) & 1)<<i;
        *position+=1;
        i++;
    }
    return result;
}

BYTE read_value(BYTE* buffer, int* position, int first_byte, BYTE type){
    switch (type){
        case 0:
         return read_n_bits(8, buffer, position, first_byte);   
        case 1:
         return read_n_bits(3, buffer, position, first_byte);   
        case 2:
         return read_n_bits(5, buffer, position, first_byte);   
        case 3:
         return read_n_bits(5, buffer, position, first_byte);       
    }
}

BYTE read_instruction_value(struct instruction x, int* position, BYTE type){
    switch (type){
        case 0:
         return read_n_instruction_bits(8, x, position);   
        case 1:
         return read_n_instruction_bits(3, x, position);   
        case 2:
         return read_n_instruction_bits(5, x, position);   
        case 3:
         return read_n_instruction_bits(5, x, position);       
    }
}

int execute(struct instruction x,BYTE* ram,BYTE* reg){
    BYTE temp_type_A=0;
    BYTE temp_type_B=0;
    BYTE temp_A=0;
    BYTE temp_B=0;
    BYTE value_B;
    int position=0;

    temp_type_A=read_n_instruction_bits(2, x, &position);
    temp_A=read_instruction_value(x, &position, temp_type_A);
    temp_type_B=read_n_instruction_bits(2, x, &position);
    temp_B=read_instruction_value(x, &position, temp_type_B);

    reg[PC]=reg[PC]+1;
    

    switch(x.opcode){
        case MOV:
            switch(temp_type_B){
                case value:
                    value_B=temp_B;
                    break;
                case reg_address:
                    value_B=reg[temp_B];
                    break;
                case stk_symbol:
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_B){
                        return 3;
                    }
                    value_B=ram[reg[FRM_PTR]+2+temp_B];
                    break;
                case ptr:
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_B){
                        return 3;
                    }
                    if (reg[STK_PTR]<ram[reg[FRM_PTR]+2+temp_B]){
                        return 4;
                    }
                    value_B=ram[ram[reg[FRM_PTR]+2+temp_B]];
                    break;
            }
            
            switch(temp_type_A){
                case reg_address:
                    reg[temp_A]=value_B;
                    break;
                case ptr:
                    if (reg[STK_PTR]<ram[reg[FRM_PTR]+2+temp_A]){
                        return 4;
                    }
                    ram[ram[reg[FRM_PTR]+2+temp_A]]=value_B;
                    break;
                case stk_symbol:
                    ram[reg[FRM_PTR]+2+temp_A]=value_B;
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_A){
                        reg[STK_PTR]=reg[FRM_PTR]+2+temp_A;
                        if (reg[STK_PTR]>=RAMSIZE-1){
                            return 2;
                        }
                    }        
                    break;
            }
            break; 
        
        case CAL:
            reg[STK_PTR]++;
            ram[reg[STK_PTR]]=reg[PC]; //store what the PC should be upon RET
            ram[reg[STK_PTR]+1]=reg[FRM_PTR]; //store the last frame pointer
            reg[FRM_PTR]=reg[STK_PTR]; //set frame pointer to return adress
            reg[STK_PTR]++;
            reg[PC]=(temp_A*32); //sets PC to a the first instruction of the given function
            if (reg[STK_PTR]>=RAMSIZE-1){
                return 2;
            }
            break;
        
        case RET:
            if (reg[FRM_PTR]==0){
                return 1;
            }
            reg[PC]=ram[reg[FRM_PTR]]; //go to return address
            reg[STK_PTR]=reg[FRM_PTR]-1; //update top of stack, current stackframe is now garbage
            reg[FRM_PTR]=ram[reg[FRM_PTR]+1]; //set frame pointer to previous one
            break;
        
        case REF:
            switch(temp_type_B){
                case stk_symbol:
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_A){
                        reg[STK_PTR]=reg[FRM_PTR]+2+temp_A;
                        if (reg[STK_PTR]>=RAMSIZE-1){
                            return 2;
                        }
                    }  
                    value_B=reg[FRM_PTR]+2+temp_B;
                    break;
                case ptr:
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_B){
                        return 3;
                    }
                    if (reg[STK_PTR]<ram[reg[FRM_PTR]+2+temp_B]){
                        return 4;
                    }
                    value_B=ram[reg[FRM_PTR]+2+temp_B];
                    break;
            }
            switch (temp_type_A){
                case reg_address:
                    reg[temp_A]=value_B;
                    break;
                case stk_symbol:
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_A){
                        return 3;
                    }
                    ram[reg[FRM_PTR]+2+temp_A]=value_B;
                    break;
                case ptr:
                    if (reg[STK_PTR]<ram[reg[FRM_PTR]+2+temp_A]){
                        return 4;
                    }
                    ram[ram[reg[FRM_PTR]+2+temp_A]]=value_B;
                    break;
            }
            break; 
        
        case ADD:
            reg[temp_A]=reg[temp_A]+reg[temp_B];
            break;
        
        case PRINT:
            switch(temp_type_A){
                case value:
                    fprintf(stdout,"%u\n",temp_A);
                    break;
                case reg_address:
                    fprintf(stdout,"%u\n",reg[temp_A]);
                    break;
                case stk_symbol:
                    if (reg[STK_PTR]<reg[FRM_PTR]+2+temp_A){
                        return 3;
                    }
                    fprintf(stdout,"%u\n",ram[reg[FRM_PTR]+2+temp_A]);
                    break;
                case ptr:
                    if (reg[STK_PTR]<ram[reg[FRM_PTR]+2+temp_A]){
                        return 4;
                    }
                    fprintf(stdout,"%u\n",ram[ram[reg[FRM_PTR]+2+temp_A]]);
                    break;
            }
            break; 
        
        case NOT:
            reg[temp_A]=~reg[temp_A];
            break; 
        
        case EQU:
            if (reg[temp_A]==0){
                reg[temp_A]=1;
            }
            else{
                reg[temp_A]=0;
            }
            break;
    }
    return 0;
}

int main(int argc, char** argv){

    //read binary file and parse instructions

    if (argc<2){
        fprintf(stderr,"ERROR: Missing command line argument\n");
        return 1;
    }

    BYTE buffer[MAXSIZE]; 
    FILE* file;
    int c;
    int bytes_read=0;
    if (file= fopen(argv[1],  "rb")){
        while ((c=fgetc(file))!=EOF){                       
            buffer[bytes_read]=c;
            bytes_read++;
        }
    }  
    else{
        fprintf(stderr,"ERROR: File does not exist\n");
        return 1;
    }
    fclose(file);

    //instructions will be stored in an array with a natural mapping to get to each function e.g. func 0 instructions start at index 0, func 1 instructions start at index 32
    struct instruction instructions[256]; 
    struct instruction temp_function[32];
    struct instruction temp_instruction;
    int n_instructions=0;
    int first_byte=bytes_read-1;
    int position=0;
    int i=0;
    int has_entry=0;
    
    BYTE opcode=0;
    BYTE temp_type_A=0;
    BYTE temp_type_B=0;
    BYTE temp_A=0;
    BYTE temp_B=0;
    int temp_shift=0;
    int temp_func_num=0;

    while (position<first_byte*8){
        if (n_instructions==0){
            n_instructions=read_n_bits(5, buffer, &position, first_byte);
            reset_array(temp_function,32);
            for (i=0; i<n_instructions;i++){
                opcode=read_n_bits(3, buffer, &position, first_byte); 
                temp_function[i].opcode=opcode;
                switch(opcode){
                    case 2:
                        break; 
                    case 1:
                    case 5:
                    case 6:
                    case 7:
                        temp_type_A=read_n_bits(2, buffer, &position, first_byte);
                        temp_function[i].param=temp_function[i].param | temp_type_A;

                        temp_A=read_value(buffer, &position, first_byte, temp_type_A);
                        temp_function[i].param=temp_function[i].param | temp_A << 2;
                        break;
                    case 0:
                    case 3:
                    case 4:
                        temp_type_A=read_n_bits(2, buffer, &position, first_byte);
                        temp_function[i].param=temp_function[i].param | temp_type_A;
                        temp_shift=position;    

                        temp_A=read_value(buffer, &position, first_byte, temp_type_A);
                        temp_function[i].param=temp_function[i].param | (temp_A << 2);
                        temp_shift=position-temp_shift;

                        temp_type_B=read_n_bits(2, buffer, &position, first_byte);
                        temp_function[i].param=temp_function[i].param | (temp_type_B << (2+temp_shift));

                        temp_B=read_value(buffer, &position, first_byte, temp_type_B);
                        temp_function[i].param=temp_function[i].param | (temp_B << (4+temp_shift));
                        break;
                }
            }
            temp_func_num=read_n_bits(3, buffer, &position, first_byte);
            if (temp_func_num==0){
                has_entry=1;
            }
            for (i=0; i<n_instructions;i++){
                instructions[temp_func_num*32+i]=temp_function[n_instructions-1-i];
            }
            n_instructions=0; 
        }
    }

    if (!has_entry){
        fprintf(stderr, "ERROR: The parsed instructions do not define FUNC 0\n");
        return 1;
    }


    //all operations and program position is stored within these arrays
    BYTE ram[RAMSIZE];
    BYTE reg[REGSIZE];

    //reset all elements to 0 to prevent undefined behaviour
    for (int i=0; i<RAMSIZE; i++){
        ram[i]=0;
    }
    for (int i=0; i<REGSIZE; i++){
        reg[i]=0;
    }

    int instruction_status=0;

    while (RUNNING){
        instruction_status=execute(instructions[reg[PC]],ram,reg);
        switch (instruction_status){
            case 1:
                return 0;
            case 2:
                fprintf(stderr,"ERROR: Stack overflow\n");
                return 1;
            case 3:
                fprintf(stderr, "ERROR: Tried to access stack variable before declaration\n");
                return 1;
            case 4:
                fprintf(stderr, "ERROR: Tried to access address outside stack memory\n");
                return 1;
        }
    }
}
