package models.support;

import id.ac.unpar.siamodels.Mahasiswa;
import id.ac.unpar.siamodels.MataKuliah;
import id.ac.unpar.siamodels.MataKuliahFactory;
import id.ac.unpar.siamodels.TahunSemester;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MultipleRequest implements Runnable {
    private int l;
    private ArrayList<String> listSemester;
    private String NILAI_URL;
    private String phpsessid;
    private Mahasiswa logged_mhs;
    ScriptEngineManager factory;
    ScriptEngine engine;

    MultipleRequest(int l, ArrayList<String> listSemester, String NILAI_URL, String phpsessid, Mahasiswa logged_mhs){
        this.l = l;
        this.listSemester = listSemester;
        this.NILAI_URL = NILAI_URL;
        this.phpsessid = phpsessid;
        this.logged_mhs = logged_mhs;
        factory = new ScriptEngineManager();
        engine = factory.getEngineByName("JavaScript");
    }

    @Override
    public void run() {
        try {
            String[] thn_sem = listSemester.get(l).split("-");
            String thn = thn_sem[0];
            String sem = thn_sem[1];
            Connection connection = Jsoup.connect(NILAI_URL + "/" + thn + "/" + sem);
            connection.cookie("ci_session", phpsessid);
            connection.timeout(0);
            connection.validateTLSCertificates(false);
            connection.method(Connection.Method.POST);
            Connection.Response resp = connection.execute();
            Document doc = resp.parse();

            Elements scripts = doc.select("script");
            for (Element script: scripts) {
                String scriptHTML = script.html();
                if (scriptHTML.contains("var data_mata_kuliah = [];") && scriptHTML.contains("var data_angket = [];")) {
                    String scriptDataMataKuliah = scriptHTML.substring(scriptHTML.indexOf("var data_mata_kuliah = [];"), scriptHTML.indexOf("var data_angket = [];"));
                    engine.eval(scriptDataMataKuliah);
                    ScriptObjectMirror dataMataKuliah = (ScriptObjectMirror) engine.get("data_mata_kuliah");
                    TahunSemester tahunSemesterNilai = new TahunSemester(Integer.parseInt(thn), sem.charAt(0));
                    for (Map.Entry<String, Object> mataKuliahEntry : dataMataKuliah.entrySet()) {
                        ScriptObjectMirror mataKuliah = (ScriptObjectMirror) mataKuliahEntry.getValue();
                        MataKuliah curr_mk = MataKuliahFactory.getInstance().createMataKuliah((String) mataKuliah.get("kode_mata_kuliah"), Integer.parseInt((String) mataKuliah.get("jumlah_sks")), (String) mataKuliah.get("nama_mata_kuliah"));
                        logged_mhs.getRiwayatNilai()
                                .add(new Mahasiswa.Nilai(tahunSemesterNilai, curr_mk, (String) mataKuliah.get("na")));
                    }
                    break;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (ScriptException se){
            se.printStackTrace();
        }
    }
}
