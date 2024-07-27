package db;

import util.TextColor;

public class DbException extends RuntimeException{
    public DbException(String errorMsg){
        super(TextColor.formatToRed(errorMsg));
    }
}
