import com.epicbot.api.ActiveScript;
import com.epicbot.api.GameType;
import java.awt.Graphics2D;
import java.util.Random;
import com.epicbot.*;
import com.epicbot.api.Manifest;
import com.epicbot.api.concurrent.Task;
import com.epicbot.api.concurrent.node.Node;
import com.epicbot.api.os.methods.Walking;
import com.epicbot.api.os.methods.interactive.NPCs;
import com.epicbot.api.os.methods.interactive.Players;
import com.epicbot.api.os.methods.node.SceneEntities;
import com.epicbot.api.os.methods.tab.Skills;
import com.epicbot.api.os.methods.tab.inventory.Inventory;
import com.epicbot.api.os.methods.widget.Bank;
import com.epicbot.api.os.methods.widget.Camera;
import com.epicbot.api.os.wrappers.Locatable;
import com.epicbot.api.os.wrappers.Tile;
import com.epicbot.api.os.wrappers.node.Item;
import com.epicbot.api.rs3.methods.widget.Bank.BankLocations;
import com.epicbot.api.shared.wrappers.Entity;
import com.epicbot.api.util.Time;
import com.epicbot.api.util.Timer;
import com.epicbot.client.os.api.SceneObject;
import com.epicbot.client.os.input.Mouse;
import com.epicbot.api.os.wrappers.interactive.Character;
import com.epicbot.api.os.wrappers.interactive.Player;

public class Catherby_Fishing_2 extends ActiveScript{
@Manifest(author="Robert Strickland", game = GameType.OldSchool, name = "Testing :]", description = "Trying to make the first script!")
//	
//	Variable Declarations
//
	public Random rand = new Random();
	public Camera cam = new Camera();
	public Bank bank = new Bank();
	public Inventory bag = new Inventory();
	public String tuna_action = "Harpoon";
	public String lobster_action = "Cage";
	public int fishing_pool_NPC_id = 1519;
	public Tile catherby_bank_coords = new Tile(2809, 3439);
	public Tile fishing_spot_0 = new Tile(2837, 3432);
	public Tile fishing_spot_1 = new Tile(2845, 3430);
	public Tile fishing_spot_2 = new Tile(2853, 3424);
	public int fishing_spot_counter = 0;
	public void onStop(){System.out.println("Script Stopping! Goodbye");}
	public BankTask banktask = new BankTask();
	public FishTask fish = new FishTask();
	public Player me = Players.getLocal();
	public int shake_counter = 1;
	private static final char[] Player = null;
	
	public boolean onStart() 
	{
		provide(new FishTask());
		provide(new BankTask());
	return true;
	}
	public void shakeCamera()
	{
		int x = rand.nextInt(2);
		int cp = rand.nextInt(10);
		int ca = rand.nextInt(10);
		
		System.out.println(x);
		if(x == 0)
		{
		//	System.out.println("Staying Alive!");
			if(shake_counter == 1){
				cam.setPitch(cam.getPitch()+cp);
				cam.setAngle(cam.getAngle()+ca);
				shake_counter = 0;}
			else
			{
				cam.setPitch(cam.getPitch()-cp);
				cam.setAngle(cam.getAngle()-ca);
				shake_counter = 1;
			}
		}
		else if(x == 1)
		{
			int xx = rand.nextInt(250);
			int yy = rand.nextInt(250);
			com.epicbot.api.input.Mouse.move(xx, yy);
		}
			
	}
	public class FishTask extends Node implements Task
	{
	
		public void initiateFishing()
		{
			System.out.println("------------------------------------------------------------------------");
			System.out.println("Initiating fishing");
			try
			{
				System.out.println("Testing for pool = null and distance > 4.0");
				if((NPCs.getNearest(fishing_pool_NPC_id).isOnScreen()) && (NPCs.getNearest(fishing_pool_NPC_id).getLocation().distanceTo() > 4.0))
				{
					System.out.println("Test true, moving to new spot");
					System.out.println("------------------------------------------------------------------------");
					fishing_spot_counter = fishing_spot_counter + 1;
					moveToFishingSpots();
				}
				else
				{
					System.out.println("Test failed, fishing");
					System.out.println("------------------------------------------------------------------------");
					fish();
				}
			}catch(NullPointerException e)
			{
				System.out.println("Caught null pointer @" + e);
				System.out.println("------------------------------------------------------------------------");
				fishing_spot_counter = fishing_spot_counter + 1;
				moveToFishingSpots();
			}
		}
		@SuppressWarnings("deprecation")
		public void fish()
		{
			if(!Inventory.isFull())
			{
				Time.sleep(1000);
				System.out.println("Fishing");
				if(!NPCs.getNearest(1519).interact(tuna_action))
					moveToFishingSpots();
				fishing_spot_counter = 0;
				int x = 7000 + rand.nextInt(3000);
				Time.sleep(x);
				keepFishing();
			}
			else
			{
				banktask.moveToBank();
			}
		}		
		
		public void keepFishing()
		{
			System.out.println("------------------------------------------------------------------------");
			int idle = 0;
			while(idle == 0)
			{
				shakeCamera();
				if(me.isIdle())
				{
					System.out.println("Incorrect Idle");
					Time.sleep(500);
					System.out.print("Double checking: ");
					if(me.isIdle())
					{
						System.out.println("Nope I'm still idle");
						System.out.println("------------------------------------------------------------------------");
						break;
					}
					idle = 1;
				}
				Time.sleep(7000);
			}
			System.out.println("------------------------------------------------------------------------");
			initiateFishing();
		}

		@SuppressWarnings("deprecation")
		public void moveToFishingSpots()
		{
			System.out.println("------------------------------------------------------------------------");
			System.out.print("moving to fishing spots");
			if(fishing_spot_counter == 3)
				fishing_spot_counter = 0;
			if(Walking.isRunEnabled() == false) //Checking if run is toggled
				Walking.setRun(true);//Setting run to true if not toggled
			Time.sleep(1000);
			switch(fishing_spot_counter)
			{
				case 0:
					System.out.println("Moving to fishing spot1");
					cam.setPitch(68);cam.setAngle(272);
					Walking.walkTo(fishing_spot_0.getLocation());
					Time.sleep(5000);
					break;
				case 1:
					System.out.println("Moving to fishing spot2");
					cam.setPitch(68);cam.setAngle(272);
					Walking.walkTo(fishing_spot_1.getLocation());
					Time.sleep(5000);
					break;
				case 2:
					System.out.println("Moving to fishing spot3");
					cam.setPitch(68);cam.setAngle(82);
					Walking.walkTo(fishing_spot_2.getLocation());
					Time.sleep(5000);
					break;
			}
			System.out.println("------------------------------------------------------------------------");
			initiateFishing();
		}
		public void run() {
			moveToFishingSpots();
		}
		public boolean shouldExecute()
		{
			return true;
		}
		
	}
	
	public class BankTask extends Node implements Task
	{

		private boolean depositFish()
		{
			System.out.println("------------------------------------------------------------------------");
			//311 - harpoon
			//356 - tuna
			//301 - lobster pot
			SceneEntities.getNearest("Bank booth").interact("Bank");
			Time.sleep(3000);
			
			if(!bank.isOpen())
				depositFish();
			bank.depositAllExcept(311);
			//bank.depositAllExcept(301);
			System.out.print("Depositing Finished, Going back to fishing");
			System.out.println("------------------------------------------------------------------------");
			fish.moveToFishingSpots();
			return true;
			
		}
		public void moveToBank()
		{
			System.out.println("------------------------------------------------------------------------");
			//(2809, 3439) Catherby Bank Tile coords
			if(Walking.isRunEnabled() == false) //Checking if run is toggled
				Walking.setRun(true);//Setting run to true if not toggled
			Time.sleep(2000);
			
			if(SceneEntities.getNearest("Bank booth").isOnScreen() && SceneEntities.getNearest("Bank booth").getLocation().distanceTo() < 5)
			{
				System.out.println("Depositing Fish...");
				System.out.println("------------------------------------------------------------------------");
				depositFish();
			}
			else
			{
				System.out.println("Walking to Bank...");
				System.out.println("------------------------------------------------------------------------");
				Walking.walkTo(catherby_bank_coords.getLocation());
				Time.sleep(3500);
				moveToBank();
			}
		}
		public void run() {
			// TODO Auto-generated method stub
			
		}
		public boolean shouldExecute()
		{
			return true;
		}
		
	}
	class AntiBan extends Node implements Task {

        public void run() {
            if(rand.nextInt(50) == 0);
                
        }
        
        public boolean shouldExecute() {
            return Players.getLocal().isInCombat();
        }
    }

}
