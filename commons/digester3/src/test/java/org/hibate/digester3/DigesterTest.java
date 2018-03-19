package org.hibate.digester3;

import org.apache.commons.digester3.Digester;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

/**
 * Created by lenovo on 2018/3/18.
 */
public class DigesterTest {

    private List<StringEntry> entryList = new ArrayList<>();

    public void addEntry(StringEntry entry) {
        this.entryList.add(entry);
    }

    private InputStream getXmlText() {
        final String xml =
                "<resources>\n" +
                "    <string name=\"Hello\" value=\"Digester!\"/>\n" +
                "</resources>";
        return new ByteArrayInputStream(xml.getBytes());
    }

    private Digester createContextDigester() {
        final SAXParserFactory factory = SAXParserFactory.newInstance();
        long t1 = System.currentTimeMillis();
        Digester digester = new Digester() {
            @Override
            public SAXParserFactory getFactory() {
                return factory;
            }
        };
        digester.setValidating(false);
        digester.setClassLoader(this.getClass().getClassLoader());

        digester.addObjectCreate("resources/string", StringEntry.class);
        digester.addSetProperties("resources/string");
        digester.addSetNext("resources/string",
                "addEntry",
                StringEntry.class.getCanonicalName());

        long t2 = System.currentTimeMillis();
        System.out.println("Digester created " + (t2 - t1) + " ms");
        return digester;
    }

    @Test
    public void parserTest() {
        try {
            Digester digester = this.createContextDigester();
            digester.push(this);
            digester.parse(this.getXmlText());

            for (int i = 0; i < this.entryList.size(); i++) {
                StringEntry entry = this.entryList.get(i);
                System.out.println(entry.getName() + " " + entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class StringEntry {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
