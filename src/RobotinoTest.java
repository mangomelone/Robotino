import java.util.ArrayList;
import java.util.List;

import rec.robotino.api2.Com;
import rec.robotino.api2.ComId;
import rec.robotino.api2.DigitalInput;
import rec.robotino.api2.OmniDrive;


public class RobotinoTest {
	
	ComId communicationId;
	OmniDrive od = new OmniDrive();
	List<DigitalInput> din = new ArrayList<DigitalInput>();
	
	public RobotinoTest()
	{
		verbinden();
		od.setComId(communicationId);
		fahreBisMarkierung();
	}
	
	public void fahre()
	{
		while (true)
		{
			long startTime = System.currentTimeMillis();
			od.setVelocity(0.05f, 0.0f, 0.0f);
			try 
			{
				Thread.sleep(10);
			} 
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			double durationInSek = duration / 1000;
			System.out.println("Der Fahrbefehl dauert " + durationInSek
					+ " Sekunden.");
		}
	}
	
	public void fahreBisMarkierung()
	{
		DigitalInput di = new DigitalInput();
		di.setComId(communicationId);
		di.setInputNumber(4);
		try 
		{
			Thread.sleep(1000);
			boolean value = di.value();
			while (!value)
			{
				od.setVelocity(0.04f, 0.0f, 0.0f);
				Thread.sleep(10);
				System.out.println(di.value());
				value = di.value();
//				Thread.sleep(10);
//				for (DigitalInput di2 : din)
//				{
//					boolean value2 = di2.value();
//					Thread.sleep(1000);
//					System.out.print(value + "  ");
//				}
//				System.out.println();
//				Thread.sleep(1000);
			}
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RobotinoTest rt = new RobotinoTest();
	
	}
	
	private void initializeDigitalInputs() {
		for (int i = 0; i < DigitalInput.numDigitalInputs(); i++) {
			DigitalInput di = new DigitalInput();
			di.setComId(this.communicationId);
			di.setInputNumber(i);
			din.add(di);
		}
	}
	
	public void verbinden()
	{
		try 
		{
			Com connection = new Com();
			connection.setAddress("172.26.201.2");
			connection.connectToServer(true);
			communicationId = connection.id();
			this.initializeDigitalInputs();
			Thread.sleep(1000);
		} 
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
