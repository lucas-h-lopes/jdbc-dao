package model.exceptions;

import util.TextColor;

public class ConsoleManagerException extends Exception{
    public ConsoleManagerException(String msg){
        super(TextColor.formatToRed(msg));
    }
}
