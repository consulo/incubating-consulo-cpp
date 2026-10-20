void set_callback(void (* callback)(const char*, int));
static void (* stored)(const char*, int) = NULL;
typedef int (*compare)(const void *, const void *);
int (*handlers[4])(int);
