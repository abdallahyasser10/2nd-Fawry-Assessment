import java.util.*;

abstract class Book {
    //some details lik ISBN(just any string identifier), title, year when the book is published, price

    protected String identifier;
    protected String title;
    protected int published;
    protected double price;
    protected String author;

    public Book(String identifier, String title, int published, double price, String author) {
        this.identifier = identifier;
        this.title = title;
        this.published = published;
        this.price = price;
        this.author = author;
    }

    public String getidentifier() {
        return identifier;
    }

    public int getpublished() {
        return published;
    }

    public double getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public abstract boolean ispurchasable();
}

class Paperbook extends Book{
    // have stock and may be shipped
    private int stock;
    public Paperbook(String isbn, String title, int year, double price, String author, int stock) {
        super(isbn, title, year, price, author);
        this.stock = stock;
    }

    public void reducestock( int quant){
        if (stock >= quant)
        {
            stock -= quant;
        }else
        {
            throw new IllegalArgumentException("Not enough stock.");
        }
    }

    public int getStock() {
        return stock;
    }

    public boolean ispurchasable() {
        return true;
    }
}
class Ebook extends Book{
    //have a filetype and may be sent via email
    private String Filetype ;

    public Ebook(String identifier, String title, int published, double price, String author, String filetype) {
        super(identifier, title, published, price, author);
        Filetype = filetype;
    }

    public String getFiletype() {
        return Filetype;
    }

    public void setFiletype(String filetype) {
        Filetype = filetype;
    }
    public boolean ispurchasable() {
        return true;
    }
}
class Demobook extends Book{
    //is not for sale


    public Demobook(String identifier, String title, int published, double price, String author) {
        super(identifier, title, published, price, author);
    }

    public boolean ispurchasable() {
        return false;
    }

}

class ShippingService {
    public static void ship(String address) {
        System.out.println("Quantum book store: Shipping to " + address);
    }
}

class MailService {
    public static void email(String email) {
        System.out.println("Quantum book store: Sending eBook to " + email);
    }
}
class QuantumBookstore {
    private Map<String, Book> inventory = new HashMap<>();

    public void addBook(Book book) {
        inventory.put(book.getidentifier(), book);
        System.out.println("Quantum book store: Added book: " + book.getTitle());
    }

    public List<Book> removeoutdatedbooks(int maxyears) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Book> removed = new ArrayList<>();
        Iterator<Map.Entry<String, Book>> iterator = inventory.entrySet().iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next().getValue();
            if (currentYear - book.getpublished() > maxyears){
                removed.add(book);
                iterator.remove();
                System.out.println("Quantum book store: Removed outdated book: " + book.getTitle());
            }

        }
        return removed;
    }

    public double buyBook(String identifier, int quantity, String email, String address) {
        Book book = inventory.get(identifier);
        if (book == null){
            throw new IllegalArgumentException("Book not found.");
        }
        if (book.ispurchasable() == false){
            throw new IllegalArgumentException("Book is not for sale.");
        }
        double totalCost = book.getPrice() * quantity;

        if (book instanceof Paperbook paperBook) {
            if (paperBook.getStock() < quantity)
                throw new IllegalArgumentException("Not enough stock.");
            paperBook.reducestock(quantity);
            ShippingService.ship(address);
        } else if (book instanceof Ebook) {
            MailService.email(email);
        }

        System.out.println("Quantum book store: Purchased " + quantity + "x " + book.getTitle() + " for " + totalCost);
        return totalCost;
    }

    }



public class Main {
    public static void main(String[] args) {

        QuantumBookstore store = new QuantumBookstore();

        store.addBook(new Paperbook("PB1", "Java", 2015, 50.0, "John", 10));
        store.addBook(new Ebook("EB1", "Clean Code", 2018, 30.0, "Robert", "PDF"));
        store.addBook(new Demobook("DB001", "python", 2010, 0.0, "Albert"));

        store.removeoutdatedbooks(10);

        try {
            store.buyBook("PB1", 2, "user@example.com", "123 Cairo St");
            store.buyBook("EB1", 1, "user@example.com", "");
            store.buyBook("DB1", 1, "user@example.com", "");
        } catch (Exception e) {
            System.out.println("Quantum book store: Error - " + e.getMessage());
        }

    }
}