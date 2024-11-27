package com.jmc.library.Assets;

import com.jmc.library.Models.LibraryModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;

public class UserDashboardInfo {
    public ListProperty<Integer> borrowedBooks;
    public ListProperty<Integer> readBooks;

    public Label total_read_book_lbl;
    public Label new_books_read_lbl;
    public Label total_hired_book_lbl;

    int countReadBook;
    int countHiredBook;
    int countNewReadBook;

    public UserDashboardInfo() {
        borrowedBooks = new SimpleListProperty<>(FXCollections.observableArrayList());
        readBooks = new SimpleListProperty<>(FXCollections.observableArrayList());
        total_hired_book_lbl = new Label();
        new_books_read_lbl = new Label();
        total_read_book_lbl = new Label();
    }

    public List<Integer> getBorrowedBooks() {
        return borrowedBooks.get();
    }

    public ListProperty<Integer> borrowedBooksProperty() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ObservableList<Integer> borrowedBooks) {
        this.borrowedBooks.set(borrowedBooks);
    }

    public List<Integer> getReadBooks() {
        return readBooks.get();
    }

    public ListProperty<Integer> readBooksProperty() {
        return readBooks;
    }

    public void setReadBooks(ObservableList<Integer>  readBooks) {
        this.readBooks.set(readBooks);
    }

    public void setBorrowedBooks(int month) {
        List<Integer> borrowedBooks = getBorrowedBooks();
        borrowedBooks.set(month, borrowedBooks.get(month) + 1);
        setBorrowedBooks(FXCollections.observableArrayList(borrowedBooks));
    }

    public Label getTotal_read_book_lbl() {
        return total_read_book_lbl;
    }

    public void setTotal_read_book_lbl(Label total_read_book_lbl) {
        this.total_read_book_lbl = total_read_book_lbl;
    }

    public Label getNew_books_read_lbl() {
        return new_books_read_lbl;
    }

    public void setNew_books_read_lbl(Label new_books_read_lbl) {
        this.new_books_read_lbl = new_books_read_lbl;
    }

    public Label getTotal_borrowed_book_lbl() {
        return total_hired_book_lbl;
    }

    public void setTotal_hired_book_lbl(Label total_hired_book_lbl) {
        this.total_hired_book_lbl = total_hired_book_lbl;
    }

    public void setCountBorrowedBook(int countHiredBook) {
        this.countHiredBook = countHiredBook;
    }

    public void setCountReadBook(int countReadBook) {
        this.countReadBook = countReadBook;
    }

    public void setCountNewReadBook(int countNewReadBook) {
        this.countNewReadBook = countNewReadBook;
    }

    public int getCountBorrowedBook() {
        return countHiredBook;
    }

    public int getCountReadBook() {
        return countReadBook;
    }

    public int getCountNewReadBook() {
        return countNewReadBook;
    }

    public void onDashBoardSetUp() {
        List<Integer> tempBorrowedBooks = FXCollections.observableArrayList();
        List<Integer> tempReadBooks = FXCollections.observableArrayList();

        for (int i = 0; i < 12; i++) {
            tempReadBooks.add(0);
            tempBorrowedBooks.add(0);
        }

        for (UserBookInfo book : LibraryModel.getInstance().getUser().getBorrowedBookList()) {
            int month = book.getPickedDate().getMonthValue() - 1;
            if (book.getReturnDate().isBefore(LocalDate.now())) {
                countReadBook ++;
                if(book.getReturnDate().isBefore(LocalDate.now().minusDays(7))) {
                    countNewReadBook ++;
                }
                tempReadBooks.set(month, tempReadBooks.get(month) + 1);
            }
            countHiredBook ++;
            tempBorrowedBooks.set(month, tempBorrowedBooks.get(month) + 1);
        }
        total_read_book_lbl.setText(String.valueOf(countReadBook));
        new_books_read_lbl.setText(String.valueOf(countNewReadBook));
        total_hired_book_lbl.setText(String.valueOf(countHiredBook));
        setReadBooks(FXCollections.observableArrayList(tempReadBooks));
        setBorrowedBooks(FXCollections.observableArrayList(tempBorrowedBooks));
    }

    public void clear() {
        countReadBook = 0;
        countHiredBook = 0;
        countNewReadBook = 0;
        borrowedBooks.clear();
        readBooks.clear();
        total_hired_book_lbl.setText("");
        new_books_read_lbl.setText("");
        total_read_book_lbl.setText("");
    }
}
