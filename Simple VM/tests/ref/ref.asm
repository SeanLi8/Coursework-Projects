FUNC LABEL 0
    MOV STK A VAL 1
    MOV STK B VAL 2
    MOV STK C VAL 3
    REF STK A STK B
    PRINT PTR A
    MOV STK A VAL 1
    REF REG 0 STK A
    MOV STK C REG 0
    PRINT PTR C
    REF PTR C STK B
    PRINT PTR A
    CAL VAL 1
    RET
FUNC LABEL 1
    MOV STK A VAL 1
    MOV STK B VAL 2
    MOV STK C VAL 3
    MOV STK D VAL 4
    REF STK A STK B
    REF STK C PTR A
    PRINT PTR C
    REF STK C STK D
    REF PTR A PTR C
    PRINT PTR B
    REF REG 0 PTR C
    MOV STK E REG 0
    PRINT PTR E
    RET