package micro_project;

import java.util.ArrayList;

public class Instruction {
	String type;
	String rs;
	Object j;
	String k;	
	public Instruction(String s) {
		String[] a = s.split(" ");
		this.type = a[0];
		this.rs = a[1];
		this.j = a[2];
		this.k = a[3];	
	}
//s.d f2 0 r3
	public String toString() {
		return type + " " + rs + " " + j + " "+k;
	}
}
