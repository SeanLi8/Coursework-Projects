#include "objdump_x2017.h"

int string_compare(const char* w1, const char* w2, unsigned int len){
    for (int i=0; i<len; i++){
        if (w1[i]!=w2[i]){
            return 1;
        }
    }
    return 0;
}

int reset_mappings(char* mappings){
    for (int i=0; i<32; i++){
        mappings[i]=0;
    }
}

int reorder_mappings(char* mappings, int value){
    for (int i=0; i<32; i++){
        if (mappings[i]!=0 && mappings[i]<mappings[value]){
            mappings[i]=mappings[i] + 1;
        }    
    }
    mappings[value]=1;
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

int opcode_string(char* buffer, int opcode){
    switch (opcode){
        case 0:
            strcpy(buffer, "\n    MOV");
            break;
        case 1:
            strcpy(buffer, "\n    CAL");
            break;
        case 2:
            strcpy(buffer, "\n    RET");
            break;
        case 3:
            strcpy(buffer, "\n    REF");
            break;
        case 4:
            strcpy(buffer, "\n    ADD");
            break;
        case 5:
            strcpy(buffer, "\n    PRINT");
            return 10;
        case 6:
            strcpy(buffer, "\n    NOT");
            break;
        case 7:
            strcpy(buffer, "\n    EQU"); 
            break;
    }
    return 8;
}

int value_string(char* buffer, int value_type, int value, int* symbol_count, char* mapping){
    switch(value_type){
        case 0:
            sprintf(buffer, " VAL %d",  value);
            if (value>=100){
                return 8;
            }
            else if(value>=10){
                return 7;
            }
            break;
        case 1:
            sprintf(buffer, " REG %d",  value);
            break;
        case 2:
            if (mapping[value]==0){
                *symbol_count=*symbol_count+1;
                mapping[value]=*symbol_count;
                reorder_mappings(mapping,value);
                sprintf(buffer, " STK %c", value);    
            }
            else{
                reorder_mappings(mapping,value);
                sprintf(buffer, " STK %c", value);
            }
            break;
        case 3:
            if (mapping[value]==0){
                *symbol_count=*symbol_count+1;
                mapping[value]=*symbol_count;
                reorder_mappings(mapping,value);
                sprintf(buffer, " PTR %c", value);     
            }
            else{
                reorder_mappings(mapping,value);
                sprintf(buffer, " PTR %c", value);
            }
            break;
    }
    return 6;
} 

int label_string(char* buffer, const int label){
    sprintf(buffer,"\nFUNC LABEL %d",label);
    if(label>=10){ return 14;}
    else{ return 13; }   
}


int main(int argc, char** argv){
    
    //The file is read in reverse order, we build the assembly code starting from a large garbage value c string, while 
    //parsing the file, appropriate strings are added onto the c string.

    if (argc<2){
        fprintf(stderr,"ERROR: Missing command line argument\n");
        return 1;
    }

    BYTE buffer[MAXSIZE]; 
    FILE* file;
    int c;
    int i=0;
    if (file= fopen(argv[1],  "rb")){
        while ((c=fgetc(file))!=EOF){                       
            buffer[i]=c;
            i++;
        }
    }  
    else{
        fprintf(stderr,"ERROR: File does not exist\n");
        return 1;
    }
    
    char string[MAXSTRING];
    char temp_string[15];
    int temp_index=0;
    int temp_string_offset;
    int string_index=MAXSTRING-1; //the start of the assembly string, constantly updated as more chars are added in reverse
    int n_instructions=0;
    int first_byte=i-1;
    int position=0;
    int opCode=0;
    int symbol_count=0;
    
    int temp_type_A=0;
    int temp_type_B=0;
    int temp_A=0;
    int temp_B=0;
    char temp_char_mapping=0;
    char char_mapping[32]; //tracks the order in which each stk symbol appears, address 0 represents A, address 1 rebpresents B and so on

    while (position<first_byte*8){    
        n_instructions=read_n_bits(5, buffer, &position, first_byte);
        symbol_count=0;
        reset_mappings(char_mapping);
        for (int instruction=1; instruction<=(int)n_instructions;instruction++){
            opCode=read_n_bits(3, buffer, &position, first_byte);
            switch(opCode){
                case 2:
                    break; 
                case 1:
                case 5:
                case 6:
                case 7:
                    temp_type_A=read_n_bits(2, buffer, &position, first_byte);
                    temp_A=read_value(buffer, &position, first_byte, temp_type_A);

                    temp_string_offset=value_string(temp_string, temp_type_A, temp_A, &symbol_count, char_mapping);
                    string_index-=temp_string_offset;
                    memcpy(string+string_index, temp_string, temp_string_offset);
                    break;
                case 0:
                case 3:
                case 4:
                    temp_type_A=read_n_bits(2, buffer, &position, first_byte);
                    temp_A=read_value(buffer, &position, first_byte, temp_type_A);
                    temp_type_B=read_n_bits(2, buffer, &position, first_byte);
                    temp_B=read_value(buffer, &position, first_byte, temp_type_B);

                    temp_string_offset=value_string(temp_string, temp_type_B, temp_B, &symbol_count, char_mapping);
                    string_index-=temp_string_offset;
                    memcpy(string+string_index, temp_string, temp_string_offset);

                    temp_string_offset=value_string(temp_string, temp_type_A, temp_A, &symbol_count, char_mapping);
                    string_index-=temp_string_offset;
                    memcpy(string+string_index, temp_string, temp_string_offset);
                    break; 
            }
            temp_string_offset=opcode_string(temp_string, opCode);
            string_index-=temp_string_offset;
            memcpy(string+string_index, temp_string, temp_string_offset);
        }

        temp_index=string_index;
        
        //go through the recently appended function and change stk symbols accoriding to current mapping array

        while ((temp_index) != MAXSTRING-1 ){
            if (string_compare(string + temp_index,"STK",3)==0 || string_compare(string + temp_index,"PTR",3)==0){ 
                temp_index+=4;
                temp_char_mapping=char_mapping[string[temp_index]];
                if(temp_char_mapping<=26){
                    string[temp_index]=temp_char_mapping - 1 + 'A';
                }
                else{
                    string[temp_index]=temp_char_mapping - 27 + 'a';
                }    
            }
            temp_index++;
            if (temp_index +4 != MAXSTRING-1 && string_compare(string + temp_index,"FUNC",4)==0){
                break;
            }
        }
        temp_string_offset=label_string(temp_string, read_n_bits(3, buffer, &position, first_byte));
        string_index-=temp_string_offset;
        memcpy(string+string_index, temp_string, temp_string_offset);
    }
    printf("%s", string+string_index+1);
}
