package db;

public class DbException extends RuntimeException{
    public DbException(String errorMsg){
        super(errorMsg);
    }
}
