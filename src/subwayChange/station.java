package subwayChange;

import java.util.ArrayList;

public class station {
	String stationName = "";  //վ������
	ArrayList<String> LineID = new ArrayList<String>();		//վ�����ڵ���·
	ArrayList<station> AdjacentStations = new ArrayList<station>(); //����վ��
	boolean IsTransfer = false;  //վ���Ƿ��ǻ���վ

	//����վ������
	public void setName(String name) {
		this.stationName = name;
	}
	
	//���վ��������·��Ϣ
	public void addLineName(String id) {
		this.LineID.add(id);
		//���վ��������·���ֶ����������Ϊ���˵�
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
