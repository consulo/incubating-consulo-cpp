int counter asm("my_counter");

void f(int x) {
    asm("nop");
    __asm__ volatile ("movl %1, %%eax\n\t"
                      "addl $1, %%eax" : "=a"(x) : "r"(x) : "cc");
    __asm { mov eax, 1 }
}
