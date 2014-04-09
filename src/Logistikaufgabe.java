import java.util.ArrayList;
import java.util.List;

import rec.robotino.api2.AnalogInput;
import rec.robotino.api2.Com;
import rec.robotino.api2.ComId;
import rec.robotino.api2.DigitalInput;
import rec.robotino.api2.ElectricalGripper;
import rec.robotino.api2.OmniDrive;


public class Logistikaufgabe {
	
//	private final LinienverfolgungForBigTask lv;
//	private final WerkstueckGreifenForBigTask wg;
	
	private ComId id;
	private ElectricalGripper greifer;
	private OmniDrive od;
	
	private List<AnalogInput> ais = new ArrayList<AnalogInput>();
	private List<DigitalInput> dis = new ArrayList<DigitalInput>();
	
	private int countTurnsInSameDirection = 0;
	
	public Logistikaufgabe() {
//		lv = new LinienverfolgungForBigTask();
//		wg = new WerkstueckGreifenForBigTask();
		
		greifer = new ElectricalGripper();
		od = new OmniDrive();
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
//	public LinienverfolgungForBigTask getLv() {
//		return lv;
//	}
//
//	public WerkstueckGreifenForBigTask getWg() {
//		return wg;
//	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Logistikaufgabe la = new Logistikaufgabe();
		la.logistikAufgabe();
	}
	
	public void logistikAufgabe() throws InterruptedException {
		verbinden();
		float induktiverSensor = ais.get(0).value();
		boolean optischerSensor = dis.get(4).value();
		while (true) {
		while (induktiverSensor > 7.0) {
			verfolgeLinie();
			induktiverSensor = ais.get(0).value();
			optischerSensor = dis.get(4).value();
			System.out.println("Analog: " + induktiverSensor + " Digital: " + optischerSensor);
		}
		werkstueckGreifen();
		induktiverSensor = ais.get(0).value();
		}
		
	}
	
	public void verfolgeLinie()
	{
		
		
		DigitalInput di = dis.get(4);
		
//		while (true)
//		{
			try 
			{
				boolean value = di.value();
				Thread.sleep(10);
				System.out.print(value + "   ");
				int intValue = 0;
				intValue = value ? 1 : -1;
				System.out.println(intValue);
				if (value == true)
				{
					od.setVelocity(0.04f, 0.0f, 0.0f);
					Thread.sleep(100);
					countTurnsInSameDirection = 0;
				}
				else if (countTurnsInSameDirection == 5)
				{
					od.setVelocity(0.0f, 0.0f, 0.6f);
					Thread.sleep(1000);
					od.setVelocity(0.0f, 0.0f, 0.6f);
					Thread.sleep(1000);
					countTurnsInSameDirection = 0;
				}
				else
				{
					od.setVelocity(0.0f, 0.0f, -0.2f);
					Thread.sleep(1000);
					countTurnsInSameDirection++;
				}
			
			
//				od.setVelocity(0.10f, 0.0f, 0.0f);
//				Thread.sleep(1000);
				
				
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//		}
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
		
		int sideDirection = 0;
		if (lichtschranke = true) {
			sideDirection = -1;
		}
		else {
			sideDirection = 1;
		}
		
		for (int i = 0; i < 2; i++) 
		{
			od.setVelocity(0.0f, -0.025f * sideDirection, -0.0125f * sideDirection);
			Thread.sleep(500);
		}
		
		induktiv = ais.get(0).value();
		
		while (induktiv > 7.0) {
			//moveSeitlich(0.025f);
			od.setVelocity(0.0f, -0.025f * sideDirection, -0.0125f * sideDirection);
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
		rotieren(0.25f * sideDirection);
		while (!kollisionDetektor) {
			
			moveVorwarts(0.03f);
			
			kollisionDetektor = dis.get(1).value();
			Thread.sleep(100);
		}
		for (int i = 0; i <= 3; i++)
		{
			moveVorwarts(0.03f);
		}
		if (lichtschranke) {
			greifer.open();
			Thread.sleep(1000);
			
		}
		else {
			greifer.close();
			Thread.sleep(1000);
			
		}
		
		for (int i = 0; i < 5; i++) 
		{
			moveVorwarts(-0.05f);
			Thread.sleep(80);
		}
		for (int i = 0; i < 6; i++) 
		{
			rotieren(0.6f * sideDirection);
			Thread.sleep(100);
		}
		while (!optisch) {
			moveVorwarts(0.04f);
			Thread.sleep(100);
			optisch = dis.get(4).value();
		}
		for (int i = 0; i < 4; i++) 
		{
			moveVorwarts(0.04f);
			Thread.sleep(80);
		}
		optisch = dis.get(4).value();
		while (!optisch) {
			rotieren(0.4f * sideDirection);
			Thread.sleep(100);
			optisch = dis.get(4).value();
		}
		
		//moveVorwarts(-0.04f);
	}
	
	public void moveVorwarts(float xDir) throws InterruptedException {
		
		od.setVelocity(xDir, 0.0f, 0.0f);
		Thread.sleep(1000);
		
	}
	
	public void moveSeitlich(float yDir) throws InterruptedException {
		od.setVelocity(0.0f, yDir, 0.0f);
		Thread.sleep(1000);
	}
	
	public void rotieren(float degPerSec) throws InterruptedException {
		od.setVelocity(0.0f, 0.0f, degPerSec);
		Thread.sleep(1000);
	}

}
