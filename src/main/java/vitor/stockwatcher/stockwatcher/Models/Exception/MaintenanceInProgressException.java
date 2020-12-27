package vitor.stockwatcher.stockwatcher.Models.Exception;

public class MaintenanceInProgressException extends RuntimeException{
    public MaintenanceInProgressException(String message) {
        super(message);
    }
}
