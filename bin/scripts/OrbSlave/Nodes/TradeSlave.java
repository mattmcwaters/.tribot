package scripts.OrbSlave.Nodes;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.Players;
import org.tribot.api2007.Trading;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.interfaces.MessageListening07;

import java.util.ArrayList;
import java.util.List;

import scripts.OrbSlave.Main.OrbSlave;
import scripts.OrbSlave.Utils.Hopper;

/**
 * Created by Matt on 2015-09-22.
 */
public class TradeSlave extends Node  {
    ArrayList<String> slaveNames = new ArrayList<String>();
    String lastTrade=null;
    public boolean validate(){

        return Player.getRSPlayer().getName().equals("Fallnleaves");
    }

    @Override
    public void execute(){
        while(306!= Game.getCurrentWorld()){
            Hopper.switchWorld(6);
            General.sleep(1000,3000);

        }
        slaveNames.add("hermoineme");
        slaveNames.add("RolePlayDie");
        slaveNames.add("simplejacque");
        slaveNames.add("earlyboird");

        getMats();
        General.sleep(1000,3000);
        if (!Player.getPosition().equals(new RSTile(3165, 3485))) {
            Walking.walkTo(new RSTile(3165, 3485));
        }

        long startTime = System.currentTimeMillis();
        while (OrbSlave.tradedCount<4){
            waitUntilTraded();
            tradeMats();
        }


    }
    public void waitUntilTraded(){
        while(Trading.getWindowState()==null){
            General.sleep(10);
        }

    }
    public void tradeMats(){
        while(Trading.getCount(true, "Air orb")==0){
            General.sleep(100);
        }
        General.sleep(3000, 5000);
        Trading.offer(20, "Tuna");
        General.sleep(200, 600);
        int orbReceived = Trading.getCount(true, "Air orb");
        int gloryPotCount = (orbReceived/100)+1;
        int cosmicCount = orbReceived*3;
        Trading.offer(gloryPotCount, "Amulet of glory(4)");
        General.sleep(200, 600);
        Trading.offer(gloryPotCount, "Stamina potion(4)");
        General.sleep(200, 600);
        Trading.offer(cosmicCount, "Cosmic rune");
        General.sleep(200, 600);
        Trading.offer(orbReceived, "Unpowered orb");
        General.sleep(200, 600);
        Trading.accept();
        General.sleep(1000, 2000);

        Trading.accept();
        OrbSlave.tradedCount++;
    }
    public void getMats(){
        if(Inventory.getCount("Unpowered orb")==0
            || Inventory.getCount("Amulet of glory(4)")==0
            || Inventory.getCount("Cosmic rune")==0
            || Inventory.getCount("Tuna")==0
            || Inventory.getCount("Stamina potion(4)")==0){

            if(Banking.isInBank()){
                if(!Banking.isBankScreenOpen()){
                    Banking.openBankBooth();
                }
                General.sleep(1000, 3000);
                Banking.depositAll();
                Mouse.click(323, 325, 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Unpowered orb", 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Amulet of glory(4)", 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Cosmic rune", 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Tuna", 0);
                General.sleep(200, 600);
                WalkToGe.withdrawItem("Stamina potion(4)", 0);
                General.sleep(200, 600);
                Banking.close();
            }
        }
    }



}
