void foo(void* p, int n) {
    struct event* e = (struct event*) &p[n];
    long l = (long) n;
    size_t s = (size_t)(n + 1);
    char c = (char) -1;
    int sum = (n) - 1;
    struct point q = (struct point) { 1, 2 };
}
