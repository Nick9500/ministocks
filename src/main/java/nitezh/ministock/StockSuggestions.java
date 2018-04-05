/*
 The MIT License

 Copyright (c) 2013 Nitesh Patel http://niteshpatel.github.io/ministocks

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

package nitezh.ministock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nitezh.ministock.utils.Cache;
import nitezh.ministock.utils.StorageCache;
import nitezh.ministock.utils.UrlDataTools;


class StockSuggestions {

    private static final String BASE_URL = "https://s.yimg.com/aq/autoc?callback=YAHOO.Finance.SymbolSuggest.ssCallback&region=US&lang=en-US&query=";
    private static final Pattern PATTERN_RESPONSE = Pattern.compile("YAHOO\\.Finance\\.SymbolSuggest\\.ssCallback\\((\\{.*?\\})\\)");
    private static final String CRYPTO_BASE_URL = "https://api.coinmarketcap.com/v1/ticker/";

    static List<Map<String, String>> getSuggestions(String query) {
        final Pattern STOCK_REGEX = Pattern.compile("^" + query + "[A-Z0-9]*", Pattern.CASE_INSENSITIVE);

        List<Map<String, String>> suggestions = new ArrayList<>();
        String cryptoResponse, stockResponse;

        String cryptoUrl = CRYPTO_BASE_URL;
        cryptoResponse = UrlDataTools.getUrlData(cryptoUrl);

        try {
            String stockUrl = BASE_URL + URLEncoder.encode(query, "UTF-8");
            Cache stockCache = new StorageCache(null);
            stockResponse = UrlDataTools.getCachedUrlData(stockUrl, stockCache, 86400);

        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            stockResponse = null;
        }

        // Return if empty response
        if (stockResponse == null || stockResponse.equals("") ||
                cryptoResponse == null || cryptoResponse.equals("")) {
            return suggestions;
        }

        try {
            JSONArray cryptoJson = new JSONArray(cryptoResponse);

            for (int i = 0; i < cryptoJson.length(); i++) {

                String symbol = cryptoJson.getJSONObject(i).getString("symbol");

                Matcher cryptoMatcher = STOCK_REGEX.matcher(symbol);
                if (cryptoMatcher.matches() && query.length() > 0) {
                    Map<String, String> suggestion = new HashMap<>();
                    suggestion.put("symbol", symbol);
                    suggestion.put("name", cryptoJson.getJSONObject(i).getString("name"));
                    suggestions.add(suggestion);
                }
            }

            Matcher m = PATTERN_RESPONSE.matcher(stockResponse);
            if (m.find()) {
                stockResponse = m.group(1);

                JSONArray jsonA = new JSONObject(stockResponse)
                        .getJSONObject("ResultSet")
                        .getJSONArray("Result");

                for (int i = 0; i < jsonA.length(); i++) {
                    JSONObject jsonO = jsonA.getJSONObject(i);

                    String symbol = jsonO.getString("symbol");
                    Matcher stockMatcher = STOCK_REGEX.matcher(symbol);

                    // 7 characters is enough for StockSymbol.CountryCode, ex: AAPL.MX
                    if (stockMatcher.matches() && symbol.length() < 8) {
                        Map<String, String> suggestion = new HashMap<>();
                        suggestion.put("symbol", symbol);
                        suggestion.put("name", jsonO.getString("name"));
                        suggestions.add(suggestion);
                    }
                }
            }
            return suggestions;
        } catch (JSONException ignored) {
        }
        return suggestions;
    }

}
