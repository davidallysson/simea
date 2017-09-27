package structures;

public class Time {
	
	public Long minutes;
	public Long seconds;
	
	public Time(Long time) {
		minutes = time / 60000;
		seconds = (time % 60000) / 1000;
	}
	
	public Long getMinutes() {
		return minutes;
	}
	
	public Long getSeconds() {
		return seconds;
	}
	
	public String toString() {
		return minutes+"Minutos, "+seconds+"Segundos";
	}

}
