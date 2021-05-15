import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;
//ovveride some .equals methods
public class Library{
    /**
     * ArrayList of books, storing Book objects which can be rented or returned by a members.
     */
    private ArrayList<Book> books;
    /**
     * ArrayList of members, storing Member objects which can rent or return books.
     */
    private ArrayList<Member> members;
    /**
     * A attribute used to keep track of what memberNumber the next member constructed should use.
     */
    public static Integer memberNumber;
    /**
     * List of all available commands with associated descriptions.
     */
    public static String HELP_STRING;
    
    /**
     * Constructs a new Library object.
     * 
     * @return nothing
     */
    public Library(){
        this.books=new ArrayList<Book>();
        this.members=new ArrayList<Member>();
        this.memberNumber=100000;
        this.HELP_STRING="EXIT ends the library process\n"+
        "COMMANDS outputs this help string\n\n"+
        "LIST ALL [LONG] outputs either the short or long string for all books\n"+
        "LIST AVAILABLE [LONG] outputs either the short of long string for all available books\n"+
        "NUMBER COPIES outputs the number of copies of each book\n"+
        "LIST GENRES outputs the name of every genre in the system\n"+
        "LIST AUTHORS outputs the name of every author in the system\n\n"+
        "GENRE <genre> outputs the short string of every book with the specified genre\n"+
        "AUTHOR <author> outputs the short string of every book by the specified author\n\n"+
        "BOOK <serialNumber> [LONG] outputs either the short or long string for the specified book\n"+
        "BOOK HISTORY <serialNumber> outputs the rental history of the specified book\n\n"+
        "MEMBER <memberNumber> outputs the information of the specified member\n"+
        "MEMBER BOOKS <memberNumber> outputs the books currently rented by the specified member\n"+
        "MEMBER HISTORY <memberNumber> outputs the rental history of the specified member\n\n"+
        "RENT <memberNumber> <serialNumber> loans out the specified book to the given member\n"+
        "RELINQUISH <memberNumber> <serialNumber> returns the specified book from the member\n"+
        "RELINQUISH ALL <memberNumber> returns all books rented by the specified member\n\n"+
        "ADD MEMBER <name> adds a member to the system\n"+
        "ADD BOOK <filename> <serialNumber> adds a book to the system\n\n"+
        "ADD COLLECTION <filename> adds a collection of books to the system\n"+
        "SAVE COLLECTION <filename> saves the system to a csv file\n\n"+
        "COMMON <memberNumber1> <memberNumber2> ... outputs the common books in members' history";
    }
    
    public static void main(String[] args){
        Library lib=new Library();
        run(lib);
        
        
    }

    /**
     * Main loop for the program, takes in user input and calls corresponding methods.
     */
    public static void run(Library lib){
        Scanner scan=new Scanner(System.in);
        while (scan.hasNext()){
            System.out.print("user: ");
            //splits user input into a string array, ' ' delimited
            String[] temp=scan.nextLine().split(" ");
            if (temp[0].toLowerCase().equals("exit")){
                System.out.println("Ending Library process.");
                return;
            }
            else if (temp[0].toLowerCase().equals("commands")){
                System.out.println(HELP_STRING);
            }
            else if(temp[0].toLowerCase().equals("list")){
                if(temp[1].toLowerCase().equals("all")){
                    if (temp.length==3){
                        if (temp[2].toLowerCase().equals("long")){
                            lib.getAllBooks(true);
                        }
                    } 
                    else{
                        lib.getAllBooks(false);
                    }     
                }
                else if(temp[1].toLowerCase().equals("available")){
                    if (temp.length==3){
                        if (temp[2].toLowerCase().equals("long")){
                            lib.getAvailableBooks(true);
                        }
                    } 
                    else{
                        lib.getAvailableBooks(false);
                    } 
                }
                else if(temp[1].toLowerCase().equals("genres")){
                    lib.getGenres();
                }
                else if(temp[1].toLowerCase().equals("authors")){
                    lib.getAuthors();
                }

            }
            else if(temp[0].toLowerCase().equals("number") && temp[1].toLowerCase().equals("copies")){
                lib.getCopies();
            }
            else if(temp[0].toLowerCase().equals("genre")){
                //If genre is multiple words this loop concatenates those words into a single string.
                String genre="";
                    for (int i=1;i<temp.length;i++){
                        genre+=temp[i]+" ";
                    }
                lib.getBooksByGenre(genre.trim());
            }
            else if(temp[0].toLowerCase().equals("author")){
                //If name is multiple words this loop concatenates those words into a single string.
                String name="";
                    for (int i=1;i<temp.length;i++){
                        name+=temp[i]+" ";
                    }
                lib.getBooksByAuthor(name.trim());
            }
            else if(temp[0].toLowerCase().equals("book")){
                if(temp[1].toLowerCase().equals("history" )){
                    lib.bookHistory(temp[2]);
                }
                else{
                    if (temp.length==3){
                        lib.getBook(temp[1],true);
                    }
                    else{
                        lib.getBook(temp[1],false);
                    }
                }
            }
            else if(temp[0].toLowerCase().equals("member")){
                if(temp.length==2){
                    lib.getMember(temp[1]);
                }
                else if(temp[1].toLowerCase().equals("books")){
                    lib.getMemberBooks(temp[2]);
                }
                else if(temp[1].toLowerCase().equals("history")){
                    lib.memberRentalHistory(temp[2]);
                }
            }
            else if(temp[0].toLowerCase().equals("rent")){
                lib.rentBook(temp[1],temp[2]);
            }
            else if(temp[0].toLowerCase().equals("relinquish")){
                if(temp[1].toLowerCase().equals("all")){
                    lib.relinquishAll(temp[2]);
                }
                else{
                    lib.relinquishBook(temp[1],temp[2]);
                }

            }
            else if(temp[0].toLowerCase().equals("add")){
                if(temp[1].toLowerCase().equals("member")){
                    //If name is multiple words this loop concatenates those words into a single string.
                    String name="";
                    for (int i=2;i<temp.length;i++){
                        name+=temp[i]+" ";
                    }
                    lib.addMember(name.trim());
                }
                else if(temp[1].toLowerCase().equals("book")){
                    //If file name is multiple words this loop concatenates those words into a single string.
                    String file="";
                    for (int i=2;i<temp.length-1;i++){
                        file+=temp[i]+" ";
                    }
                    lib.addBook(file.trim(),temp[temp.length-1]);
                }
                else if(temp[1].toLowerCase().equals("collection")){
                    //If file name is multiple words this loop concatenates those words into a single string.
                    String file="";
                    for (int i=2;i<temp.length;i++){
                        file+=temp[i]+" ";
                    }
                    lib.addCollection(file.trim());
                }
            }
            else if(temp[0].toLowerCase().equals("save")){
                lib.saveCollection(temp[2]);
            }
            else if(temp[0].toLowerCase().equals("common")){
                //Loop reads multiple names and stores them in an array.
                String[] memberSet=new String[temp.length-1];
                for (int i=1;i<temp.length;i++){
                    memberSet[i-1]=temp[i];
                }
                lib.common(memberSet);
            }
            System.out.println();
        }
        
    }
    /**
     * Adds a book to the system by reading it from a csv file.
     * 
     * Invoked by the command "ADD BOOK [file] [serialNumber]", this method reads the given file, searches for the serial number, and then adds the book to the system.
     * If the file does not exist, output "No such file."
     * If the book does not exist within the file, output "No such book in file."
     * If the book's serial number is already present in the system, output "Book already exists in the system."
     * If the book is successfully added, output "Successfully added: [shortstring].
     * 
     * @param bookFile the csv file to read.
     * @param serialNumber the serial number of the book.
     * @return nothing
     */
    public void addBook(String bookFile,String serialNumber){
        //some printing done in book method
        //iterates through books attribute and checks if there is any book in the system with the same serialNumber.
        for (Book x:this.books){
            if(x.getSerialNumber().equals(serialNumber)){
                System.out.println("Book already exists in system.");
                return;
            }
        }
        Book book=Book.readBook(bookFile,serialNumber);
        if (book!=null){
            this.books.add(book);
            System.out.printf("Successfully added: %s.\n",book.shortString());
        }
    }

    /**
     * Adds the collection of books stored in a csv file to the system.
     * 
     * Invoked by the command "ADD COLLECTION [filename]", this method reads all the books from a csv file and adds them to the system.
     * If the file does not exist, output "No such collection."
     * If no books are able to be added (for example, because they all have serial numbers already present in the system), output "No books have been added to the system."
     * If at least one book has been added to the system, output "[number] books successfully added."
     * 
     * @param filename the csv storing the collection of books
     * @return nothing
     */
    public void addCollection(String filename){
        ArrayList<Book> temp=Book.readBookCollection(filename);
        if (temp==null){
            System.out.println("No such collection.");
            return;
        }
        int count=0;
        //each book from the file is checked if it is contained in the library books attribue, if not then add it to the library 
        for (Book x:temp){
            boolean contains=false;
            for (Book y:this.books){
                if (y.getSerialNumber().equals(x.getSerialNumber())){
                    contains=true;
                }
            }
            if (!contains){
                count++;
                this.books.add(x);
            }
        }
        if (count==0){
            System.out.println("No books have been added to the system.");
        }
        else{
            System.out.printf("%d books successfully added.\n",count);
        }

    }

    /**
     * Adds a member to the system.
     * 
     * Invoked by the command "ADD MEMBER [name]", this method creates a new member with the specified name and adds them to the system. The first member will always have member number 100000, and subsequent members will increment from there (so the second member has number 100001).
     * Once the member is successfully added to the system, output "Success."
     * 
     * @param name the name of the member
     * @return nothing
     */
    public void addMember(String name){
        this.members.add(new Member(name,this.memberNumber.toString()));
        //update the attribute which keeps track of the next memberNumber to be added
        this.memberNumber++;
        System.out.println("Success.");

    }
    
    /**
     * Prints out all the member numbers of members who have previously rented a book.
     * 
     * Invoked by the command "BOOK HISTORY [serialNumber]", this method outputs every member that has rented the given book, in the order of rents.
     * If the book does not exist in the system, output "No such book in system."
     * If the book has not been rented, output "No rental history."
     * 
     * @param serialNumber the serial number of the book
     */
    public void bookHistory(String serialNumber){
        boolean found=false;
        ArrayList<Member> history=new ArrayList<Member>();
        for (Book x:this.books){
            if(x.getSerialNumber().equals(serialNumber)){
                history=x.renterHistory();
                found=true;
            }
        }
        if (!found){
            System.out.println("No such book in system.");
            return;
        }
        if (history.size()==0){
            System.out.println("No rental history.");
            return;
        }
        for (Member x:history){
            System.out.println(x.getMemberNumber());
        }

    }

    /**
     * Prints out all the books that all members provided have previously rented.
     * 
     * Invoked by the command "COMMON [member1] [member2] ...", this method lists out the short strings of all the books that have been rented by listed members, ordered by serial number.
     * If there are no members in the system, output "No members in system."
     * If there are no books in the system, output "No books in system."
     * If at least one of the members does not exist, output "No such member in system."
     * If there are duplicate members provided, output "Duplicate members provided."
     * If there are no common books, output "No common books."
     * 
     * @param memberNumbers the array of member numbers
     * @return nothing
     */
    public void common(String[] memberNumbers){
        //for every member we iterate through the array and check that there is not member with the same memberNumber in the array besides the one we are checking. 
        //i.e. check for duplicates
        for (int i=0;i<memberNumbers.length;i++){
            for (int j=0;j<memberNumbers.length;j++){
                if (memberNumbers[i].equals(memberNumbers[j]) && i!=j){
                    System.out.println("Duplicate members provided.");
                    return;
                }
            }
        }
        Member[] memberArray=new Member[memberNumbers.length];
        int index=0;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        //we check if the library object contains members with memberNumber from the arguments
        //the members found are added to an array, if any member is not found we print the below message and return the method
        for (String x:memberNumbers){
            boolean found=false;
            for(Member y:this.members){
                if (y.getMemberNumber().equals(x)){
                    found=true;
                    memberArray[index]=y;
                    index++;
                }
            }
            if (!found){
                System.out.println("No such member in system.");
                return;
            }
        }
        if (Member.commonBooks(memberArray).size()==0){
            System.out.println("No common books.");
            return;
        }
        for (Book x : Member.commonBooks(memberArray)){
            System.out.println(x.shortString());
        }
        
    }

    /**
     * Prints out the formatted strings for all books in the system.
     * 
     * Invoked by "LIST ALL", this method prints out either the short string or long string of all books (depending on whether "LONG" is included in the command), seperated by new lines.
     * If there are no books in the system, output "No books in system."
     */
    public void getAllBooks(boolean fullString){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        if (fullString){
            for (int i=0;i<this.books.size();i++){
                if (i==this.books.size()-1){
                    System.out.println(this.books.get(i).longString());
                }
                else{
                    System.out.println(this.books.get(i).longString()+"\n");
                }  
            }
        }
        else{
            for (Book x:this.books){
                System.out.println(x.shortString());
            }
        }
    }

    /**
     * Prints out all the authors in the system. 
     * 
     * Invoked by "LIST AUTHORS", this method prints out the list of authors in the system. Each author should only be printed once, and they should be printed in alphabetical order.
     * If there are no books, output "No books in system."
     * 
     * @return nothing
     */
    public void getAuthors(){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        ArrayList<String> authors=new ArrayList<String>();
        //iterate through all books in the library and add all distinct author names to authors arraylist
        for (Book x:this.books){
            if (!(authors.contains(x.getAuthor()))){
                authors.add(x.getAuthor());
            }
        }
        //sort the list
        Collections.sort(authors, String.CASE_INSENSITIVE_ORDER);
        for (String x:authors){
            System.out.println(x);
        }

    }

    /**
     * Prints out the formatted strings for all available books in the system.
     * 
     * Invoked by "LIST AVAILABLE", this method prints out either the short string or long string of all available books (depending on whether "LONG" is included in the command), seperated by new lines. Note that a book is available when it is not currently rented by a member.
     * If there are no books in the system, output "No books in system."
     * If there are no available books, output "No books available."
     * 
     * @param fulString whether to print shor or long strings.
     * @return nothing
     */
    public void getAvailableBooks(boolean fullString){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        boolean found=false;
        if (fullString){
            for (int i=0;i<this.books.size();i++){
                if (!(this.books.get(i).isRented())){
                    if (i==this.books.size()-1 ){
                        System.out.println(this.books.get(i).longString());
                    }
                    else{
                        System.out.println(this.books.get(i).longString()+"\n");
                    }
                    found=true;
                }     
            }
        }
        else{
            for (Book x:this.books){
                if (!(x.isRented())){
                    System.out.println(x.shortString());
                    found=true;
                }
            }
        } 
        if (!found){
            System.out.println("No books available.");
        }

    }

    /**
     * Prints either the short or long string of the specified book.
     * 
     * Invoked by the command "BOOK [serialNumber] [LONG]", this method prints out the details for the specified book.
     * If there are no books, output "No books in system."
     * If the book does not exist, output "No such book in system."
     * If fullString is true, output the long string of the book.
     * If fullString is false, output the short string of the book.
     * 
     * @param serialNumber the serial number of the book.
     * @param fullString whether to print short or long string.
     * @return nothing
     */
    public void getBook(String serialNumber, boolean fullString){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        for (Book x:this.books){
            if (x.getSerialNumber().equals(serialNumber)){
                if(fullString){
                    System.out.println(x.longString()); 
                }                   
                else{
                    System.out.println(x.shortString());
                }  
                return;  
                }
            }
        System.out.println("No such book in system.");
    }

    /**
     * Prints all books in the system by the specified author. (case sensitive author)
     * 
     * Invoked by "AUTHOR [author]", this method outputs all the books in the system that were written by the specified author. Each book should have its short string printed on a new line, and books should be ordered by serial number.
     * If there are no books, output "No books in system."
     * If there are no books by the specified author, output "No books by author [author]."
     * 
     * @param author the author to filter by.
     * @return nothing
     */
    public void getBooksByAuthor(String author){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        if (Book.filterAuthor(this.books,author).size()==0){
            System.out.println("No books by "+author+".");
            return;
        }
        for(Book x:Book.filterAuthor(this.books,author)){
            System.out.println(x.shortString());
        }

    }

    /**
     * Prints all books in the system with the specified genre. (duplicates allowed, case sensitive genre)
     * 
     * Invoked by "GENRE [genre]", this method outputs all the books in the system with the specified genre. Each book should have its short string printed on a new line, and books should be ordered by serial number.
     * If there are no books, output "No books in system."
     * If there are no books of the specified genre, output "No books with genre [genre]."
     * 
     * @param genre the genre to filter by.
     */
    public void getBooksByGenre(String genre){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        if (Book.filterGenre(this.books,genre).size()==0){
            System.out.println("No books with genre "+genre+".");
        }
        for(Book x:Book.filterGenre(this.books,genre)){
            System.out.println(x.shortString());
        }
    }

    /**
     * Prints out the number of copies of each book in the system.
     * 
     * Invoked by "NUMBER COPIES", this method prints out the number of copies present of each book, seperated by new lines, sorted lexicographically. Note that books are considered copies if they have the same short string.
     * If there are no books in the system, output "No books in system."
     * Format: "[short string]: [number]"
     * @return nothing
     */
    public void getCopies(){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        HashMap<String,Integer> copies=new HashMap<String,Integer>();
        for (Book x:this.books){
            if (copies.get(x.shortString())==null){
                copies.put(x.shortString(),1);
            }
            else{
                copies.replace(x.shortString(),copies.get(x.shortString())+1);
            }
        }
        //sort keys by serial number
        Set<String> keys=copies.keySet();
        ArrayList<String> keysArrayList=new ArrayList<String>();
        keysArrayList.addAll(keys);
        Collections.sort(keysArrayList);
        //print out sorted books with the amount of copies
        for (String x:keysArrayList){
            System.out.println(x+": "+copies.get(x));
        }
    }

    /**
     * Prints out all the genres in the system.
     * 
     * Invoked by "LIST GENRES", this method prints out the list of genres stored in the system. Each genre should only be printed once, and they should be printed in alphabetical order.
     * If there are no books, output "No books in system."
     * 
     * @return nothing
     */
    public void getGenres(){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        ArrayList<String> genres=new ArrayList<String>();
        for (Book x:this.books){
            if (!(genres.contains(x.getGenre()))){
                genres.add(x.getGenre());
            }
        }
        Collections.sort(genres, String.CASE_INSENSITIVE_ORDER);
        for (String x:genres){
            System.out.println(x);
        }

    }

    /**
     * Prints the details of the specified member.
     * 
     * Invoked by the command "MEMBER [memberNumber]", this method outputs the details of the specified member.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member exists, output "[memberNumber]: [member name]"
     * 
     * @param memberNumber the member's member number.
     * @return nothing
     */
    public void getMember(String memberNumber){
        boolean foundMember=false;
        Member member=null;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        for(Member x:this.members){
            if (x.getMemberNumber().equals(memberNumber)){
                foundMember=true;
                member=x;
            }
        }
        if (!foundMember){
            System.out.println("No such member in system.");
            return;
        }
        System.out.printf("%s: %s\n",memberNumber,member.getName());   

    }

    /**
     * Prints a list of all the books a member is currently renting.
     * 
     * Invoked by the command "MEMBER BOOKS [memberNumber]", this method prints out all the books the specified member is currently renting, in the order that they were rented. Each book should have its short string printed on a new line.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member is not renting any books, output "Member is not currently renting."
     * 
     * @param memberNumber the member's member number.
     * @return nothing
     */
    public void getMemberBooks(String memberNumber){
        boolean foundMember=false;
        Member member=null;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        for(Member x:this.members){
            if (x.getMemberNumber().equals(memberNumber)){
                foundMember=true;
                member=x;
            }
        }
        if (!foundMember){
            System.out.println("No such member in system.");
            return;
        }
        if (member.renting().size()==0){
            System.out.println("Member not currently renting.");
            return;
        }
        for (Book x:member.renting()){
            System.out.println(x.shortString());
        }


    }

    /**
     * Prints a list of all the books a member has previously rented.
     * 
     * Invoked by the command "MEMBER HISTORY [memberNumber]", this method prints out all the books the specified member has previously rented, in the order that they were rented. Each book should have its short string printed on a new line.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member has not rented any books, output "No rental history for member."
     * 
     * @param memberNumber the member's member number.
     * @return nothing 
     */
    public void memberRentalHistory(String memberNumber){
        boolean foundMember=false;
        Member member=null;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        for(Member x:this.members){
            if (x.getMemberNumber().equals(memberNumber)){
                foundMember=true;
                member=x;
            }
        }
        if (!foundMember){
            System.out.println("No such member in system.");
            return;
        }
        if (member.history().size()==0){
            System.out.println("No rental history for member.");
            return;
        }
        for (Book x:member.history()){
            System.out.println(x.shortString());
        }


    }

    /**
     * Makes a member return all books they are currently renting. (no specific order of returning)
     * 
     * Invoked by the command "RELINQUISH ALL [memberNumber]", this method causes the specified member to return all books they are currently renting.
     * If there are no members in the system, output "No members in system."
     * If the member does not exist, output "No such member in system."
     * If the member does exist, return all books and output "Success."
     * 
     * @param memberNumber the member's member number.
     * @return nothing 
     */
    public void relinquishAll(String memberNumber){
        boolean foundMember=false;
        Member member=null;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        for(Member x:this.members){
            if (x.getMemberNumber().equals(memberNumber)){
                foundMember=true;
                member=x;
            }
        }
        if (!foundMember){
            System.out.println("No such member in system.");
            return;
        }
        member.relinquishAll();
        System.out.println("Success.");

    }

    /**
     * Returns a book to the system.
     * 
     * Invoked by the command "RELINQUISH [memberNumber] [serialNumber]", this method gets the specified member to return the given book back to the system.
     * If there are no members in the system, output "No members in system."
     * If there are no books in the system, output "No books in system."
     * If the member does not exist, output "No such member in system."
     * If the book does not exist, output "No such book in system."
     * If the book is not being loaned out by the member, output "Unable to return book."
     * If the book is successfully return, output "Success."
     * 
     * @param memberNumber the member's member number.
     * @param serialNumber the book's serial number.
     * @return nothing
     */
    public void relinquishBook(String memberNumber,String serialNumber){
        boolean foundBook=false;
        boolean foundMember=false;
        Book book=null;
        Member member=null;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        
        for(Book x:this.books){
            if (x.getSerialNumber().equals(serialNumber)){
                foundBook=true;
                book=x;
            }
        }
        if (!foundBook){
            System.out.println("No such book in system.");
            return;
        }
        for(Member x:this.members){
            if (x.getMemberNumber().equals(memberNumber)){
                foundMember=true;
                member=x;
            }
        }
        if (!foundMember){
            System.out.println("No such member in system.");
            return;
        }
        if (!member.relinquish(book)){
            System.out.println("Unable to return book.");
            return;
        }
        System.out.println("Success.");


    }

    /**
     * Loans out a book to a member within the system.
     * 
     * Invoked by the command "RENT [memberNumber] [serialNumber]", this method loans out the specified book to the given member.
     * If there are no members in the system, output "No members in system."
     * If there are no books in the system, output "No books in system."
     * If the member does not exist, output "No such member in system."
     * If the book does not exist, output "No such book in system."
     * If the book is already being loaned out, output "Book is currently unavailable."
     * If the book is successfully loaned out, output "Success."
     * 
     * @param memberNumber the member's member number.
     * @param serialNumber the book's serial number.
     * @return nothing
     */
    public void rentBook(String memberNumber, String serialNumber){
        boolean foundBook=false;
        boolean foundMember=false;
        Book book=null;
        Member member=null;
        if (this.members.size()==0){
            System.out.println("No members in system.");
            return;
        }
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        
        for(Book x:this.books){
            if (x.getSerialNumber().equals(serialNumber)){
                foundBook=true;
                book=x;
            }
        }
        if (!foundBook){
            System.out.println("No such book in system.");
            return;
        }
        for(Member x:this.members){
            if (x.getMemberNumber().equals(memberNumber)){
                foundMember=true;
                member=x;
            }
        }
        if (!foundMember){
            System.out.println("No such member in system.");
            return;
        }
        if (!member.rent(book)){
            System.out.println("Book is currently unavailable.");
            return;
        }
        System.out.println("Success.");

    }

    /**
     * Saves the current collection of books in the system to a csv file.
     * 
     * Invoked by the command "SAVE COLLECTION [filename]", this method saves all the books stored in the system to a csv file (in the same format as can be read with "ADD COLLECTION [filename").
     * If there are no books in the system, output "No books in system."
     * If there are books, write them to the file and output "Success."
     * 
     * @param filename the csv file to write the collection to.
     * @return nothing
     */
    public void saveCollection(String filename){
        if (this.books.size()==0){
            System.out.println("No books in system.");
            return;
        }
        Book.saveBookCollection(filename,this.books);
        System.out.println("Success.");
    }
    
}