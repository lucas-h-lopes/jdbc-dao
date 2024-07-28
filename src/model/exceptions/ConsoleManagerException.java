package model.exceptions;

import util.TextColor;

public class ConsoleManagerException extends RuntimeException{
    public ConsoleManagerException(String msg){
        super(TextColor.formatToRed(msg));
    }
}
