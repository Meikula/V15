package come.webserver.core.http;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpContext {
    private static Map<String, String> mimeMapping = new HashMap<>();

    static {
        initMimeMapping();
    }
    private static void initMimeMapping(){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("./config/web.xml");
            Element root = document.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element e:list){
                String key=e.elementText("extension");
                String value=e.elementText("mime-type");
                mimeMapping.put(key,value);
            }
            System.out.println(list.size());
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
    public static String getMimeType(String key){
        return mimeMapping.get(key);

    }
}
