package scripts.OrbSlave.Nodes;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;
import org.tribot.api.rs3.WebWalking;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.Walking;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSTile;

import java.util.Random;


/**
 * Created by Matt on 2015-09-21.
 */
public class WalkToGe extends Node{
    RSTile[] pathToGe = {new RSTile(3101, 3499), new RSTile(3103,3504),
        new RSTile(3119,3507),new RSTile(3132, 3505),
        new RSTile(3132, 3490), new RSTile(3132,3476),
        new RSTile(3139, 3466), new RSTile(3143, 3455), new RSTile(3156, 3463),
        new RSTile(3164, 3474), new RSTile(3165,3485)};

    RSTile[] edgebankTiles = {new RSTile(3090,3499,0),
        new RSTile(3100,3499,0),  new RSTile(3100, 3487, 0), new RSTile(3089,3487,0)};
    RSArea edgeBank = new RSArea(edgebankTiles);

    @Override
    public boolean validate(){
        if(Player.getRSPlayer().getName().equals("Fallnleaves")){
            return false;
        }
        RSTile playerPosition = Player.getPosition();
        if(edgeBank.contains(playerPosition)){
            return true;
        }
        return false;
    }

    @Override
    public void execute(){
        General.sleep(500, 1000);
        if(Inventory.getCount("Air orb")==0 || Inventory.getCount("Amulet of glory")==0){
            if(Banking.isInBank()){
                if(!Banking.isBankScreenOpen()){
                    Banking.openBankBooth();
                }
                General.sleep(1000, 3000);
                Banking.depositAll();
                Mouse.click(323, 325, 0);
                General.sleep(200, 600);
                withdrawItem("Air orb", 0);
                General.sleep(200, 600);
                withdrawItem("Amulet of glory", 0);

            }
        }
        Walking.walkPath(pathToGe);
        General.sleep(1000, 3000);


    }

    /**
     * Withdraws a select amount of an item that matches the given name.
     *
     * @param name
     *            Name of the item.
     * @param amount
     *            Amount of the item to withdraw; 0 to withdraw all.
     * @return If we withdrew the item or not.
     */
    public static boolean withdrawItem(final String name, final int amount) {
        RSItem[] i = Banking.find(
            (new Filter<RSItem>() {
                @Override public boolean accept(RSItem item) {
                    if (item.getDefinition() != null) {
                        return item.getDefinition().getName().equals(name);
                    }
                    return false;
                }
            }));
        if (i.length > 0) {
            return Banking.withdrawItem(i[0], amount);
        }
        return false;
    }
}
