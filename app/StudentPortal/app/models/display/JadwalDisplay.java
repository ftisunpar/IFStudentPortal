package models.display;

import java.util.List;

import models.support.JadwalKuliah;

public class JadwalDisplay {
	private List<JadwalKuliah> jadwalList;
	private JadwalKuliah[][] kuliahCalendar;
	private String[] hariList;
	
	public JadwalDisplay(List<JadwalKuliah> jadwalList){
		this.jadwalList = jadwalList;
		kuliahCalendar = new JadwalKuliah[6][22];
		hariList = new String[]{"Senin","Selasa","Rabu","Kamis","Jumat","Sabtu"};
		fillKuliahCalendar();
	}
	
	public JadwalKuliah getJadwalKuliah(int hari, int waktu){
		return kuliahCalendar[hari][waktu];
	}
	
	public boolean isKuliahEmpty(){
		return jadwalList.isEmpty();
	}
	
	private void fillKuliahCalendar(){
		if(!jadwalList.isEmpty()){
            for (int i = 0; i < kuliahCalendar.length; i++) {
                for (int j = 0; j < kuliahCalendar[i].length; j++) {
                    kuliahCalendar[i][j] = new JadwalKuliah();
                }
            }
            for (int i = 0; i < jadwalList.size(); i++) {
                JadwalKuliah jdw = jadwalList.get(i);
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
                    int endHalf = Character.getNumericValue(end.charAt(3));
                    if(endHalf>3){
                    	range++;  
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
