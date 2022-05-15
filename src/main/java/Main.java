import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main ( String[] args ) throws IOException, ParserConfigurationException, SAXException {


        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        CSVWriter writer = new CSVWriter(new FileWriter(fileName));
        String[] record1 = "1,John,Smith,USA,25".split(",");
        String[] record = "2,Inav,Petrov,RU,23".split(",");
        writer.writeNext(record1);
        writer.writeNext(record);
        writer.close();
        String nameJson = "data.json";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        String dataJson = writeString(json, nameJson);




        String nameJson2 = "data2.json";
        List<Employee> list2 = parseXML("data.xml");

        String json2 = listToJson(list2);
        String dataJson2 = writeString(json2, nameJson2);

    }

    public static List<Employee> parseCSV ( String[] columnMapping, String fileName ) {

        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            staff.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public static String listToJson ( List<Employee> list ) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        System.out.println(gson.toJson(list));
        return json;
    }

    public static String writeString ( String json, String fileNameJson ) throws IOException {
        try (FileWriter file = new
                FileWriter(fileNameJson)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static List<Employee> parseXML ( String fileName ) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> staff = new ArrayList<>();

        try {
            File file = new File(fileName);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("Сотрудник");
            System.out.println("Cписок сотрудников : ");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element fstElmnt = (Element) node;


                    NodeList elmntLst = fstElmnt.getElementsByTagName("id");
                    Element elmnt = (Element) elmntLst.item(0);
                    NodeList fstNm = elmnt.getChildNodes();
                    String ID = ((Node) fstNm.item(0)).getNodeValue();
                    System.out.println("id : " + ID);

                    NodeList elmntLst2 = fstElmnt.getElementsByTagName("Первое имя");
                    Element elmnt2 = (Element) elmntLst2.item(0);
                    NodeList lstNm2 = elmnt2.getChildNodes();
                    String FirstName = ((Node) lstNm2.item(0)).getNodeValue();
                    System.out.println("FirstName : " + FirstName);

                    NodeList elmntLst3 = fstElmnt.getElementsByTagName("Фамилия");
                    Element elmnt3 = (Element) elmntLst3.item(0);
                    NodeList lstNm3 = elmnt3.getChildNodes();
                    String lastName = ((Node) lstNm3.item(0)).getNodeValue();
                    System.out.println("Фамилия: " + lastName);


                    NodeList elmntLst4 = fstElmnt.getElementsByTagName("страна");
                    Element elmnt4 = (Element) elmntLst4.item(0);
                    NodeList lstNm4 = elmnt4.getChildNodes();
                    String country = ((Node) lstNm4.item(0)).getNodeValue();
                    System.out.println("страна: " + country);


                    NodeList elmntLst5 = fstElmnt.getElementsByTagName("возраст");
                    Element elmnt5 = (Element) elmntLst5.item(0);
                    NodeList lstNm5 = elmnt5.getChildNodes();
                    String Age = ((Node) lstNm5.item(0)).getNodeValue();
                    System.out.println("age : " + Age);

                    Employee employee = new Employee(Long.parseLong(ID), FirstName, lastName, country, Integer.parseInt(Age));
                    staff.add(employee);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return staff;
    }

}