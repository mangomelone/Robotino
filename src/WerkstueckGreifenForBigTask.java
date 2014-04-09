import java.util.ArrayList;
import java.util.List;

import rec.robotino.api2.AnalogInput;
import rec.robotino.api2.Com;
import rec.robotino.api2.ComId;
import rec.robotino.api2.DigitalInput;
import rec.robotino.api2.ElectricalGripper;
import rec.robotino.api2.Motor;
import rec.robotino.api2.OmniDrive;


public class WerkstueckGreifenForBigTask {
	
	private ComId id;
	private List<DigitalInput> dis = new ArrayList<DigitalInput>();
	private List<AnalogInput> ais = new ArrayList<AnalogInput>();
	private OmniDrive od = new OmniDrive();
	private ElectricalGripper greifer = new ElectricalGripper();
	
	public WerkstueckGreifenForBigTask() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws InterruptedException {
		WerkstueckGreifenForBigTask wg = new WerkstueckGreifenForBigTask();
		wg.verbinden();
//		wg.seitlichFahren();
//		wg.vorwärtsFahren();
//		wg.werkstueckGreifen();

		wg.greiferOeffnen();
//		wg.readAllDigitalInputs();
//		wg.readAllAnalogInputs();

	}
	
	private void readAllAnalogInputs() throws InterruptedException {
		while (true) {
			for (int i = 0; i < AnalogInput.numAnalogInputs(); i++) {
				AnalogInput ai = ais.get(i);
				float value = ai.value();
				System.out.print(value + " ");
			}
			System.out.println();
			Thread.sleep(2000);
		}
	}	
		
	

	public void verbinden() throws InterruptedException {
		Com connection = new Com();
		connection.setAddress("172.26.201.2");
		connection.connectToServer(true);
		id = connection.id();
		initializeDigitalInputs();
		initializeAnalogInputs();
		initializeOmniDrive();
		initializeElectricalGripper();
		Thread.sleep(1000);
	}
	
	private void initializeElectricalGripper() {
		greifer.setComId(id);
		
	}

	private void initializeOmniDrive() {
		od.setComId(id);
		
	}

	private void initializeAnalogInputs() {
		for (int i = 0; i < AnalogInput.numAnalogInputs(); i++) {
			AnalogInput ai = new AnalogInput();
			ai.setComId(id);
			ai.setInputNumber(i);
			ais.add(ai);
		}
		
	}

	private void initializeDigitalInputs() throws InterruptedException {
		
		for (int i = 0; i < DigitalInput.numDigitalInputs(); i++) {
			DigitalInput di = new DigitalInput();
			di.setComId(id);
			di.setInputNumber(i);
			dis.add(di);

		}
		
	}
	
	public void readAllDigitalInputs() throws InterruptedException {
		while (true) {
			for (int i = 0; i < DigitalInput.numDigitalInputs(); i++) {
				DigitalInput di = dis.get(i);
				boolean value = di.value();
				System.out.print(value + " ");
			}
			System.out.println();
			Thread.sleep(2000);
		}
	}
	
	public void moveVorwarts(float xDir) throws InterruptedException {
		
		od.setVelocity(xDir, 0.0f, 0.0f);
		Thread.sleep(1000);
		
	}
	
	public void moveSeitlich(float yDir) throws InterruptedException {
		od.setVelocity(0.0f, yDir, 0.0f);
		Thread.sleep(1000);
	}
	
	public void seitlichFahren() throws InterruptedException {
		while (true) {
			moveSeitlich(-0.05f);
		}
	}
	
	public void rotieren(float degPerSec) throws InterruptedException {
		od.setVelocity(0.0f, 0.0f, degPerSec);
		Thread.sleep(1000);
	}
	
	
	
	public void werkstueckGreifen() throws InterruptedException {
		//boolean optSens = dis.get(4).value();
		boolean kollisionDetektor = dis.get(1).value();
		boolean lichtschranke = dis.get(0).value();
		boolean optisch = dis.get(4).value();
		
		float induktiv = ais.get(0).value();
		
		System.out.print(kollisionDetektor);
		System.out.print(lichtschranke);
		System.out.println(induktiv);
		
		while (induktiv > 7.0) {
			//moveSeitlich(0.025f);
			od.setVelocity(0.0f, -0.025f, -0.0125f);
			Thread.sleep(100);
			
			
//			kollisionDetektor = dis.get(1).value();
//			Thread.sleep(100);
//			System.out.print(kollisionDetektor);
//			
//			lichtschranke = dis.get(0).value();
//			Thread.sleep(100);
//			System.out.print(lichtschranke);
			
			induktiv = ais.get(0).value();
			Thread.sleep(100);
			System.out.println(induktiv);
			
			optisch = dis.get(4).value();
			if (optisch)
			{
				moveVorwarts(0.04f);
				optisch = dis.get(4).value();
				if (optisch) {
					moveVorwarts(-0.08f);
//					rotieren(0.4f);
				}
				else {
//					rotieren(-0.4f);
				}
			}
			
			
		}
		rotieren(0.25f);
		while (!kollisionDetektor) {
			
			moveVorwarts(0.03f);
			
			kollisionDetektor = dis.get(1).value();
			Thread.sleep(100);
		}
		for (int i = 0; i <= 3; i++)
		{
			moveVorwarts(0.03f);
		}
		
		greifer.close();
		Thread.sleep(1000);
		
		
		for (int i = 0; i < 5; i++) 
		{
			moveVorwarts(-0.05f);
			Thread.sleep(500);
		}
		for (int i = 0; i < 5; i++) 
		{
			moveVorwarts(0.05f);
			Thread.sleep(500);
		}
		//moveVorwarts(-0.04f);
	}
	
	public void vorwärtsFahren() throws InterruptedException {
		while (true) {
			moveVorwarts(0.04f);
		}
	}
	
	private void greiferOeffnen() throws InterruptedException {
		greifer.open();
		Thread.sleep(1000);
		System.out.println("greifer is open");
	}

}
