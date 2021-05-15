CC=gcc
CFLAGS=-fsanitize=address -Wall -Werror -std=gnu11 -g -lm

#this one for ed
tests: tests.c virtual_alloc.c
	$(CC) $(CFLAGS) $^ -o $@


#only need this for local tests
run_tests: tests.c virtual_alloc.c
	./tests
	bash test.sh
