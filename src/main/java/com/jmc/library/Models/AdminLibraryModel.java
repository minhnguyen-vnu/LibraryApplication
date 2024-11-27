package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model class for the admin library view.
 */
public class AdminLibraryModel {
    private static AdminLibraryModel model;
    private ObservableList<BookInfo> bookList;
    private int totalUsers;
    private int totalUser30PreviousDays;
    private int totalHiredBooks;
    private boolean thread1, thread2, thread3;

    private AdminLibraryModel() {
        bookList = FXCollections.observableArrayList();
        dataFetching();
    }

    public static synchronized AdminLibraryModel getInstance() {
        if (model == null) model = new AdminLibraryModel();
        return model;
    }

    public ObservableList<BookInfo> getBookList() {
        return bookList;
    }

    public void setBookList(ObservableList<BookInfo> bookList) {
        this.bookList = bookList;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }

    public int getTotalUser30PreviousDays() {
        return totalUser30PreviousDays;
    }

    public void setTotalUser30PreviousDays(int totalUser30PreviousDays) {
        this.totalUser30PreviousDays = totalUser30PreviousDays;
    }

    public int getTotalHiredBooks() {
        return totalHiredBooks;
    }

    public void setTotalHiredBooks(int totalHiredBooks) {
        this.totalHiredBooks = totalHiredBooks;
    }

    private void dataFetching() {
        DBQuery totalUsersQuery = new DBQuery("select count(*) as cnt from users where isAdmin = 0;");
        totalUsersQuery.setOnSucceeded(event -> {
            ResultSet resultSet = totalUsersQuery.getValue();
            try {
                if (resultSet.next()) {
                    totalUsers = resultSet.getInt("cnt");
                    thread1 = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        totalUsersQuery.setOnFailed(event -> totalUsersQuery.getException().printStackTrace());

        DBQuery totalUser30PreviousDaysQuery = new DBQuery("select count(*) as cnt from users where registeredDate <= current_date - interval 30 day;");
        totalUser30PreviousDaysQuery.setOnSucceeded(event -> {
            ResultSet resultSet = totalUser30PreviousDaysQuery.getValue();
            try {
                if (resultSet.next()) {
                    totalUser30PreviousDays = resultSet.getInt("cnt");
                    thread2 = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        totalUser30PreviousDaysQuery.setOnFailed(event -> totalUser30PreviousDaysQuery.getException().printStackTrace());

        DBQuery totalHiredBooksQuery = new DBQuery("select count(*) as cnt from userRequest where returnDate > current_date or returnDate is null;");
        totalHiredBooksQuery.setOnSucceeded(event -> {
            ResultSet resultSet = totalHiredBooksQuery.getValue();
            try {
                if (resultSet.next()) {
                    totalHiredBooks = resultSet.getInt("cnt");
                    thread3 = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        totalHiredBooksQuery.setOnFailed(event -> totalHiredBooksQuery.getException().printStackTrace());

        Thread totalUsersThread = new Thread(totalUsersQuery);
        Thread totalUser30PreviousDaysThread = new Thread(totalUser30PreviousDaysQuery);
        Thread totalHiredBooksThread = new Thread(totalHiredBooksQuery);

        totalUsersThread.setDaemon(true);
        totalUser30PreviousDaysThread.setDaemon(true);
        totalHiredBooksThread.setDaemon(true);

        totalUsersThread.start();
        totalUser30PreviousDaysThread.start();
        totalHiredBooksThread.start();
    }

    public boolean isThread2() {
        return thread2;
    }

    public void setThread2(boolean thread2) {
        this.thread2 = thread2;
    }

    public boolean isThread1() {
        return thread1;
    }

    public void setThread1(boolean thread1) {
        this.thread1 = thread1;
    }

    public boolean isThread3() {
        return thread3;
    }

    public void setThread3(boolean thread3) {
        this.thread3 = thread3;
    }
}
