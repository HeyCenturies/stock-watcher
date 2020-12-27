package vitor.stockwatcher.stockwatcher.Models.Exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message) {
        super(message);
    }
}
