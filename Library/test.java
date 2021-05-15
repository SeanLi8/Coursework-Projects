import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class test{
    public static void main(String[] args){
        testReadBook();
        testReadBookCollection();
        testSaveBookCollection();
    }
    public static void testReadBook(){
        Book testBook=Book.readBook("Assignment 1 - Sample Collection.csv","111112");
        try{
            boolean condition="111112".equals(testBook.getSerialNumber()) &&
            "The Hobbit".equals(testBook.getTitle()) &&
            "J.R.R. Tolkien".equals(testBook.getAuthor()) &&
            "Fantasy".equals(testBook.getGenre());

            if (condition){
                System.out.println("Passed readBook: valid input");
            }
            else{
                System.out.printf("Failed readBook: expected 111112,The Hobbit,J.R.R. Tolkien,Fantasy got %s,%s,%s,%s\n"
                ,testBook.getSerialNumber(),testBook.getTitle(),testBook.getAuthor(),testBook.getGenre());
            }
        }
        catch (NullPointerException e){
            System.out.println("Failed readBook: book attribute was null");
            return;     
        }
        Book testBookNull=Book.readBook("Assignment 1 - Sample Collection.csv","123422");
        if (testBookNull==null){
            System.out.println("Passed readBook: serial number not found");
        }
        else{
            System.out.println("Failed readBook: serial number not found, expected null");
        }
        Book testBookFNF=Book.readBook("not a file","111112");
        if (testBookFNF==null){
            System.out.println("Passed readBook: File not found ");
        }
        else{
            System.out.println("Failed readBook: File not found, expected null");
        }

    }

    public static void testReadBookCollection(){
        ArrayList<Book> testBookCollection=Book.readBookCollection("Assignment 1 - Sample Collection.csv");
        if (testBookCollection==null){
            System.out.println("Failed readBookCollection: valid input, got null");
            return;
        }
        else if (testBookCollection.size()!=17){
            System.out.printf("Failed readBookCollection: valid input, expected 17 elements got%d\n",testBookCollection.size());
            return;
        }
        else{
            try{
                File f=new File("Assignment 1 - Sample Collection.csv");
                Scanner scan=new Scanner(f);  
                ArrayList<String[]> orderActual=new ArrayList<String[]>();
                boolean firstLine=true;
                while (scan.hasNext()){
                    if (firstLine){
                        firstLine=false;
                        scan.nextLine();
                        continue;
                    }
                    orderActual.add(scan.nextLine().split(","));
                    
                }

                for (int i=1;i<testBookCollection.size();i++){
                    if(!testBookCollection.get(i).getSerialNumber().equals(orderActual.get(i)[0])){
                        System.out.println("Failed readBookCollection: valid input, order incorrect");
                        return;
                    }         
                }
                System.out.println("Passed readBookCollection: valid input");   
            } 
            catch (FileNotFoundException e){
                return;
            }
            
        }
        ArrayList<Book> testBookCollectionFNF=Book.readBookCollection("not a file");
        if (testBookCollectionFNF==null){
            System.out.println("Passed readBookCollection: File not found ");
        }
        else{
            System.out.println("Failed readBookCollection: File not found, expected null");
        }

    }

    public static void testSaveBookCollection(){
        ArrayList<Book> testBookCollection=new ArrayList<Book>();
        testBookCollection.add(new Book("Harry Potter 6","J.K. Rowling","Fantasy","111999"));
        testBookCollection.add(new Book("Harry Potter 6","J.K. Rowling","Fantasy","111339"));
        testBookCollection.add(new Book("Harry Potter 5","J.K. Rowling","Fantasy","111122"));
        testBookCollection.add(new Book("Harry Potter 6","J.K. Rowling","Fantasy","111123"));
        testBookCollection.add(new Book("Harry Potter 7","J.K. Rowling","Fantasy","111555"));
        
        testBookCollection.add(new Book("Wasteland","T.S Elliot","Poetry","111124"));
        testBookCollection.add(new Book("Biggles in the Battle","W.E. Johns","War","111125"));

        Book.saveBookCollection("TestSaveBookCollection.csv",Book.filterAuthor(testBookCollection,"J.K. Rowling"));

    }



}