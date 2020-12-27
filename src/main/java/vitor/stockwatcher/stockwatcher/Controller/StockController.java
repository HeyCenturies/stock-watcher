package vitor.stockwatcher.stockwatcher.Controller;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import vitor.stockwatcher.stockwatcher.Models.AlphaVantageConfig;
import vitor.stockwatcher.stockwatcher.Models.StockValue;
import vitor.stockwatcher.stockwatcher.Models.Exception.StockNotFoundException;

@Controller
@RequestMapping(value = "/stock")
public class StockController {

    @Autowired
    AlphaVantageConfig alphaVantageConfig;

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping(value = "{stockName}")
    @ResponseBody
    public StockValue getStockInformation(@PathVariable String stockName) throws IOException {
        
        String api_key = alphaVantageConfig.getApiKey();
        /*TRY NASDAQ, IF IT FAILS, TRY BOVESPA*/
        try{
            return getStockInformationNASDAQ(stockName, api_key);
        } catch (StockNotFoundException e){
            return null;
        }
        
    }


    private StockValue getStockInformationNASDAQ(String stockName, String api_key) throws StockNotFoundException,
            IOException {

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+stockName+"&interval=5min&apikey="+api_key;
        Request request = new Request.Builder().url(url).build();
        String response = OK_HTTP_CLIENT.newCall(request).execute().body().string();
        JsonNode jsonNode = objectMapper.readTree(response);
        
        if(jsonNode.has("Time Series (5min)")){
            Iterator<JsonNode> stockIterator = jsonNode.get("Time Series (5min)").elements();
            if(stockIterator.hasNext()){
                JsonNode stockValues = stockIterator.next();
                String open = stockValues.get("1. open").toString().replaceAll("\"", "");
                String high = stockValues.get("2. high").toString().replaceAll("\"", "");
                String low = stockValues.get("3. low").toString().replaceAll("\"", "");
                String close = stockValues.get("4. close").toString().replaceAll("\"", "");
                String volume = stockValues.get("5. volume").toString().replaceAll("\"", "");

                return StockValue.builder()
                .open(Double.parseDouble(open))
                .high(Double.parseDouble(high))
                .low(Double.parseDouble(low))
                .close(Double.parseDouble(close))
                .volume(Double.parseDouble(volume))
                .build();
            }
        }
        throw new StockNotFoundException("Stock not found");
    }
}
