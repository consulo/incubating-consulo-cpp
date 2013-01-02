#ifndef CT_ASSERT
#define CT_ASSERT_CONCAT_(a, b) a##b
#define CT_ASSERT_CONCAT(a, b) CT_ASSERT_CONCAT_(a, b)
#define CT_ASSERT(e) typedef int CT_ASSERT_CONCAT(__compile_assert_line_, __LINE__)[(e) ? 1 : -1];
#endif // CT_ASSERT

#ifndef CT_ASSERTMSG
#define CT_ASSERTMSG(e, n) typedef int __compile_assert_msg_##n [(e) ? 1 : -1];
#endif // CT_ASSERTMSG
