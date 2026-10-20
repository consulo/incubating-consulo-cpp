int foo(int argc, char** argv) {
    for (int i = 0; i < argc; i++) {
        if (argv[i] == NULL) continue;
        else break;
    }
    do { argc--; } while (argc > 0);
    switch (argc) {
        case 1 ... 3:
            goto done;
        default:
            ;
    }
done:
    return argc;
}
