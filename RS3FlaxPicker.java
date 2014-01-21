package scripts;

import java.awt.Color;
import java.awt.Point;
import java.awt.*;
import java.text.DecimalFormat;

import org.tribot.api.General;
import org.tribot.api.Screen;
import org.tribot.api.rs3.*;
import org.tribot.api.rs3.types.*;
import org.tribot.api.input.*;
import org.tribot.api.rs3.Skills;
import org.tribot.api.rs3.Skills.*;
import org.tribot.api.rs3.util.ThreadSettings;
import org.tribot.api.rs3.util.ThreadSettings.MODEL_CLICKING_METHOD;
import org.tribot.script.EnumScript;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Painting;
import org.tribot.api.Timing;
import org.tribot.api.util.Restarter;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;


@ScriptManifest(authors = { "dcthugs93" }, category = "Money Making", name = "Picks Flax in Seers")
public class RS3FlaxPicker extends EnumScript<RS3FlaxPicker.STATE> implements Painting
{

	public static 					Font font = AddFont.createFont();
	private final 					Color color = new Color(0,0,0);
	public static final Polygon 	Bank_Area = new Polygon(
															new int[] {2722, 3490, 2729, 3490 },    
															new int[] {2729, 3493, 2721, 3493 },
															4);
	public static final EGWPosition Bank_Pos = new EGWPosition (2724, 3492, 0),
									Flax_Pos = new EGWPosition (2743, 3445, 0);
	public static final long		banker = 3764876361L,
									flax = 2365396454L,
									startTime = System.currentTimeMillis();
	public static final int[] 		loginScreen = {21234, 46069};
	public static final int 		inventoryFull = 24078;
	public static int 				mouseSpeed = General.random(65, 155),
									flaxPicked = 0,
									flaxInventory = 1104812;
	private static 					String Status = "";
	
	
	enum STATE
	{
		CHECK, WALK_TO_FLAX, WALK_TO_BANK, BANK, PICK_FLAX
	}
	
		@Override
	public STATE getInitialState()
	{
		ThreadSettings.get().setScreenModelClickingMethod(MODEL_CLICKING_METHOD.CENTRE);
		AntiBan.startAntiBan();		
		Mouse.setSpeed(General.random(165, 175));


		return STATE.CHECK;
	}

	@Override
	public STATE handleState(STATE t)
	{
		switch (t)
		{
			case CHECK:
			ScreenModel[] flaxToPick = ScreenModels.findNearest(flax);
			EGWPosition PlayerPOS = EGW.getPosition();			

			try {
				if (font == null) {
					font = AddFont.createFont();
                }
           } catch (Exception e) {
				e.printStackTrace();
           }
			if(loggedOut()){ 	
				Restarter.restartClient(); 
			}else
			if(inventoryIsFull()){
				if(Bank_Area.contains(PlayerPOS)){
					Status = "Depositing Flax";
					return STATE.BANK;
				} else
				if(PlayerPOS.distance(Bank_Pos) > 6){
					Status = "Walking To Bank";
					return STATE.WALK_TO_BANK;
				}					
			}else
			if(PlayerPOS.distance(Flax_Pos) > 10 && !inventoryIsFull()){
				Status = "Walking to Flax";
				return STATE.WALK_TO_FLAX;
			}else
			if(PlayerPOS.distance(Flax_Pos) < 10 && flaxToPick.length > 0 && !inventoryIsFull()) {
				General.println("flax ENUM");
				Status = "Picking Flax";
				return STATE.PICK_FLAX;
			}			
			
			case PICK_FLAX:
			EGWPosition PlayerPOS4 = EGW.getPosition();
			General.println("flax CASE");
			if(PlayerPOS4.distance(Flax_Pos) < 8 && !inventoryIsFull()){
					General.println("Flax LOOOP");
					PickFlax.pickFlax();
					if(inventoryIsFull()){
						return STATE.WALK_TO_BANK;
					}
			}
			return STATE.CHECK;
			
			case WALK_TO_BANK:
			EGWPosition PlayerPOS2 = EGW.getPosition();
			if(PlayerPOS2.distance(Bank_Pos) > 6){
				WalkingMethods.walkToBank();
			}else
			if(PlayerPOS2.distance(Bank_Pos) < 6){
				return STATE.BANK;
			}
			return STATE.WALK_TO_BANK;
			
			case BANK:
			while(Backpack.find(flaxInventory).length > 0){
				Methods.bank();
			} 
			return STATE.WALK_TO_FLAX;
			
			case WALK_TO_FLAX:
			EGWPosition PlayerPOS3 = EGW.getPosition();
			if(PlayerPOS3.distance(Flax_Pos) > 6){
				WalkingMethods.walkToFlax();
			}else
			if(PlayerPOS3.distance(Flax_Pos) <= 6){
				return STATE.PICK_FLAX;
			}
			break;		

		}
		return STATE.CHECK;
	}
	
	/*Backpack.isFull() is broken so I use this*/
	public static boolean inventoryIsFull(){
		TextChar[] fullInventory = Text.findCharsInArea(5, 465, 270, 150, false);
		String x = Text.lineToString(fullInventory, 10000);
		boolean b = x.contains("YYoouuccaann''tt");
		if(b){
			return true;
		}return false;
	}
	
	public static boolean loggedOut(){
		Texture[] loggedOut = Textures.find(loginScreen);
		return loggedOut.length > 0;
	}
	
	    @Override
    public void onPaint(Graphics g) {
		
        long timeRan = System.currentTimeMillis() - startTime;
		
		
		DecimalFormat df = new DecimalFormat("#,###");
	
        g.setFont(font);
        g.setColor(color);
        g.drawString("Time Ran: " + Timing.msToString(timeRan), 10, 30);
		g.drawString("Status: " + Status + "  ", 10, 60);
		g.drawString("APX. Flax Picked: " + flaxPicked + "  ", 10, 90);
		g.drawString("This is in Beta Stage, Report Bugs", 10, 120);
		g.drawString("dcthugs93 RS3 Seers Flax Picker", 10, 150);

    }	

	
}
		
	