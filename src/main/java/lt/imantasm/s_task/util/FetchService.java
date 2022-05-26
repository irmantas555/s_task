package lt.imantasm.s_task.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lt.imantasm.s_task.model.currency.entity.Currency;
import lt.imantasm.s_task.model.currency.repository.CurrencyRepository;

@Log
@Service
@RequiredArgsConstructor
public class FetchService {

    @Value("${currency.fetch.xml.uri}")
    private String fetchUrlXML;

    @Value("${currency.fetch.csv.zip.uri}")
    private String fetchUrlCsvZip;

    @Value("${currency.names.file}")
    private String currencyNames;

    static final Map<String, String> codeNameMap = new HashMap<>();

    private final CurrencyRepository currencyRepository;

    @SneakyThrows
    private List<Currency> fetchAndProcessXML() {
        URL url = new URL(fetchUrlXML);
        XmlMapper xmlMapper = new XmlMapper();
        List<Currency> currencyList = new ArrayList<>();
        try (JsonParser parser = xmlMapper.getFactory().createParser(url)) {
            JsonToken token;
            while ((token = parser.nextToken()) != null) {
                String currency = null;
                if (token == JsonToken.FIELD_NAME) {
                    if (Objects.equals(parser.getValueAsString(), "currency")) {
                        parser.nextToken();
                    }
                    if (Objects.equals(parser.getValueAsString(), "rate")) {
                        parser.nextToken();
                        currencyList.add(new Currency(null, currency, "", BigDecimal.valueOf(Double.parseDouble(parser.getValueAsString().trim()))));
                    }
                }
            }
        } catch (Exception e) {
            log.info(Arrays.toString(e.getStackTrace()));
        }
        return currencyList;
    }

    @SneakyThrows
    private void readCurrenciesNames() {
        Files.readAllLines((new ClassPathResource(currencyNames)).getFile().toPath())
             .forEach(line ->
                      {
                          String[] split = line.split(",");
                          codeNameMap.put(split[1].trim(), split[0]);
                      }
             );
    }

    @SneakyThrows
    @PostConstruct
    @Order(2)
    private void fetchAndProcessCSV() {
        readCurrenciesNames();
        URL url = new URL(fetchUrlCsvZip);
        List<Currency> currencyList = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(url.openStream())) {
            zis.getNextEntry();
            BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
            String[] currencyCodes = reader.readLine().split(", ");
            String[] currencyRates = reader.readLine().split(", ");
            for (int i = 1; i < currencyCodes.length; i++) {
                currencyList.add(new Currency(null, currencyCodes[i], codeNameMap.get(currencyCodes[i]) != null ? codeNameMap.get(currencyCodes[i]) : "",
                                              BigDecimal.valueOf(Double.parseDouble(currencyRates[i].trim()))));
            }

        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        currencyRepository.saveAll(currencyList);
    }

}
