import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rec.robotino.api2.Com;
import rec.robotino.api2.ComId;
import rec.robotino.api2.DigitalInput;
import rec.robotino.api2.DigitalInputArray;
import rec.robotino.api2.DigitalOutput;
import rec.robotino.api2.DigitalOutputArray;
import rec.robotino.api2.DistanceSensor;
import rec.robotino.api2.DistanceSensorArray;
import rec.robotino.api2.ElectricalGripper;
import rec.robotino.api2.Grappler;
import rec.robotino.api2.GrapplerReadings;
import rec.robotino.api2.Motor;
import rec.robotino.api2.OmniDrive;
import rec.robotino.api2.SWIGTYPE_p_float;
import rec.robotino.api2.SWIGTYPE_p_int;

public class Wandverfolgung {
	private ComId id;
	
	
	
	List<DistanceSensor> lds = new ArrayList<DistanceSensor>();
	
	List<DigitalInput> dis = new ArrayList<DigitalInput>();
	
	private void initializeDigitalInputs() {
		for (int i = 0; i < DigitalInput.numDigitalInputs(); i++) {
			DigitalInput di = new DigitalInput();
			di.setComId(id);
			di.setInputNumber(i);
			dis.add(di);
		}
	}

	private void initializeDistanceSensors() {
		for (int i = 0; i <= 8; i++) {
			if (i == 8 || i == 0 || i == 1)
			{
				DistanceSensor ds = new DistanceSensor();
				ds.setComId(id);
				ds.setSensorNumber(i);
				lds.add(ds);
			}
		}
	}

	

	public Wandverfolgung() {

	}

	public void getIRSensoren() throws InterruptedException {
		for (int i = 0; i < 3; i++) {

			float distance = lds.get(i).distance();
			System.out.print(distance + "   ");
		}
		System.out.println();
		Thread.sleep(1000);
	}

	public void moveVorwarts() throws InterruptedException {
		OmniDrive od = new OmniDrive();
		od.setComId(id);
		od.setVelocity(0.10f, 0.0f, 0.0f);

		// Motor m1 = new Motor();
		// m1.setComId(id);
		// m1.setMotorNumber(0);
		//
		// Motor m2 = new Motor();
		// m2.setComId(id);
		// m2.setMotorNumber(1);
		//
		// Motor m3 = new Motor();
		// m3.setComId(id);
		// m3.setMotorNumber(2);
		Thread.sleep(1000);

	}

	public void rotate() throws InterruptedException {
		OmniDrive od = new OmniDrive();
		od.setComId(id);
		od.setVelocity(0.0f, 0.0f, -0.32f);

		Thread.sleep(1000);
	}

	public void readAllDigitalInputs() throws InterruptedException {
		for (int i = 0; i < DigitalInput.numDigitalInputs(); i++) {
			DigitalInput di = dis.get(i);
			boolean value = di.value();
			System.out.print(value + " ");
		}
		System.out.println();
		Thread.sleep(1000);
	}

	public void greiferÖffnen() throws InterruptedException {
		readAllDigitalInputs();
		ElectricalGripper greifer = new ElectricalGripper();
		greifer.setComId(id);
		if (greifer.isOpened()) {
			greifer.close();

			// greifer.stateChangedEvent(ElectricalGripper.IsClosed);
			while (greifer.isOpened()) {

			}

		} else {
			greifer.open();

			// greifer.stateChangedEvent(ElectricalGripper.IsOpen);
			while (!greifer.isOpened()) {

			}
		}
		Thread.sleep(1000);
		readAllDigitalInputs();

	}

	public void verbinden() throws InterruptedException {
		Com connection = new Com();
		connection.setAddress("172.26.201.2");
		connection.connectToServer(true);
		id = connection.id();
		initializeDistanceSensors();
		initializeDigitalInputs();
		Thread.sleep(1000);
	}

	public void wandverfolgung() {
		try {
			verbinden();
			getIRSensoren();

			while (true) {

				while (getNearestIRSensorValue() > 0.1)
				{
					moveVorwarts();
					getIRSensoren();
				}
				int irSensorNumber = getNearestIRSenorNumber();
				float currentValue = getValueFromSensor(irSensorNumber);

				while (currentValue < 0.1) 
				{
					rotate();
					currentValue = getValueFromSensor(irSensorNumber);
				}
			}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private float getValueFromSensor(int sensorNumber)
	{
		float value = lds.get(sensorNumber).distance();
		return value;
	}

	private int getNearestIRSenorNumber() {
		int minIndex = -1;
		ArrayList<Float> distanceSensorsValues = new ArrayList<Float>();
		for (DistanceSensor ds : lds) {

			float latestValue = ds.distance();
			distanceSensorsValues.add(latestValue);
			// if (latestValue < minIndex)
			// {
			// minIndex = latestValue;
			// }
		}
		minIndex = distanceSensorsValues.indexOf(Collections
				.min(distanceSensorsValues));
		return minIndex;
	}

	private float getNearestIRSensorValue() {
		float minValue = 1;
		for (DistanceSensor ds : lds) {
			float latestValue = ds.distance();
			if (latestValue < minValue) {
				minValue = latestValue;
			}
		}
		return minValue;
	}

	public static void main(String[] args) throws InterruptedException {
		Wandverfolgung m = new Wandverfolgung();
//		m.wandverfolgung();
		m.verbinden();
		while (true)
		{
			m.getIRSensoren();
		}

		// try {
		// m.verbinden();
		// m.getIRSensoren();
		// m.greiferÖffnen();
		// m.moveVorwarts();
		// m.readAllDigitalInputs();
		// m.getIRSensoren();
		// }
		// catch (InterruptedException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
