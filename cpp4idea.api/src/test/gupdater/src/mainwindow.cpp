#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "qlistwidget.h"

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent), ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    ui->listWidget->addItem(new QListWidgetItem(QString::fromUtf8("Lineage 2")));
    ui->listWidget->addItem(new QListWidgetItem(QString::fromUtf8("Aion")));
}

MainWindow::~MainWindow()
{
    delete ui;
}
