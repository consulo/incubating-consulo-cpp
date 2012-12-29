#include <QtGui/QApplication>
#include "mainwindow.h"
#include "qtwin.h"

int main(int argc, char *argv[])
{
    QApplication a(argc, argv);
    MainWindow w;

    if (QtWin::isCompositionEnabled())
    {
        QtWin::extendFrameIntoClientArea(&w);
        w.setContentsMargins(0, 0, 0, 0);
    }
    w.show();
    
    return a.exec();
}
