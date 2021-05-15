import java.util.ArrayList;
public class Member{
    private String memberNumber;
    private String name;
    /**
     * a list to keep track of books that the member is renting.
     */
    private ArrayList<Book> currentlyRenting;
    /**
     * a list to keep track of books that member has relinquished/returned
     */
    private ArrayList<Book> renterHistory;

    /**
     * Constructs a new Member object given a name and member number.
     * 
     * @param name the name of the new member.
     * @param memberNumber the member number of the new member.
     * @return nothing
     */
    public Member(String name, String memberNumber){
        this.memberNumber=memberNumber;
        this.name=name;
        this.currentlyRenting=new ArrayList<Book>();
        this.renterHistory=new ArrayList<Book>();

    }

    /**
     * Returns the intersection of the members' histories, ordered by serial number.
     * If members is invalid, return null.
     * Otherwise, return the sorted intersection of books.
     * 
     * @param members the array of members.
     * @return the sorted intersection of books.
     * 
     */
    public static ArrayList<Book> commonBooks(Member[] members){
        if (members==null){
            return null;
        }
        else{
            for (Member x:members){
                if (x==null){
                    return null;
                }
            }
            ArrayList<Book> booksIntersection=new ArrayList<Book>();
            //for every book in the history of the first member in the array, check if it is contained in every other member's history
            //if it is then add book to the bookIntersection list i.e. a duplicate do not add it
            for (Book x:members[0].history()){
                boolean common=true;
                boolean duplicate=false;
                //loop checks for duplicates
                for (Book y:booksIntersection){
                    if (y.shortString().equals(x.shortString())){
                        duplicate=true;
                        break;
                    }
                }
                if (duplicate){
                    continue;
                }
                //checks if the book is common for all member histories
                for (int i=1;i<members.length;i++){
                    if(!(members[i].history().contains(x))){
                        common=false;
                        break;
                    }                    
                }   
                if (common){
                    booksIntersection.add(x);
                }
            }
            return booksIntersection;
        }    
    }

    /**
     * Returns the member number of the member.
     * 
     * @return the member number of the member
     */
    public String getMemberNumber(){
        return this.memberNumber;

    }

    /**
     * Returns the name of the member.
     * 
     * @return the name of the member
     */
    public String getName(){
        return this.name;

    }

    /**
     * Returns the history of books rented, in the order they were returned.
     * Books currently being rented are not included in this list.
     * 
     * @return the list of book that gave been rented
     */
    public ArrayList<Book> history(){
        return this.renterHistory;
    }

    /**
     * Returns the book to the library.
     * If the book doesn't exist, or the member isn't renting the book, return false.
     * Otherwise, set the renter of the book to null, add it to the rental history, and return true.
     * 
     * @param book the book to return
     * @return the outcome of the rental transaction.
     * 
     */
    public boolean relinquish(Book book){
        if (book==null){
            return false;
        }
        boolean temp=book.relinquish(this);
        if (temp){
            this.renterHistory.add(book);
            this.currentlyRenting.remove(book);
        }
        return temp;
    }

    /**
     * Returns all books rented by the member.
     * 
     * @return nothing
     */
    public void relinquishAll(){
        for (Book x:this.currentlyRenting){
            x.relinquish(this);
            this.renterHistory.add(x);
        }
        this.currentlyRenting.clear();
        return;
    }

    /**
     * Rents the given book.
     * 
     * If the book does not exist or it is already being rented, do nothing and return false.
     * Otherwise, set the renter of the book to this instance of member and return true.
     * 
     * @param book the book to rent.
     * @return the outcome of the rental transaction.
     */
    public boolean rent(Book book){
        if (book==null){
            return false;
        }
        boolean temp=book.rent(this);
        if (temp){
            this.currentlyRenting.add(book);
        }
        return temp;

    }

    /**
     * Returns the list of books currently being rented, in the order they were rented.
     * 
     * @return the list of books currently being rented.
     */
    public ArrayList<Book> renting(){
        return this.currentlyRenting;
    }

    
}