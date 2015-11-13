package models.display;

import models.id.ac.unpar.siamodels.MataKuliah;
import models.support.*;

public class JadwalDisplay {
	private JadwalBundle jb;
	private JadwalKuliah[][] kuliahCalendar;
	private JadwalUjian[][] UTSCalendar;
	private JadwalUjian[][] UASCalendar;
	
	public JadwalDisplay(JadwalBundle jb){
		this.jb = jb;
		/*
		JadwalKuliah test = new JadwalKuliah(MataKuliah.createMataKuliah("AIFT3S",5,"Test"),'A',"X","Sabtu","07.00-08.40","AMX0903");
		jb.getJadwalKuliah().add(test);
		*/
		kuliahCalendar = new JadwalKuliah[6][22];
		fillJadwalKuliah();
	}
	
	public JadwalKuliah getJadwalKuliah(int hari, int waktu){
		return kuliahCalendar[hari][waktu];
	}
	
	public boolean isKuliahEmpty(){
		return jb.getJadwalKuliah().isEmpty();
	}
	
	public boolean isUTSEmpty(){
		return jb.getJadwalUTS().isEmpty();
	}
	
	public boolean isUASEmpty(){
		return jb.getJadwalUAS().isEmpty();
	}
	
	private void fillJadwalKuliah(){
		if(!jb.getJadwalKuliah().isEmpty()){
            for (int i = 0; i < kuliahCalendar.length; i++) {
                for (int j = 0; j < kuliahCalendar[i].length; j++) {
                    kuliahCalendar[i][j] = new JadwalKuliah();
                }
            }
            for (int i = 0; i < jb.getJadwalKuliah().size(); i++) {
                JadwalKuliah jdw = jb.getJadwalKuliah().get(i);
                int day = dayTranslate(jdw.getHari());
                if(day!=-1){
                	String[] timePair = jdw.getWaktu().split("-");
                    String start = timePair[0];
                    String end = timePair[1];
                    int range = (Integer.parseInt(end.substring(0, 2))- Integer.parseInt(start.substring(0, 2)))*2;
                    int beginIndex = 0;
                    int half = Character.getNumericValue(start.charAt(3));
                    if(half<3){
                        beginIndex = (Integer.parseInt(start.substring(0, 2))-7)*2;
                    }
                    else if(half>=3){
                        beginIndex =((Integer.parseInt(start.substring(0, 2))-7)*2)+1;
                    }
                    for (int j = beginIndex; j < beginIndex+range; j++) {
                        kuliahCalendar[day][j] = jdw;  
                    }
                }
            }
        }
	}
	
	private int dayTranslate(String hari){
        int day = -1;
        switch(hari){
            case "Senin": 
                day = 0;
                break;
            case "Selasa": 
                day = 1;
                break;
            case "Rabu": 
                day = 2;
                break;
            case "Kamis": 
                day = 3;
                break;
            case "Jumat": 
                day = 4;
                break;
            case "Sabtu": 
                day = 5;
                break;
            default:
                day = -1;
                break;
        }
        return day;
    }
}
