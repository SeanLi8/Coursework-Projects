CC=gcc
CFLAGS=-fsanitize=address -Wvla -Wall -Werror -std=gnu11 -lasan 
LDFLAGS=-s 


# fill in all your make rules

vm_x2017: vm_x2017.c vm_x2017.h
	gcc -Os -s -z norelro -z max-page-size=4096   -o  vm_x2017 vm_x2017.c
	strip --remove-section .note.gnu.property vm_x2017
	strip --remove-section .note.gnu.build-id vm_x2017
	strip --remove-section .note.ABI-tag vm_x2017
	strip --remove-section .gnu.hash vm_x2017
	strip --remove-section .shstrtab vm_x2017
	strip --remove-section .comment vm_x2017
	strip --remove-section .bss vm_x2017
	strip --remove-section .data vm_x2017
	strip --remove-section .got vm_x2017
	strip --remove-section .fini_array vm_x2017
	strip --remove-section .init_array vm_x2017
	strip --remove-section .eh_frame vm_x2017
	strip --remove-section .eh_frame_hdr vm_x2017
	strip --remove-section .eh_frame_hdr vm_x2017
	strip --remove-section .shstrtab vm_x2017
	strip --remove-section .rela.pit vm_x2017
	
	
objdump_x2017: objdump_x2017.c objdump_x2017.h
	gcc -Os  -o objdump_x2017 objdump_x2017.c


tests: x2017_compiler.py
	bash testgen.sh


run_tests: vm_x2017	objdump_x2017 
	bash test.sh

clean:
	rm vm_x2017
	rm objdump_x2017
