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
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
    @Value("${currency.fetch.xml.uri}")
    private String fetchUrlXML;
    @Value("${currency.fetch.csv.zip.uri}")
    private String fetchUrlCsvZip;
    @Value("${currency.names.file}")
    private String currencyNames;
    @Value("${historical.data.file}")
    private String historicalDataTxtFile;
    ObjectMapper mapper = new ObjectMapper();

    static final Map<String, String> codeNameMap = new HashMap<>();
    private final CurrencyRepository currencyRepository;
    List<Currency> currencyList = new ArrayList<>();
    Map<String, Map<String, Double>> dateRatesMap = new HashMap<>();

    @SneakyThrows
    public boolean fetchAndProcessCSV() {
        if (CollectionUtils.isEmpty(currencyList) || currencyList.get(0).getDate().equals(LocalDate.now())) {
            return false;
        }
        readCurrenciesNames();
        URL url = new URL(fetchUrlCsvZip);
        try (ZipInputStream zis = new ZipInputStream(url.openStream())) {
            zis.getNextEntry();
            BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
            String[] currencyCodes = reader.readLine().split(", ");
            String[] currencyRates = reader.readLine().split(", ");
            for (int i = 1; i < currencyCodes.length; i++) {
                String code = codeNameMap.get(currencyCodes[i]);
                BigDecimal rateToEuro = BigDecimal.valueOf(Double.parseDouble(currencyRates[i].trim()));
                currencyList.add(new Currency(null, currencyCodes[i], code != null ? code : "", rateToEuro, LocalDate.now()));
            }
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        return true;
    }

    @PostConstruct
    public void saveHistoricalDataAndReturnNotExist() throws IOException {
        boolean todaysProcessed = fetchAndProcessCSV();
        if (todaysProcessed) {
            return;
        } else {
            readCurrenciesNames();
        }
        String currentDateString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
        Map<String, Double> data;
        dateRatesMap = mapper.readValue(new ClassPathResource(historicalDataTxtFile).getFile(), new TypeReference<>() {});
        data = dateRatesMap.get(currentDateString);
        if (data == null) {
            Map<String, Double> ratesMap = new HashMap<>();
            for (int i = 1; i < currencyList.size(); i++) {
                ratesMap.put(currencyList.get(i).getCode(), currencyList.get(i).getRateToEuro().doubleValue());
            }
            dateRatesMap.put(currentDateString, ratesMap);
            String path = "src/main/resources/historical_data.txt";
            try (BufferedWriter out = new BufferedWriter(new FileWriter(path, false))) {
                String s = mapper.writeValueAsString(dateRatesMap);
                out.write(s);
                out.flush();
            } catch (IOException e) {
                log.info(Arrays.toString(e.getStackTrace()));
            }
        }
        long count = currencyRepository.count();
        if (count == 0) {
            List<Currency> allCurencyDates = processDatesRateMap(dateRatesMap);
            currencyRepository.saveAll(allCurencyDates);
        } else {
            currencyRepository.saveAll(currencyList);
        }

    }

    private List<Currency> processDatesRateMap(Map<String, Map<String, Double>> dateRatesMap) {
        return dateRatesMap.entrySet().stream()
                           .flatMap(entry -> getListFromEntry(entry).stream())
                           .toList();
    }

    private List<Currency> getListFromEntry(Map.Entry<String, Map<String, Double>> entry) {
        return entry.getValue().entrySet().stream()
                    .map(entrySet -> Currency.builder().id(null)
                                             .code(entrySet.getKey())
                                             .name(codeNameMap.get(entrySet.getKey()))
                                             .rateToEuro(BigDecimal.valueOf(entrySet.getValue()))
                                             .date(LocalDate.parse(entry.getKey(), DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)))
                                             .build())
                    .toList();
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
        List<Currency> currencyArrayList = new ArrayList<>();
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
                        currencyArrayList.add(new Currency(null, currency, "", BigDecimal.valueOf(Double.parseDouble(parser.getValueAsString().trim())), LocalDate.now()));
                    }
                }
            }
        } catch (Exception e) {
            log.info(Arrays.toString(e.getStackTrace()));
        }
        return currencyArrayList;
    }
}
