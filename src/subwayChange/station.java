package subwayChange;

import java.util.ArrayList;

public class station {
	String stationName = "";  //站点名称
	ArrayList<String> LineID = new ArrayList<String>();		//站点所在的线路
	ArrayList<station> AdjacentStations = new ArrayList<station>(); //相邻站点
	boolean IsTransfer = false;  //站点是否是换乘站

	//设置站点名称
	public void setName(String name) {
		this.stationName = name;
	}
	
	//添加站点所在线路信息
	public void addLineName(String id) {
		this.LineID.add(id);
		//如果站点所在线路出现多条，则可作为换乘点
		if(LineID.size()>1) {
			IsTransfer = true;
		}
	}
	
	public void addAdjacentStations(station t) {
		this.AdjacentStations.add(t);
	}
	
	public String getStationName() {
		return this.stationName;
	}
	
	public ArrayList<String> getLineName() {
		return this.LineID;
	}
	public ArrayList<station> getAdjacentStations(){
		return this.AdjacentStations;
	}
	public boolean getIsTransfer() {
		return this.IsTransfer;
	}
}
