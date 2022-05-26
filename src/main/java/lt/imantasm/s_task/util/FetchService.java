package lt.imantasm.s_task.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    static final Map<String, String> codeNameMap = new HashMap<>();
    private final CurrencyRepository currencyRepository;
    Map<String, Map<String, Double>> dateRatesMap = new HashMap<>();
    ObjectMapper mapper = new ObjectMapper();
    @Value("${currency.fetch.xml.uri}")
    private String fetchUrlXML;
    @Value("${currency.fetch.csv.zip.uri}")
    private String fetchUrlCsvZip;
    @Value("${currency.names.file}")
    private String currencyNames;
    @Value("${historical.data.file}")
    private String historicalDataTxtFile;

    @SneakyThrows
    @PostConstruct
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
                String code = codeNameMap.get(currencyCodes[i]);
                BigDecimal rateToEuro = BigDecimal.valueOf(Double.parseDouble(currencyRates[i].trim()));
                currencyList.add(new Currency(null, currencyCodes[i], code != null ? code : "", rateToEuro));
            }
            saveHistoricalData(currencyCodes, currencyRates);
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        currencyRepository.saveAll(currencyList);
    }

    private void saveHistoricalData(String[] currencyCodes, String[] currencyRates) throws IOException {
        String currentDateString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
        Map<String, Double> data;
        dateRatesMap = mapper.readValue(new ClassPathResource(historicalDataTxtFile).getFile(), new TypeReference<>() {});
        data = dateRatesMap.get(currentDateString);
        if (data.isEmpty()) {
            Map<String, Double> ratesMap = new HashMap<>();
            Map<String, Map<String, Double>> dateRatesEntry = new HashMap<>();
            for (int i = 1; i < currencyCodes.length; i++) {
                ratesMap.put(currencyCodes[i], Double.parseDouble(currencyRates[i].trim()));
            }
            dateRatesEntry.put(currentDateString, ratesMap);
            String path = "src/main/resources/historical_data.txt";
            try (BufferedWriter out = new BufferedWriter(new FileWriter(path, true))) {
                String s = mapper.writeValueAsString(dateRatesEntry);
                out.write(s);
                out.newLine();
                out.flush();
            } catch (IOException e) {
                log.info(Arrays.toString(e.getStackTrace()));
            }
        }
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

}
