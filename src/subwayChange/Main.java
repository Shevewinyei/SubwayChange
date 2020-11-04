package subwayChange;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;



public class Main {
	final static int Max = 999;
	static ArrayList<station> stations = new ArrayList<station>();
	static int[][] edges = new int[Max][Max];   //�ڽӾ���
	static int[] pre = new int[Max];    //ǰ���ڵ�����
	//��ȡ������·���ݣ�����վ������
	public static void AddStation() throws FileNotFoundException {
		Scanner in = new Scanner(new File("/Users/shenwenyan/eclipse-workspace/subwayChange/src/SubwayMessage.txt"));
		while(in.hasNextLine()) {
			String temp = in.nextLine();
			String[] tokens = temp.split(" ");
			//��·����
			String lineName = tokens[0];
			for(int i=1;i<tokens.length;i++) {
				//������list���Ƿ���ڸ�վ������,��ֻ�����·�����ڽڵ�(ȥ��)
				boolean t = false;  //�ж��Ƿ����arraylist��
				for(int j=0;j<stations.size()-1;j++) {
					if(stations.get(j).getStationName().equals(tokens[i])) {
						stations.get(j).addLineName(lineName);
						t = true;
						break;
					}
				}
				if(t==false) {
					station a = new station();
					a.setName(tokens[i]);
					a.addLineName(lineName);
					stations.add(a);
				}
				
			}
			//�������վ��
			for(int i=1;i<tokens.length-1;i++) {
				ADDAdjacentStations(tokens[i], tokens[i+1]);
			}
		}
	}
	//������ڽڵ㲢�����ڽӾ���
	public static void ADDAdjacentStations(String name1,String name2) {
		station t1 = findStation(name1);
		station t2 = findStation(name2);
		if(t1!=null && t2!=null) {
			t1.addAdjacentStations(t2);
			t2.addAdjacentStations(t1);
			int x = findStationIndex(name1);
			int y = findStationIndex(name2);
			edges[x][y] = 1;
			edges[y][x] = 1;
		}
		else {
			//System.out.println("δ�ҵ������Ƶ�վ�㣡����");
		}
	}
	//�ҵ��ڵ�
	public static station findStation(String name) {
		station aStation = null;
		for(int i=0;i<stations.size()-1;i++) {
			if(stations.get(i).getStationName().equals(name)) {
				aStation =  stations.get(i);
				return aStation;
			}
		}
		//System.out.println("δ�ҵ������Ƶ�վ�㣡����");
		return aStation;
	}
	//�ҵ��ڵ��±�
	public static int findStationIndex(String name) {
		int index = -1;
		for(int i=0;i<stations.size()-1;i++) {
			if(stations.get(i).getStationName().equals(name)) {
				return i;
			}
		}
		//System.out.println("δ�ҵ������Ƶ�վ�㣡����");
		return index;
	}
	public static int Dijkstra(int startId,int endId,int[][] graph,int[] visit,int[] pre) {
		//�ڵ����        
	    int n = graph.length;        
	    PriorityQueue<Node> pq = new PriorityQueue<>(new Node());        
	    //��������pq        
	    pq.add(new Node(startId, 0));        
	    while (!pq.isEmpty()){            
	        Node t = pq.poll(); 
	        //��ǰ�ڵ����յ㣬���ɷ������·��            
	        if(t.node == endId)                
	            return t.cost;            
	        //t�ڵ��ʾ��δ����            
	        if (visit[t.node]==0){                
	            //���ڵ�����Ϊ�ѷ���                
	            visit[t.node] = -1;                
	            //����ǰ�ڵ�������δ���ʵĽڵ����                
	            for (int i = 0; i < n; i++) {                    
	                if (graph[t.node][i]!=0 && visit[i]==0) {  
	                    pq.add(new Node(i, t.cost + graph[t.node][i]));
	                    pre[i] = t.node;
	                }                
	            }            
	        }        
	    }        
	    return -1;    
	}
	//��ӡ·��
	public static void PrintPath(int startId,int endId) {
		Stack<Integer> Path = new Stack<Integer>();
		int end = endId;
		//ǰ�ýڵ���ջ��ʹ�����ʱ��Ϊ����
		while(endId!=startId) {
			Path.add(endId);
			int temp = pre[endId];
			endId = temp;
		}
		
		String lineString = "";
		String nextLineString = "";
		
		lineString = IsinOneLine(stations.get(startId), stations.get(Path.peek()));
		System.out.println(stations.get(startId).getStationName()+lineString);
		int i;
		while(true){
			i = Path.pop();
			if(Path.isEmpty()) break;
			nextLineString = IsinOneLine(stations.get(i), stations.get(Path.peek()));
			//�ж��Ƿ���
			if(nextLineString.equals(lineString)) {
				//������
				System.out.print("   ");
				System.out.print("------->");
				System.out.println(stations.get(i).getStationName());
			}
			else {
				//����
				lineString = nextLineString;
				System.out.print("   ");
				System.out.print("------->");
				System.out.println(stations.get(i).getStationName());
				System.out.println("�� "+stations.get(i).getStationName()+" ���� "+lineString);
			}
		}
		System.out.print("   ");
		System.out.print("------->");
		System.out.println(stations.get(end).getStationName());
	}
	//�ж��Ƿ���ͬһ��·�ϣ�����Ǿͷ�����·�����򷵻�null
	public static String IsinOneLine(station a,station b) {
		for(int i=0;i<a.getLineName().size();i++) {
			for(int j=0;j<b.getLineName().size();j++) {
				if(a.getLineName().get(i).equals(b.getLineName().get(j))) {
					return a.getLineName().get(i);
				}
			}
		}
		return null;
	}
	public static void main(String[] args) throws FileNotFoundException {
		AddStation();
		//����վ�㣬�ֶ���������
		ADDAdjacentStations("T2��վ¥", "��Ԫ��");
		ADDAdjacentStations("T3��վ¥", "T2��վ¥");
		
		//�������վ���յ�վ
		System.out.print("���������վ��");
		Scanner in = new Scanner(System.in);
		String startNameString = in.next();
		System.out.print("�������յ�վ��");
		in = new Scanner(System.in);
		String endNameString = in.next();
		
		//�ҵ����վ���յ�վ
		station startStation = findStation(startNameString);
		station endStation = findStation(endNameString);
		if(startStation==null&&endStation!=null){
			System.out.println("���վ�����ڣ�����");
		}
		else if(endStation==null&&startStation!=null){
			System.out.println("�յ�վ�����ڣ�����");
		}
		else if(endStation==null&&startStation==null) {
			System.out.println("���վ���յ�վ�������ڣ�����");
		}
		else {
			System.out.println("��������.....");
			int startId = findStationIndex(startNameString);
			int endId = findStationIndex(endNameString);
			
			//�����·��
			int[] visit = new int[Max];  //�Ƿ����
			int dis = Dijkstra(startId, endId,edges,visit,pre);
			System.out.println("������վ������"+ dis);
			if(dis!=-1 && dis!=0) {
				//��ӡ·��
				PrintPath(startId, endId);
			}
			else if(dis == -1){
				System.out.println("�޷����");
			}
			else if(dis == 0){
				System.out.println("���վ���յ�վΪͬһվ������");
			}
			
		}
		
		
	}
}
class Node implements Comparator<Node> {        
    public int node;        
    public int cost;
             
    public Node(){}
     
    public Node(int node, int cost){            
        this.node = node;            
        this.cost = cost;        
    }        
    @Override        
    public int compare(Node node1, Node node2){            
        return node1.cost-node2.cost;       
    }    
}
