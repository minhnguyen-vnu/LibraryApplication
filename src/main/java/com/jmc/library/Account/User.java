package com.jmc.library.Account;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Database.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Account {
    private ObservableList<UserBookInfo> bookList;

    public User() {
        this.bookList = FXCollections.observableArrayList();
    }

    public ObservableList<UserBookInfo> getBookList() {
        return bookList;
    }

    public void setBookList(ObservableList<UserBookInfo> bookList) {
        this.bookList = bookList;
    }

    public void loadBookList() {
        bookList.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    r.username,\n" +
                    "    r.bookName,\n" +
                    "    b.authorName,\n" +
                    "    r.bookId,\n" +
                    "    r.pickedDate,\n" +
                    "    r.returnDate,\n" +
                    "    r.cost\n" +
                    "from userRequest r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ?;", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"));
                System.out.println(userBookInfo);
                this.bookList.add(userBookInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAdmin() {
        return false;
    }
}
