void foo(int a, int b) {
    int c = a >= b ? a >> 1 : b << 2;
    c >>= 1;
    c |= a & ~b;
    if (a != b && !(a <= 0) || b % 2 == 1) c++;
    c = sizeof(int) + sizeof c + sizeof(struct point);
    p->next->value += (a, b);
}
