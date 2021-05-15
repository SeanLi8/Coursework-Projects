#include <stdio.h>
#include <stdlib.h>
#define MAXLENGTH (4096)
#define MAXTIME (24*60*60)

int string_compare(const char* w1, const char* w2, unsigned int len); /*Checks if two strings are equal by comparing characters upto len*/
int modM(int x,int mod);/*Returns congruent number in csor mod n*/
int lineLengthTest(char *line);/*Test that line contains a newline character, if it doesn't the line was too long*/
int totalDoubleColonTest(char* line, int lineLength);/*Checks that there are 2 double colon's in a line*/
int timeFormatTest(char* time,int timeLength);/*Checks time is formatted as HH:MM::SS*/
int searchValid(int argc,char** argv);/*Checks the user inputted arguments are valid*/


int string_compare(const char* w1, const char* w2, unsigned int len){
    for (int i=0; i<len; i++){
        if (w1[i]!=w2[i]){
            return 0;
        }
    }
    return 1;
}

int modM(int x,int mod){
    while(x<0){
        x+=mod;
    }
    while(x>mod-1){
        x-=mod;
    }
    return x;
}

int lineLengthTest(char* line){
    int endfound=0;
    for(int k=0;k<MAXLENGTH;k++){
        if(line[k]=='\n'){
            endfound=1;
        }
    }

    if (endfound){
        return 1;
    }
    else{
        return 0;
    }
}

int totalDoubleColonTest(char* line, int lineLength){
    int count=0;
    for(int i=0; i<lineLength-3; i++){
        if (string_compare(line+i+1,"::",2) && line[i]!=58 && line[i+3]!=58 ){
            count++;
        }
    }
    if (count==2){
        return 1;
    }
    else{
        return 0;
    }
}

int timeFormatTest(char* time,int timeLength){
    if (timeLength!=8){
        return 0;
    }
    if ((48<=time[0] && time[0]<=50) && 
        (48<=time[1] && time[1]<=57) &&
        !(time[0]==50 && 52<=time[1] && time[1]<=57) &&
        time[2]==58 &&
        (48<=time[3] && time[3]<=53) &&
        (48<=time[4] && time[4]<=57) &&
        time[5]==58 &&
        (48<=time[6] && time[6]<=53) &&
        (48<=time[7] && time[7]<=57)
        ){
        return 1;
    }
    else{
        return 0;
    }
}

int searchValid(int argc,char** argv){
    if(argc!=4){
        printf("Please provide <source> <destination> <time> as command line arguments\n");
        return 0;
    }
    int searchTimeLength=0;
    for (;argv[3][searchTimeLength]!='\0';searchTimeLength++);
    if(!timeFormatTest(argv[3],searchTimeLength)){
        printf("Invalid time inputted\n");
        return 0;
    }
    else{
        return 1;
    }
}

int main(int argc,char** argv) {
    
    if (!searchValid(argc,argv)){
        return 0;
    }
    
    char line[MAXLENGTH];
    int searchSourceLength=0;
    int searchDestLength=0;
    int currentTime[3];
    sscanf(argv[3],"%d:%d:%d",currentTime,currentTime+1,currentTime+2);
    int currentSeconds =currentTime[0]*60*60+currentTime[1]*60+currentTime[2];
    int minSecondsAway=MAXTIME;
    char result[100]="No suitable trains can be found";
    int found=0;
    int validCount=0;

    for (;argv[1][searchSourceLength]!='\0';searchSourceLength++);
    for (;argv[2][searchDestLength]!='\0';searchDestLength++);


    while (fgets(line,MAXLENGTH,stdin)!=NULL){
        
        if (!lineLengthTest(line)){
            continue;
        }
        
        int lineLength=0;
        for (;line[lineLength]!='\n' ;lineLength++);

        if (!totalDoubleColonTest(line,lineLength)){
            continue;
        }

        char source[MAXLENGTH];
        char dest[MAXLENGTH];
        char timeString[MAXLENGTH];
        int time[3];

        int sourceLength=0;
        int destLength=0;
        int timeLength=0;
        int i=0;

        for(; !string_compare(line+i,"::",2); i++){
            source[i]=line[i];
            sourceLength++;
        }
        i+=2;
    
        for(; !string_compare(line+i,"::",2); i++){
            dest[i-sourceLength-2]=line[i];
            destLength++;

        }
        i+=2;

        for(; line[i]!='\n'; i++){
            timeString[i-sourceLength-destLength-4]=line[i];
            timeLength++;
        }

        if (!timeFormatTest(timeString,timeLength)){
            continue;
        }

        sscanf(timeString,"%d:%d:%d",time,time+1,time+2);

        validCount++;

        if (searchSourceLength==sourceLength && string_compare(argv[1],source,searchSourceLength)){
            if (searchDestLength==destLength && string_compare(argv[2],dest,searchDestLength)){
                found=1;
                int secondsAway=modM(time[0]*60*60+time[1]*60+time[2]-currentSeconds,(MAXTIME));
                if (secondsAway<=minSecondsAway){
                    minSecondsAway=secondsAway;
                    for (int j=0; j<8; j++){
                        result[j]=timeString[j];
                    }
                    result[8]='\0';
                }
            }
        }
    }

    if(validCount==0){
        fprintf(stderr,"No timetable to process.\n");
    }
    else if (!found){
        printf("%s\n",result);
    }
    else{
        printf("The next train to %s from %s departs at %s\n",argv[2],argv[1],result);
    }
    
}

