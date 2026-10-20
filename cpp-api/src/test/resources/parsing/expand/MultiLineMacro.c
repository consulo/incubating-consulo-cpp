#define __FD_ZERO(s) \
  do {									      \
    unsigned int __i;							      \
    fd_set *__arr = (s);						      \
    for (__i = 0; __i < sizeof (fd_set) / sizeof (__fd_mask); ++__i)	      \
      __FDS_BITS (__arr)[__i] = 0;					      \
  } while (0)
#define	FD_ZERO(fdsetp)		__FD_ZERO (fdsetp)
void loop(void) {
  while (1) {
    FD_ZERO(&rfds);
  }
}
