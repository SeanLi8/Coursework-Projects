#! /usr/bin/env sh
export ASAN_OPTIONS=verify_asan_link_order=0

#Test running template taken from info1110 acorn assignment
echo 
echo "##########################"
echo "##### Running tests! #####"
echo "##########################"
echo

count=0 # number of test cases run so far

# Assume all `.in` and `.out` files are located in a separate `tests` directory

for folder in `ls -d tests/*/ | sort -V`; do
    name=$(basename "$folder")
    
    if [ "$name" = "fnf" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 bruh.txt 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))

        echo Running test $name objdump
        expected_file_od=tests/$name/$name.od
        ./objdump_x2017 bruh.txt  2> tests/$name/$name.oderr
        diff tests/$name/$name.oderr $expected_file_od || echo "Test $name-objectdump: failed!"
        count=$((count+1))
        continue
    fi

    if [ "$name" = "no_arguments" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))

        echo Running test $name objdump
        expected_file_od=tests/$name/$name.od
        ./objdump_x2017 2> tests/$name/$name.oderr
        diff tests/$name/$name.oderr $expected_file_od || echo "Test $name-objectdump: failed!"
        count=$((count+1))
        continue
    fi

    if [ "$name" = "stk_overflow" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 tests/$name/$name.x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))

        echo Running test $name objdump
        expected_file_od=tests/$name/$name.od
        ./objdump_x2017 tests/$name/$name.x2017 | diff - $expected_file_od || echo "Test $name-objectdump: failed!"
        count=$((count+1))

        continue
    fi

    if [ "$name" = "invalid_symbol" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 tests/$name/$name.x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))

        echo Running test $name objdump
        expected_file_od=tests/$name/$name.od
        ./objdump_x2017 tests/$name/$name.x2017 | diff - $expected_file_od || echo "Test $name-objectdump: failed!"
        count=$((count+1))

        continue
    fi
    
     if [ "$name" = "invalid_symbol2" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 tests/$name/$name.x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))
        
        continue
    fi
    
    if [ "$name" = "invalid_ptr1" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 tests/$name/$name.x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))

        continue
    fi

    if [ "$name" = "invalid_ptr2" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 tests/$name/$name.x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))
        
        continue
    fi

    if [ "$name" = "no_entry" ];then 
        echo Running test $name vm
        expected_file_vm=tests/$name/$name.vm
        ./vm_x2017 tests/$name/$name.x2017 2> tests/$name/$name.vmerr 
        diff tests/$name/$name.vmerr $expected_file_vm || echo "Test $name vm: failed!"
        count=$((count+1))

        echo Running test $name objdump
        expected_file_od=tests/$name/$name.od
        ./objdump_x2017 tests/$name/$name.x2017 | diff - $expected_file_od || echo "Test $name-objectdump: failed!"
        count=$((count+1))

        continue
    fi
    


    echo Running test $name vm
    expected_file_vm=tests/$name/$name.vm
    ./vm_x2017 tests/$name/$name.x2017 | diff - $expected_file_vm || echo "Test $name vm: failed!"
    count=$((count+1))

    echo Running test $name objdump
    expected_file_od=tests/$name/$name.od
    ./objdump_x2017 tests/$name/$name.x2017 | diff - $expected_file_od || echo "Test $name-objectdump: failed!"
    count=$((count+1))
done

echo
echo "Finished running $count tests!"