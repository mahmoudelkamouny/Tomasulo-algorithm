package micro_project;

public class Register {
	String Qi ="0";
	double content;
	public Register(String qi, double content) {
		Qi = qi;
		this.content = content;
	}
	public void cycleStepRead(String regName) {
		if(Simulator.CDB.containsKey(Qi)) {
			System.out.println("Value ready for register " + regName + " from "+Qi + " value: "+Simulator.CDB.get(Qi));
			this.content=Simulator.CDB.get(Qi);
			Qi ="0";

		}
	}
}
