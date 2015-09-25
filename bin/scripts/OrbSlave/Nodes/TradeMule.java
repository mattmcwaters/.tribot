package scripts.OrbSlave.Nodes;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.Game;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Trading;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;

import scripts.OrbSlave.Main.OrbSlave;
import scripts.OrbSlave.Utils.Hopper;

/**
 * Created by Matt on 2015-09-21.
 */
public class TradeMule extends Node {

    RSTile[] geTileArray = {new RSTile(3156,3492),new RSTile(3171, 3493),new RSTile(3173, 3482),new RSTile(3158, 3482)};
    @Override
    public boolean validate(){
        RSArea geArea = new RSArea(geTileArray);
        if(Player.getRSPlayer().getName().equals("Fallnleaves")){
            return false;
        }
        return geArea.contains(Player.getPosition());
    }

    @Override
    public void execute(){
        while(306!=Game.getCurrentWorld()){
            Hopper.switchWorld(6);
            General.sleep(1000,3000);


        }
        if(Inventory.getCount("Air orb")==0 || Inventory.getCount("Amulet of glory")==0){
            if(Banking.isInBank()){
                if(!Banking.isBankScreenOpen()){
                    Banking.openBankBooth();
                }
                General.sleep(1000, 3000);
                Banking.depositAll();
                Mouse.click(323, 325, 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Air orb", 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Amulet of glory", 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Coins", 0);
                General.sleep(200, 600);
                Banking.close();
            }
        }
        String muleName = "Fallnleaves";
        General.sleep(2000,3000);
        RSPlayer mule = Players.find(muleName)[0];
        General.sleep(400,1200);
        long startTime = System.currentTimeMillis();
        while(Trading.getWindowState()==null){
            mule.click("Trade with "+muleName);
            General.sleep(20*1000);


        }



        General.sleep(1000, 3000);
        offerLoop("Air orb");
        General.sleep(1000, 3000);
        Trading.offer(Inventory.getCount("Amulet of glory"), "Amulet of glory");
        General.sleep(1000, 3000);
        Trading.offer(Inventory.getCount("Coins"), "Coins");

        if(Trading.getOpponentName().equals(muleName)){
            waitUntilAccept();
            Trading.accept();
            waitUntilAccept();
            Trading.accept();
            General.sleep(1000, 3000);
            if(Banking.isInBank()) {
                if (!Banking.isBankScreenOpen()) {
                    Banking.openBankBooth();
                }
                General.sleep(1000, 3000);
                Banking.depositAll();
                Banking.close();
            }
            gloryTeleport();

            throw new NullPointerException();


        }
        else{
            System.out.println("Traded wrong person and they accepted!");
            Trading.close();
        }





    }
    public void offerLoop(String itemName){
        while(Inventory.getCount(itemName)>0){
            Trading.offer(0, itemName);
            General.sleep(2000, 5000);

        }

    }
    public void waitUntilAccept(){
        while(!Trading.hasAccepted(true)){
            General.sleep(400, 1000);
        }
    }



    private boolean gloryTeleport() {
/* 177 */     String[] GloryIDs = { "Amulet of glory(1)", "Amulet of glory(2)",
/* 178 */       "Amulet of glory(3)", "Amulet of glory(4)" };
/* 179 */     RSItem[] glories = Equipment.find(GloryIDs);
/*     */
/* 181 */     if (glories.length > 0) {
/* 182 */       RSItem glory = glories[0];
/*     */
/* 184 */       glory.click("Edgeville");
/* 185 */       return true;
/*     */     }
/* 187 */     return false;
/*     */   }

    public void waitUntilTraded(){
        while(Trading.getWindowState()==null){
            General.sleep(10);
        }

    }
}
