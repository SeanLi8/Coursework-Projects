#! /usr/bin/env sh
export ASAN_OPTIONS=verify_asan_link_order=0

#Test running template taken from info1110 acorn assignment
echo "##########################"
echo "##### Running tests! #####"
echo "##########################"
count=0 # number of test cases run so far

# Assume all `.in` and `.out` files are located in a separate `tests` directory

for folder in `ls -d tests/*/ | sort -V`; do
    name=$(basename "$folder")
    
    echo Running test $name.

    arguments_file=tests/$name/$name.args
    expected_file=tests/$name/$name.out
    in_file=tests/$name/$name.in

    if [ "$name" = "no_timetable" ];then 
        cat $in_file | xargs -a $arguments_file ./timetable 2> tests/$name/$name.error 
        diff tests/$name/$name.error $expected_file || echo "Test $name: failed!"
        count=$((count+1))
        continue
    fi

    # Change this command to run your program!
    # You will need to read the code here and figure out how to pass in your config yourself!
    cat $in_file | xargs -a $arguments_file ./timetable | diff - $expected_file || echo "Test $name: failed!"

    count=$((count+1))
done

echo "Finished running $count tests!"