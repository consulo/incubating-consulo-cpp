typedef unsigned long size_type;
typedef struct __array array;
typedef enum {
  MSG_INSTANCE_LIMIT, MSG_WATCH_LIMIT
} MSG;
typedef struct {
  int x, y;
} point;
array* array_create(int initial_capacity);
point origin;
