/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tryjsoup;

import java.io.IOException;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * @author Herfan Heryandi
 */
public class TryJsoup {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        Connection conn = Jsoup.connect("http://data.bmkg.go.id/propinsi_13_1.xml");
        conn.timeout(0);
        conn.method(Connection.Method.GET);
        Response resp = conn.execute();
        Document xml =  resp.parse();
        Elements elem = xml.select("row");
        for (Element e:elem) {
            System.out.println("Kota: " + e.child(0).text());
            System.out.println("Lintang: " + e.child(2).text());
            System.out.println("Bujur: " + e.child(3).text());
            System.out.println("Cuaca: " + e.child(6).text());
            System.out.println("Suhu Min: " + e.child(7).text());
            System.out.println("Suhu Max: " + e.child(8).text());
            System.out.println();
        }
    }
    
}


