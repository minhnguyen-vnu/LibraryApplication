package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLibraryModel {
    private static AdminLibraryModel model;
    private ObservableList<BookInfo> bookList;
    private int totalUsers;
    private int totalUser30PreviousDays;
    private int totalHiredBooks;

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
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    count(*) as cnt\n" +
                    "from users\n" +
                    "where isAdmin = 0;");
            if (resultSet.next()) {
                totalUsers = resultSet.getInt("cnt");
            }
            resultSet = DBUtlis.executeQuery("select count(*) as cnt from users\n" +
                    "where registeredDate <= current_date - interval 30 day;");
            if (resultSet.next()) {
                totalUser30PreviousDays = resultSet.getInt("cnt");
            }
            resultSet = DBUtlis.executeQuery("select count(*) as cnt from userRequest\n" +
                    "where returnDate > current_date or returnDate is null;");
            if (resultSet.next()) {
                totalHiredBooks = resultSet.getInt("cnt");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
