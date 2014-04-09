import rec.robotino.api2.ComId;
import rec.robotino.api2.OmniDrive;


public class VorwaertsFahrenThread extends Thread
{
	OmniDrive od;
	ComId comId;
	
	public VorwaertsFahrenThread(ComId id)
	{
		this.comId = comId;
		od = new OmniDrive();
		od.setComId(comId);
	}
	public void run()
	{
		while (true) 
		{
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
		}
	}
}
