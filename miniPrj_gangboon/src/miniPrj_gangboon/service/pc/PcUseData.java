package miniPrj_gangboon.service.pc;

public class PcUseData {
	
	private int useNum;
	private int seatNum;
	private int memNum;
	private String startTime;
	private String endTime;
	public int getUseNum() {
		return useNum;
	}
	public void setUseNum(int useNum) {
		this.useNum = useNum;
	}
	public int getSeatNum() {
		return seatNum;
	}
	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}
	public int getMemNum() {
		return memNum;
	}
	public void setMemNum(int memNum) {
		this.memNum = memNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public PcUseData(int useNum, int seatNum, int memNum, String startTime, String endTime) {
		super();
		this.useNum = useNum;
		this.seatNum = seatNum;
		this.memNum = memNum;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public PcUseData() {
		super();
	}
	
	
	

}
