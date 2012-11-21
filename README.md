S&P500 index overbought/sold indicator
--------------------------------------
Calculates the number of S&P500 stocks under 50 day moving average.
When the indicator value is over 80%, it could indicate that the 
market is oversold and there could be a good opportunity to buy, 
likewise when the indicator is under 20% there could be a good opportunity
to sell.
Stock data is downloaded from Yahoo Finance. SQLite is used as a database to
store the stock data.


config.txt : The date when the database was last updated

sp500.db : Database file

SP500.txt : S&P500 stock symbol file

How to use
-----------
- Copy config.txt, sp500.db, SP500.txt and SP500Indicator into same directory
- Run the .jar file
- Program will automatically update the stock data and calculate the new indicator values and
  draw the graph

If the stock data is not updated regularly, it will take a considerable amount of time to calculate the new
indicator values !