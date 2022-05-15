package micro_project;

public class ResStation {
	int id;
	String op;
	double Vj=-1;
	double Vk=-1;
	String Qj;
	String Qk;
	int A;
	boolean busy;
	int time;
	public ResStation(int id, String op, boolean busy, int time) {
		super();
		this.op = op;
		this.busy = busy;
		this.time = time;
		this.id = id;
	}
	public void cycleStepWrite() {
		if(op.toLowerCase().equals("mul.d")) {
			if(Vj!=-1 && Vk!=-1) {
				if(time>0) {
					time--;
					System.out.println("Executing mul instruction, cycles remaining: "+time );

				}
				else {
					System.out.println("Finished executing mul instruction");
					if(Simulator.CDB.size()<1) {
						System.out.println("Writing mul instruction with Tag: "+"M"+id + " Value: " + Vj*Vk);
						busy = false;
						Simulator.CDB.put("M"+id,Vj*Vk);
					}
					else {
						System.out.println("Stalled mul instruction due to write conflict");
					}
				}
			}
			else {
				System.out.println("Mul instruction stalled because operands not ready");
			}
		}
		if(op.toLowerCase().equals("div.d")) {
			if(Vj!=-1 && Vk!=-1) {
				if(time>0) {
					time--;
					System.out.println("Executing div instruction, cycles remaining: "+time );
				}
				else {
					System.out.println("Finished executing div instruction");
					if(Simulator.CDB.size()<1) {
						System.out.println("Writing div instruction with Tag: "+"M"+id + " Value: " + Vj/Vk);
						busy = false;
						Simulator.CDB.put("M"+id, Vj/Vk);	
					}
					else {
						System.out.println("Stalled div instruction due to write conflict");
					}
				}
			}
			else {
				System.out.println("Div instruction stalled because operands not ready");
			}
		}
		if(op.toLowerCase().equals("add.d")) {
			if(Vj!=-1 && Vk!=-1) {
				if(time>0) {
					time--;
					System.out.println("Executing add instruction, cycles remaining: "+time );
				}
				else {
					System.out.println("Finished executing add instruction");
					if(Simulator.CDB.size()<1) {
						System.out.println("Writing add instruction with Tag: "+"A"+id + " Value: " + (Vj+Vk));
						busy = false;
						Simulator.CDB.put("A"+id, Vj+Vk);	
					}
					else {
						System.out.println("Stalled add instruction due to write conflict");
					}
				}
			}
			else {
				System.out.println("Add instruction stalled because operands not ready");
			}
		}
		if(op.toLowerCase().equals("sub.d")) {
			if(Vj!=-1 && Vk!=-1) {
				if(time>0) {
					time--;
					System.out.println("Executing sub instruction, cycles remaining: "+time );
				}
				else {
					System.out.println("Finished executing sub instruction");
					if(Simulator.CDB.size()<1) {
						System.out.println("Writing sub instruction with Tag: "+"A"+id + " Value: " + (Vj-Vk));
						busy = false;
						Simulator.CDB.put("A"+id, Vj-Vk);	
					}
					else {
						System.out.println("Stalled sub instruction due to write conflict");
					}
				}
			}
			else {
				System.out.println("Sub instruction stalled because operands not ready");
			}
		}
		if(op.toLowerCase().equals("l.d")) {
			if(time>0) {
				time--;
				System.out.println("Executing load instruction, cycles remaining: "+time );
			}
			else {
				System.out.println("Finished executing load instruction");
				if(Simulator.CDB.size()<1) {
					System.out.println("Writing load instruction with Tag: "+"L"+id + " Value: " + Simulator.Memory.get(A));
					busy = false;
					Simulator.CDB.put("L"+id, Simulator.Memory.get(A));		
				}
				else {
					System.out.println("Stalled load instruction due to write conflict");
				}
			}

		}
		if(op.toLowerCase().equals("s.d")) {
			if(Vj!=-1 && Vk!=-1) {
				if(time>0) {
					time--;
					System.out.println("Executing store instruction, cycles remaining: "+time );
				}
				else {
					Simulator.Memory.add(A, Vj);
//					System.out.println(Simulator.Memory.get(A));
					System.out.println("Finished executing store instruction writing " + Simulator.Memory.get(A) + " in mem address "+A);
					busy = false;	
				}
			}
			else {
				System.out.println("Store instruction stalled because operands not ready");
			}
		}
	}
	public void cycleStepRead() {
		if(Qj != null && Simulator.CDB.containsKey(Qj)) {
			System.out.println("Operands ready for " + op + " intruction from " + Qj + " value: " + Simulator.CDB.get(Qj));
			Vj=Simulator.CDB.get(Qj);
			Qj ="0";
		}
		if(Qk != null && Simulator.CDB.containsKey(Qk)) {
			System.out.println("Operands ready for " + op + " intruction from " + Qk + " value: " + Simulator.CDB.get(Qk));
			Vk=Simulator.CDB.get(Qk);
			Qk ="0";

		}
	}
	public String toString() {
		return "id:" + id +"\nop:" + op + "\nVj:" + Vj+"\nVk:" + Vk + "\nQj:" + Qj + "\nQk:" + Qk + "\nA:" + A + 
				"\nbusy:" + busy +  "\ntime:" + time; 

	}
}
