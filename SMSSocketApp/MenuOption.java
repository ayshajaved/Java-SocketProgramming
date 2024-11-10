package SMSSocketApp;
public enum MenuOption{
        SEND(1),     //calling constructor to assign 1 to send
        DISPLAY(2),
        FIND(3),
        EDIT(4),
        DELETE(5),
        SORT_BY_ID(6),
        SORT_BY_TIME(7),
        SORT_BY_CONTENT(8),
        EXIT(9);
    //constructor to assign a value to the constant of the enum. We can also assign string value to the constant
        MenuOption(int value){
        this.value = value; // Initialize the 'value' for each enum constant
    }
    private int value;//private variable value that assigns integer value to each constant
    public int getValue(){
        return value;
    }
    //method to get the constant from the int value
    public static MenuOption getValueOf(int value){
        for(MenuOption option: values()){
            if(option.getValue() == value){
                return option;
            }
        }
        return null;
    }
    
}
