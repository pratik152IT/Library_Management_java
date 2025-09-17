package lms.app;

import lms.db.DBInit;
import lms.models.Book;
import lms.models.User;
import lms.services.BookService;
import lms.services.TransactionService;
import lms.services.UserService;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        DBInit.init(); // create tables + default admin if needed

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        BookService bookService = new BookService();
        TransactionService txService = new TransactionService();

        while (true) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("3) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Enter username: ");
                    String rUser = sc.nextLine().trim();
                    System.out.print("Enter password: ");
                    String rPass = sc.nextLine().trim();
                    boolean ok = userService.register(rUser, rPass, "MEMBER");
                    if (ok) System.out.println("Registered. Login to continue.");
                    break;

                case "2":
                    System.out.print("Username: ");
                    String u = sc.nextLine().trim();
                    System.out.print("Password: ");
                    String p = sc.nextLine().trim();
                    User user = userService.login(u, p);
                    if (user == null) {
                        System.out.println("Invalid credentials.");
                    } else {
                        System.out.println("Welcome " + user);
                        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                            adminMenu(sc, bookService, txService, userService);
                        } else {
                            memberMenu(sc, bookService, txService, user);
                        }
                    }
                    break;

                case "3":
                    System.out.println("Bye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void adminMenu(Scanner sc, BookService bookService, TransactionService txService, UserService userService) {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1) Add Book");
            System.out.println("2) List all books");
            System.out.println("3) Search books");
            System.out.println("4) Issue book to user");
            System.out.println("5) Return book");
            System.out.println("6) View overdue reminders");
            System.out.println("7) Back / Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.print("Title: "); String title = sc.nextLine();
                    System.out.print("Author: "); String author = sc.nextLine();
                    if (bookService.addBook(title, author)) System.out.println("Book added.");
                    break;
                case "2":
                    List<Book> all = bookService.listAllBooks();
                    all.forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("Keyword: "); String kw = sc.nextLine();
                    bookService.searchBooks(kw).forEach(System.out::println);
                    break;
                case "4":
                    System.out.print("User ID to issue to: "); int uid = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Book ID: "); int bid = Integer.parseInt(sc.nextLine().trim());
                    txService.issueBook(uid, bid);
                    break;
                case "5":
                    System.out.print("Book ID to return: "); int rb = Integer.parseInt(sc.nextLine().trim());
                    txService.returnBook(rb);
                    break;
                case "6":
                    txService.checkDueReminders();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid.");
            }
        }
    }

    private static void memberMenu(Scanner sc, BookService bookService, TransactionService txService, User user) {
        while (true) {
            System.out.println("\n--- MEMBER MENU ---");
            System.out.println("1) List all books");
            System.out.println("2) Search books");
            System.out.println("3) Borrow book");
            System.out.println("4) Return book");
            System.out.println("5) My transactions");
            System.out.println("6) Check overdue reminders");
            System.out.println("7) Back / Logout");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            switch (c) {
                case "1":
                    bookService.listAllBooks().forEach(System.out::println);
                    break;
                case "2":
                    System.out.print("Keyword: "); String kw = sc.nextLine();
                    bookService.searchBooks(kw).forEach(System.out::println);
                    break;
                case "3":
                    System.out.print("Book ID to borrow: "); int bid = Integer.parseInt(sc.nextLine().trim());
                    txService.issueBook(user.getUserId(), bid);
                    break;
                case "4":
                    System.out.print("Book ID to return: "); int rb = Integer.parseInt(sc.nextLine().trim());
                    txService.returnBook(rb);
                    break;
                case "5":
                    txService.listUserTransactions(user.getUserId());
                    break;
                case "6":
                    txService.checkDueReminders();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid.");
            }
        }
    }
}
