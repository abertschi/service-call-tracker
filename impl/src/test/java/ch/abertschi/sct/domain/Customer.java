package ch.abertschi.sct.domain;

public class Customer {
    
    private String name;

    private int yearOfBirth;

    private String comment;

    public Customer(String name, String comment) {
        this.name = name;
        setComment(comment);
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
