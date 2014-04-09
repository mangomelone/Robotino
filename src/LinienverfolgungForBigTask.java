import java.util.ArrayList;

import rec.robotino.api2.Com;
import rec.robotino.api2.ComId;
import rec.robotino.api2.DigitalInput;
import rec.robotino.api2.DigitalInputArray;
import rec.robotino.api2.OmniDrive;

public class LinienverfolgungForBigTask {
	
	ComId communicationId;
	DigitalInput optischerSensor = new DigitalInput();
	ArrayList<DigitalInput> dis = new ArrayList<DigitalInput>();
	OmniDrive od = new OmniDrive();
	
	
	
	public LinienverfolgungForBigTask()
	{
		verbinden();
//		optischerSensor.setComId(communicationId);
//		optischerSensor.setInputNumber(4);
		od.setComId(communicationId);
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
	
	public void verfolgeLinie()
	{
		int countTurnsInSameDirection = 0;
		
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
	
	
	
	public void verbinden()
	{
		try 
		{
			Com connection = new Com();
			connection.setAddress("172.26.201.2");
			connection.connectToServer(true);
			communicationId = connection.id();
			initializeDigitalInputs();
			Thread.sleep(1000);

		} 
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initializeDigitalInputs() 
	{
		for (int i = 0; i < DigitalInput.numDigitalInputs(); i++) 
		{
			DigitalInput di = new DigitalInput();
			di.setComId(communicationId);
			di.setInputNumber(i);
			dis.add(di);
		}
	}

	public static void main(String[] args) throws InterruptedException
	{
		LinienverfolgungForBigTask lv = new LinienverfolgungForBigTask();
		lv.verfolgeLinie();
	}
}
