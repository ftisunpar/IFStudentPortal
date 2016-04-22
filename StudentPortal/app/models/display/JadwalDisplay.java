package models.display;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import id.ac.unpar.siamodels.JadwalKuliah;

public class JadwalDisplay {
	private List<JadwalKuliah> jadwalList;
	private JadwalKuliah[][] kuliahCalendar;
	private String[] hariList;

	public JadwalDisplay(List<JadwalKuliah> jadwalList) {
		this.jadwalList = jadwalList;
		kuliahCalendar = new JadwalKuliah[6][22];
		hariList = new String[] { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" };
		fillKuliahCalendar();
	}

	public String getHariByIndex(int index) {
		return this.hariList[index];
	}

	public JadwalKuliah getJadwalKuliah(int indexHari, int indexWaktu) {
		return kuliahCalendar[indexHari][indexWaktu];
	}

	public boolean isKuliahEmpty() {
		return jadwalList.isEmpty();
	}

	private void fillKuliahCalendar() {
		if (!jadwalList.isEmpty()) {
			for (int i = 0; i < kuliahCalendar.length; i++) {
				for (int j = 0; j < kuliahCalendar[i].length; j++) {
					kuliahCalendar[i][j] = new JadwalKuliah();
				}
			}
			for (int i = 0; i < jadwalList.size(); i++) {
				JadwalKuliah jdw = jadwalList.get(i);
				int day = dayTranslate(jdw.getHari());
				if (day != -1) {
					LocalTime start = jdw.getWaktuMulai();
					LocalTime end = jdw.getWaktuSelesai();
					int range = (end.getHour() - start.getHour()) * 2;
					int beginIndex = 0;
					int half = start.getMinute() / 10;
					if (half < 3) {
						beginIndex = (start.getHour() - 7) * 2;
					} else if (half >= 3) {
						beginIndex = ((start.getHour() - 7) * 2) + 1;
					}
					int endHalf = end.getMinute() / 10;
					if (endHalf > 3) {
						range++;
					}
					for (int j = beginIndex; j < beginIndex + range; j++) {
						kuliahCalendar[day][j] = jdw;
					}
				}
			}
		}
	}

	private int dayTranslate(DayOfWeek hari) {
		int day = -1;
		switch (hari) {
		case MONDAY:
			day = 0;
			break;
		case TUESDAY:
			day = 1;
			break;
		case WEDNESDAY:
			day = 2;
			break;
		case THURSDAY:
			day = 3;
			break;
		case FRIDAY:
			day = 4;
			break;
		case SATURDAY:
			day = 5;
			break;
		default:
			day = -1;
			break;
		}
		return day;
	}
}
