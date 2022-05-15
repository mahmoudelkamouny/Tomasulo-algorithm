package micro_project;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;

public class Simulator {
	static ArrayList<Instruction> instructionQueue = new ArrayList<Instruction>();
	static ArrayList<Double> Memory = new ArrayList<Double>();
//	static ArrayList<ResStation> mulResStation = new ArrayList<ResStation>();
//	static ArrayList<ResStation> addResStation = new ArrayList<ResStation>();
//	static ArrayList<ResStation> storeBuffer = new ArrayList<ResStation>();
//	static ArrayList<ResStation> loadBuffer = new ArrayList<ResStation>();
	static ResStation[] mulResStation = new ResStation[2];
	static ResStation[] addResStation = new ResStation[3];
	static ResStation[] storeBuffer = new ResStation[2];
	static ResStation[] loadBuffer = new ResStation[2];
	static Hashtable<String, Integer> cycles = new Hashtable<String, Integer>();
	static Hashtable<String, Register> registerFile = new Hashtable<String, Register>();
	static Hashtable<String, Double> CDB = new Hashtable<String, Double>();
	static int cycle=0;
	
	public static void nextCycle() {
		System.out.println("Cycle--Cycle--Cycle--Cycle--Cycle--Cycle--Cycle--Cycle--Cycle--Cycle--Cycle--Cycle-- "+cycle);
		CDB = new Hashtable<String, Double>();
		if(cycle<1) {
			cycle++;
			nextCycle();
		}
		else if(cycle==1) {
			issue(instructionQueue.get(0));
			cycle++;
			nextCycle();
		}
		else {
//			write
			for(int i=0;i<mulResStation.length;i++) {
				if(mulResStation[i]!=null && mulResStation[i].busy) {
					mulResStation[i].cycleStepWrite();
				}
			}
			for(int i=0;i<addResStation.length;i++) {
				if(addResStation[i]!=null && addResStation[i].busy) {
					addResStation[i].cycleStepWrite();
				}
			}
			for(int i=0;i<storeBuffer.length;i++) {
				if(storeBuffer[i]!=null && storeBuffer[i].busy) {
					storeBuffer[i].cycleStepWrite();
				}
			}
			for(int i=0;i<loadBuffer.length;i++) {
				if(loadBuffer[i]!=null && loadBuffer[i].busy) {
					loadBuffer[i].cycleStepWrite();
				}
			}
//			read
			for(int i=0;i<mulResStation.length;i++) {
				if(mulResStation[i]!=null && mulResStation[i].busy) {
					mulResStation[i].cycleStepRead();
				}
			}
			for(int i=0;i<addResStation.length;i++) {
				if(addResStation[i]!=null && addResStation[i].busy) {
					addResStation[i].cycleStepRead();
				}
			}
			for(int i=0;i<storeBuffer.length;i++) {
				if(storeBuffer[i]!=null && storeBuffer[i].busy) {
					storeBuffer[i].cycleStepRead();
				}
			}
//			for(int i=0;i<loadBuffer.length;i++) {
//				if(loadBuffer[i]!=null && loadBuffer[i].busy) {
//					storeBuffer[i].cycleStepRead();
//				}
//			}
			Enumeration<String> em = registerFile.keys();
			while (em.hasMoreElements()) {
				String key = em.nextElement();
				Register value = registerFile.get(key);
				value.cycleStepRead(key);
			}
			
			if(instructionQueue.size()>0) {		
				issue(instructionQueue.get(0));
				cycle++;
				nextCycle();
			}	
			else {
				if(isBusy()) {
					cycle++;
					nextCycle();
//					System.out.println("busy");
				}
				else {
					return;
				}
			}
		
		}
	}
	public static void issue(Instruction inst) {
		if(inst.type.toLowerCase().equals("mul.d") || inst.type.toLowerCase().equals("div.d")) {
			for(int i=0;i<mulResStation.length;i++) {
				if(mulResStation[i]==null || mulResStation[i].busy==false) {
					System.out.println("Issuing instruction: "+inst);
					instructionQueue.remove(0);
					ResStation newRes= new ResStation(i,inst.type.toLowerCase(), true, (int)(cycles.get(inst.type.toLowerCase())));
					Register rj = registerFile.get(inst.j);
					Register rk = registerFile.get(inst.k);
					if(rj.Qi.equals("0")){
						newRes.Vj = rj.content;
					}
					else {
						newRes.Qj = rj.Qi;
					}
					if(rk.Qi.equals("0")){
						newRes.Vk = rk.content;
					}
					else {
						newRes.Qk = rk.Qi;
					}
					mulResStation[i] = newRes;
					registerFile.remove(inst.rs);
					registerFile.put(inst.rs, new Register(("M" + i), 0));
					System.out.println("Added to Mul-ResStation"+ i +" the following:\n"+ newRes);
					return;
				}
			}
			System.out.println("Cannot issue instruction: " +inst + " due to structure hazard");
		}
		else if(inst.type.toLowerCase().equals("add.d")|| inst.type.toLowerCase().equals("sub.d")) {
			for(int i=0;i<addResStation.length;i++) {
				if(addResStation[i]==null || addResStation[i].busy==false) {
					System.out.println("Issuing instruction: "+inst);
					instructionQueue.remove(0);
					ResStation newRes= new ResStation(i,inst.type.toLowerCase(), true, (int)(cycles.get(inst.type.toLowerCase())));
					Register rj = registerFile.get(inst.j);
					Register rk = registerFile.get(inst.k);
					if(rj.Qi.equals("0")){
						newRes.Vj = rj.content;
					}
					else {
						newRes.Qj = rj.Qi;
					}
					if(rk.Qi.equals("0")){
						newRes.Vk = rk.content;
					}
					else {
						newRes.Qk = rk.Qi;
					}
					addResStation[i] = newRes;
					registerFile.remove(inst.rs);
					registerFile.put(inst.rs, new Register(("A" + i), 0));
					System.out.println("Added to Add-ResStation" + i +  " the following:\n"+ newRes);
					return;
				}
			}
			System.out.println("Cannot issue instruction: " +inst + " due to structure hazard");
		}
		else if(inst.type.toLowerCase().equals("s.d")) {
			for(int i=0;i<storeBuffer.length;i++) {
				if(storeBuffer[i]==null || storeBuffer[i].busy==false) {
					System.out.println("Issuing instuction: "+inst);
					instructionQueue.remove(0);
					ResStation newRes= new ResStation(i,inst.type.toLowerCase(), true, (int)(cycles.get(inst.type.toLowerCase())));
					Register rj = registerFile.get(inst.rs);
					if(rj.Qi.equals("0")){
						newRes.Vj = rj.content;
					}
					else {
						newRes.Qj = rj.Qi;
					}
					newRes.Vk = (double)Integer.parseInt((String)inst.j);
					newRes.A = (int)(Integer.parseInt((String)inst.j) + registerFile.get(inst.k).content);
					storeBuffer[i] = newRes;
					System.out.println("Added to StoreBuffer"+i+ " the following:\n"+ newRes);
					return;
				}				
			}
			System.out.println("Cannot issue instruction: " +inst + " due to structure hazard");
			
		}
		else if(inst.type.toLowerCase().equals("l.d")) {
			for(int i=0;i<loadBuffer.length;i++) {
				if(loadBuffer[i]==null || loadBuffer[i].busy==false) {
					System.out.println("Issuing instruction: "+inst);
					instructionQueue.remove(0);
					ResStation newRes= new ResStation(i,inst.type.toLowerCase(), true, (int)(cycles.get(inst.type.toLowerCase())));
					Register rj = registerFile.get(inst.k);
					
					newRes.Vj = rj.content;
					
					newRes.Vk = (double)Integer.parseInt((String)inst.j);
					newRes.A = (int)(Integer.parseInt((String)inst.j) + registerFile.get(inst.k).content);
					loadBuffer[i] = newRes;
					registerFile.remove(inst.rs);
					registerFile.put(inst.rs, new Register(("L" + i), 0));
					System.out.println("Added to LoadBuffer"+i+ " the following:\n"+ newRes);
					return;
				}
					
			}
			System.out.println("Cannot issue instruction: " +inst + " due to structure hazard");
		}
		else {
			System.out.println("SYNATX ERROR!!");
		}
	}
	public static boolean isBusy() {
		if(cycle<=1)
			return true;
		else {
			for(int i=0;i<mulResStation.length;i++) {
				if(mulResStation[i]!=null && mulResStation[i].busy) {
					return true;
				}
			}
			for(int i=0;i<addResStation.length;i++) {
				if(addResStation[i]!=null && addResStation[i].busy) {
					return true;
				}
			}
			for(int i=0;i<storeBuffer.length;i++) {
				if(storeBuffer[i]!=null && storeBuffer[i].busy) {
					return true;
				}
			}
			for(int i=0;i<loadBuffer.length;i++) {
				if(loadBuffer[i]!=null && loadBuffer[i].busy) {
					return true;
				}
			}
			return false;
		}
	}
	public static void main(String[] args) {
		//add.d F1 F0 F0
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the program with a ; between the instructions and a space between the instruction attributes");
		System.out.println("For example: mul.d F1 F0 F0;s.d F1 0 F0");
		//s.d F1 0 R0;s.d F1 0 R0;s.d F1 0 R0
		//l.d F6 0 R2;l.d F2 0 R3;mul.d F0 F2 F4;sub.d F8 F2 F6;div.d F10 F0 F6;add.d F6 F8 F2
		//
		String s = sc.nextLine();
		String[] a = s.split(";");
		for(int i=0;i<a.length;i++) {
			String[] len = a[i].split(" ");
			if(len.length==4) {
				Instruction temp = new Instruction(a[i]);
				instructionQueue.add(temp);
			}
			else {
				System.out.println("SYNTAX ERROR!!");
			}
		}
//		cycles.put("mul.d", 5);
//		cycles.put("div.d", 8);
//		cycles.put("add.d", 3);
//		cycles.put("sub.d", 3);
//		cycles.put("s.d", 2);
//		cycles.put("l.d", 2);
		cycles.put("mul.d", 10);
		cycles.put("div.d", 40);
		cycles.put("add.d", 2);
		cycles.put("sub.d", 2);
		cycles.put("s.d", 2);
		cycles.put("l.d", 2);
		
		registerFile.put("F0",new Register("0",1.0));	
		registerFile.put("F1",new Register("0",1.0));
		registerFile.put("F2",new Register("0",1.0));	
		registerFile.put("F4",new Register("0",2.0));
		registerFile.put("F6",new Register("0",1.0));	
		registerFile.put("F8",new Register("0",1.0));
		registerFile.put("F10",new Register("0",1.0));	

		
		registerFile.put("R0",new Register("0",1));	
		registerFile.put("R2",new Register("0",0));
		registerFile.put("R3",new Register("0",1));

		Memory.add(0,1.0);
		Memory.add(1, 2.33);
		nextCycle();
	}
}
