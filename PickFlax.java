package scripts;

import org.tribot.api.input.Mouse;
import org.tribot.api.rs3.Player;
import org.tribot.api.rs3.types.EGWPosition;
import org.tribot.api.rs3.EGW;
import org.tribot.api.rs3.util.ThreadSettings;
import org.tribot.api.rs3.util.ThreadSettings.MODEL_CLICKING_METHOD;
import org.tribot.api.General;
import org.tribot.api.rs3.ScreenModels;
import org.tribot.api.rs3.types.ScreenModel;
import org.tribot.api.rs3.types.*;
import org.tribot.api.rs3.*;


public class PickFlax{
	
	public static void pickFlax()  {
		TextChar[] flaxPicked = Text.findCharsInArea(5, 465, 270, 150, false);
		String x = Text.lineToString(flaxPicked, 10000);
		boolean b = x.contains("YYoouuppiicckkssoommeeffllaaxx");
		ScreenModel[] flax = ScreenModels.findNearest(RS3FlaxPicker.flax);  
		ScreenModel flax1 = RandomizedClicking.getClosestModel(RS3FlaxPicker.flax);
		EGWPosition PlayerPOS = EGW.getPosition();
		if(!RS3FlaxPicker.inventoryIsFull()){
			if (flax.length > 0)	{	
				General.println("Found Flax");
				if(flax[0].isClickable(MODEL_CLICKING_METHOD.CENTRE)){		
					General.println("The Flax is clickable");
					Mouse.setSpeed(RS3FlaxPicker.mouseSpeed);
					if(RandomizedClicking.clickScreenModel(flax1, "Pick", -2, 6, -3, 9)){
						General.println("click successful");
						General.sleep(100, 200);
					}
					while(Player.getAnimation() > 0 || Player.isMoving()) { 
						if(b){
							RS3FlaxPicker.flaxPicked++;
						}
					}

				}
				RandomizedCameraMovements.randomCameraRotation(); General.sleep(1500, 2000);
			
			}
		}
	}
}