package CustomExceptions;

public class EmptyTextFieldException extends Exception{

    public EmptyTextFieldException(){
        super("Not all fields are filled.");
    }
}
