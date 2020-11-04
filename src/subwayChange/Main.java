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
	static int[][] edges = new int[Max][Max];   //邻接矩阵
	static int[] pre = new int[Max];    //前驱节点数组
	//读取地铁线路数据，创建站点数组
	public static void AddStation() throws FileNotFoundException {
		Scanner in = new Scanner(new File("/Users/shenwenyan/eclipse-workspace/subwayChange/src/SubwayMessage.txt"));
		while(in.hasNextLine()) {
			String temp = in.nextLine();
			String[] tokens = temp.split(" ");
			//线路名称
			String lineName = tokens[0];
			for(int i=1;i<tokens.length;i++) {
				//先搜索list中是否存在该站点名称,则只添加线路和相邻节点(去重)
				boolean t = false;  //判断是否存在arraylist中
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
			//添加相邻站点
			for(int i=1;i<tokens.length-1;i++) {
				ADDAdjacentStations(tokens[i], tokens[i+1]);
			}
		}
	}
	//添加相邻节点并更新邻接矩阵
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
			//System.out.println("未找到该名称的站点！！！");
		}
	}
	//找到节点
	public static station findStation(String name) {
		station aStation = null;
		for(int i=0;i<stations.size()-1;i++) {
			if(stations.get(i).getStationName().equals(name)) {
				aStation =  stations.get(i);
				return aStation;
			}
		}
		//System.out.println("未找到该名称的站点！！！");
		return aStation;
	}
	//找到节点下标
	public static int findStationIndex(String name) {
		int index = -1;
		for(int i=0;i<stations.size()-1;i++) {
			if(stations.get(i).getStationName().equals(name)) {
				return i;
			}
		}
		//System.out.println("未找到该名称的站点！！！");
		return index;
	}
	public static int Dijkstra(int startId,int endId,int[][] graph,int[] visit,int[] pre) {
		//节点个数        
	    int n = graph.length;        
	    PriorityQueue<Node> pq = new PriorityQueue<>(new Node());        
	    //将起点加入pq        
	    pq.add(new Node(startId, 0));        
	    while (!pq.isEmpty()){            
	        Node t = pq.poll(); 
	        //当前节点是终点，即可返回最短路径            
	        if(t.node == endId)                
	            return t.cost;            
	        //t节点表示还未访问            
	        if (visit[t.node]==0){                
	            //将节点设置为已访问                
	            visit[t.node] = -1;                
	            //将当前节点相连且未访问的节点遍历                
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
	//打印路径
	public static void PrintPath(int startId,int endId) {
		Stack<Integer> Path = new Stack<Integer>();
		int end = endId;
		//前置节点入栈，使得输出时候为正序
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
			//判断是否换线
			if(nextLineString.equals(lineString)) {
				//不换线
				System.out.print("   ");
				System.out.print("------->");
				System.out.println(stations.get(i).getStationName());
			}
			else {
				//换线
				lineString = nextLineString;
				System.out.print("   ");
				System.out.print("------->");
				System.out.println(stations.get(i).getStationName());
				System.out.println("在 "+stations.get(i).getStationName()+" 换乘 "+lineString);
			}
		}
		System.out.print("   ");
		System.out.print("------->");
		System.out.println(stations.get(end).getStationName());
	}
	//判断是否在同一线路上，如果是就返回线路，否则返回null
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
		//特殊站点，手动加入连接
		ADDAdjacentStations("T2航站楼", "三元桥");
		ADDAdjacentStations("T3航站楼", "T2航站楼");
		
		//输入起点站和终到站
		System.out.print("请输入起点站：");
		Scanner in = new Scanner(System.in);
		String startNameString = in.next();
		System.out.print("请输入终点站：");
		in = new Scanner(System.in);
		String endNameString = in.next();
		
		//找到起点站和终点站
		station startStation = findStation(startNameString);
		station endStation = findStation(endNameString);
		if(startStation==null&&endStation!=null){
			System.out.println("起点站不存在！！！");
		}
		else if(endStation==null&&startStation!=null){
			System.out.println("终点站不存在！！！");
		}
		else if(endStation==null&&startStation==null) {
			System.out.println("起点站和终点站都不存在！！！");
		}
		else {
			System.out.println("正在搜索.....");
			int startId = findStationIndex(startNameString);
			int endId = findStationIndex(endNameString);
			
			//找最短路径
			int[] visit = new int[Max];  //是否访问
			int dis = Dijkstra(startId, endId,edges,visit,pre);
			System.out.println("经过总站点数："+ dis);
			if(dis!=-1 && dis!=0) {
				//打印路径
				PrintPath(startId, endId);
			}
			else if(dis == -1){
				System.out.println("无法到达！");
			}
			else if(dis == 0){
				System.out.println("起点站和终点站为同一站！！！");
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
