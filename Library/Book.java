import java.util.Collection;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.List;

public class Book{
    private String serialNumber;
    private String title;
    private String author;
    private String genre;
    /**
     * attribute is null if no book is not being rented, if it is then attribute will be a reference to a member object
     */
    private Member memberRenting;
    /**
     * list keeping track of members who have relinquished/returned the book 
     */
    private ArrayList<Member> rentarHistory;
    
    /**
     * Constructs a new Book object. The instance stores the parameters as its own properties.
     * @param title the name of the book.
     * @param author the author of the book.
     * @param genre the genre of the book.
     * @param serialNumber  The serial number for this instance of the book. There can be multiple books with the same title/author/genre, however each have a unique serial number .
     * @return nothing 
     */
    public Book(String title,String author,String genre,String serialNumber){
        this.serialNumber=serialNumber;
        this.title=title;
        this.author=author;
        this.genre=genre;
        this.rentarHistory=new ArrayList<Member>();

    }
    /**
     * Sort a list of books according to serialNumber attribute in ascending order.
     * @param books the list of books to be sorted.
     * @return a list of books sorted by serialNumber in ascending order.
     */
    public static List<Book> sort(List<Book> books){
        ArrayList<Book> out=new ArrayList<Book>();
        Integer lowest=Integer.MIN_VALUE;
        // loops through books n times, where n is amount of books in the books attribute
        // each iteration the lowest number greater than the last lowest is found and stored in the out arraylist and also stored in the lowest variable
        for (int i=0;i<books.size();i++){
            Integer temp=Integer.MAX_VALUE;
            int index=0;
            for (int j=0;j<books.size();j++){ 
                if (Integer.parseInt(books.get(j).serialNumber)>lowest && Integer.parseInt(books.get(j).serialNumber)<temp){
                    temp=Integer.parseInt(books.get(j).serialNumber);
                    index=j;
                }
            }
            lowest=temp;
            out.add(books.get(index));
        }
        return out;
    }
    
    /**
     * Creates a new list containing books by the specified author.
     * If the list or author does not exist, return null.
     * If they do exist, create a new list with all the books written by the given author, sort by serial number, and return the result.
     * 
     * @param books the list of books to filter.
     * @param author the author to filter by.
     * @return the filtered list of books.
     */
    public static List<Book> filterAuthor(List<Book> books,String author){
        if (books==null || author==null){
            return null;
        }
        List<Book> filteredList=new ArrayList<Book>();
        for (Book x:books){
            if (x.getAuthor().equals(author)){
                filteredList.add(x);
            }
        }
        
        return sort(filteredList);
    }
    /**
     * Creates a new list containing books by the specified genre.
     * If the list or genre does not exist, return null.
     * If they do exist, create a new list with all the books in the specified genre, sort by serial number, and return the result.
     * 
     * @param books the list of books to filter.
     * @param genre the genre to filter by.
     *  @return the filtered list of books.
     */
    public static List<Book> filterGenre(List<Book> books,String genre){
        if (books==null || genre==null){
            return null;
        }
        List<Book> filteredList=new ArrayList<Book>();
        for (Book x:books){
            if (x.getGenre().equals(genre)){
                filteredList.add(x);
            }
        }
        

        return sort(filteredList);
    }
    /**
     * Retrieves the book from the given file based on its serial number.
     * If the file or given book doesn't exist, return null.
     * If the file does exist, retrieve the information for the book with the given serial number, and return the newly created book.
     * 
     * @param filename the csv file containing a book collection.
     * @param serialNumber the serial number for the book.
     * @return the Book object created based on the file.
     */
    public static Book readBook(String filename,String serialNumber){
        if(filename==null || serialNumber==null){
            return null;
        }
        try{
            File f=new File(filename);
            Scanner scan=new Scanner(f);  
            ArrayList<String[]> books=new ArrayList<String[]>();
            while (scan.hasNext()){
                books.add(scan.nextLine().split(","));
            } 
            int foundAt=0;
            for (int i=1;i<books.size();i++){
                if (books.get(i)[0].equals(serialNumber)){
                    foundAt=i;
                }
            }
            if (foundAt==0){
                System.out.println("No such book in file.");
                return null;
            }
            else{
                return new Book(books.get(foundAt)[1],books.get(foundAt)[2],books.get(foundAt)[3],books.get(foundAt)[0]);
            }
        }
        catch (FileNotFoundException e){
            System.out.println("No such file.");
            return null;
        }
        

    }
    /**
     * Reads in the collection of books from the given file.
     * If the file or given book doesn't exist, return null.
     * If the file exists, traverse through the csv and create a new book object for each line. 
     * The method should then return the list of books in the same order they appear in the csv file.
     * 
     * @param filename the csv to read.
     * @return the collection of books stored in the csv file.
     */
    public static ArrayList<Book> readBookCollection(String filename){
        if(filename==null){
            return null;
        }
        try{
            File f=new File(filename);
            Scanner scan=new Scanner(f);  
            ArrayList<Book> books=new ArrayList<Book>();
            boolean firstLine=true;
            while (scan.hasNext()){
                if (firstLine){
                    firstLine=false;
                    scan.nextLine();
                    continue;
                }
                String[] temp=scan.nextLine().split(",");
                Book tempBook=new Book(temp[1],temp[2],temp[3],temp[0]);
                books.add(tempBook);
            }  
            return books; 
        }
        catch (FileNotFoundException e){
            return null;
        }
        
    } 
    /**
     * Save the collection of books to the given file.
     * If the file or collection doesn't exist, do nothing.
     * Otherwise, write the collection to file, ensuring that the file maintains the csv format.
     * 
     * @param filename the csv file to write to.
     * @param books the collection of books to write to a file.
     * @return nothing
     */
    public static void saveBookCollection(String filename, Collection<Book> books){
        if (filename==null || books==null){
            return;
        }
        File f=new File(filename);
        try{
            PrintWriter writer=new PrintWriter(f);
            writer.println("serialNumber,title,author,genre");
            for (Book x:books){
                Book temp=x;
                writer.printf("%s,%s,%s,%s\n",temp.getSerialNumber(),temp.getTitle(),temp.getAuthor(),temp.getGenre());  
            }
            writer.close();
        }
        catch (FileNotFoundException e){
            return;
        }
    }
    /**
     * Returns the author of the book.
     * 
     * @return the title of the book.
     */
    public String getAuthor(){
        return this.author;

    }
    /**
     * Returns the genre of the book.
     * 
     * @return the genre of the book.
     */
    public String getGenre(){
        return this.genre;

    }
    /**
     * Returns the serialNumber of the book.
     * 
     * @return the serialNumber of the book.
     */
    public String getSerialNumber(){
        return this.serialNumber;

    }
    /**
     * Returns the title of the book.
     * 
     * @return the title of the book.
     */
    public String getTitle(){
        return this.title;

    }
    /**
     * Returns whether the book is currently being rented by a member of the library.
     * 
     * @return whether the book is currently rented
     */
    public boolean isRented(){
        if (this.memberRenting==null){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * Formats the Book object to create the long for of its toString().
     * Format:
     * If the book is rented: [serialNumber]: [title] ([author], [genre])\nRented by [renter number].
     * If the book is available: [serialNumber]: [title] ([author], [genre])\nCurrently available.
     * 
     * @return the extended string
     */
    public String longString(){
        if (this.isRented()){
            return this.serialNumber+": "+this.title +" (" +this.author+", "+this.genre+")\nRented by: "+this.memberRenting.getMemberNumber()+".";
        }
        else{
            return this.serialNumber+": "+this.title +" (" +this.author+", "+this.genre+")\nCurrently available.";
        }
    }
    /**
     * Formats the Book object to create the short form of its toString().
     * Format:
     * [title] ([author])
     * 
     * @return the shortened String.
     */
    public String shortString(){
        return this.title +" (" +this.author+")";
    }
    /**
     * Returns the book to the library.
     * If the member does not exist or isn't the current renter, do nothing and return false.
     * If the book is rented by the member, change the current renter and return true.
     * 
     * @param member the member returning the book.
     * @return the outcome of the rental transaction.
     */
    public boolean relinquish(Member member){
        if (member==null || member!=this.memberRenting){
            return false;
        }
        else{
            this.rentarHistory.add(member);
            this.memberRenting=null;
            return true;
        }
    }
    /**
     * Sets the current renter to be the given member.
     * If the member does not exist, or the book is already being rented, do nothing and return false.
     * If the book is able to be rented, return true.
     * 
     * @param member the new person renting the book.
     * @return the outcome of the rental transaction.
     */
    public boolean rent(Member member){
       if (member==null || this.memberRenting!=null){
           return false;
       }
       else{
           this.memberRenting=member;
           return true;
       }
    }
    /**
     * Returns the renter history,in chronological order.
     * 
     * @return the list of members who have rented this book.
     */
    public ArrayList<Member> renterHistory(){
        return this.rentarHistory;
    }
    
    

}