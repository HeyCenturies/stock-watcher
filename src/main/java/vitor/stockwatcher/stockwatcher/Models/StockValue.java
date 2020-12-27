package vitor.stockwatcher.stockwatcher.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockValue {    
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
}
