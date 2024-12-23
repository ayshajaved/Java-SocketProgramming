package SMSSocketApp;
//CLASS CONTACT FOR THE FUTURE USE IN THE PROGRAM, WHEN WE WILL BE DEALING WITH MULTIPLE CLIENTS
public class Contact {
    private String name;
    private String email;
    Address address;
    public Contact(String name, String email, Address address) {
        if(name.isEmpty() || email.isEmpty() || name.trim().isEmpty() || email.trim().isEmpty()){
            throw new IllegalArgumentException("Name or email cannot be empty");
        }
        setName(name);
        setEmail(email);
        setAddress(address);
    }
    public String getName() {
        return name;
    }   
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Address getAddress() {
        return address;
    }
    public void setAddress(Address address) {
        this.address = address;
    }
    @Override
    public String toString() {
        System.out.println("--------");
        return String.format("Name: %s\nEmail: %s\nAddress: \n%s \n", name, email, address);
}
}
