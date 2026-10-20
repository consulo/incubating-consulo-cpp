#define NAME(x) #x
#define VAR(n) var_ ## n
const char* s = NAME(hello);
int VAR(1) = 0;
