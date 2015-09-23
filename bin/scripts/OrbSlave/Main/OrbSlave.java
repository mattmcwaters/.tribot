package scripts.OrbSlave.Main;

import org.tribot.api.General;
import org.tribot.api.util.ABCUtil;
import org.tribot.api2007.Players;
import org.tribot.api2007.Trading;
import org.tribot.api2007.types.RSPlayer;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import scripts.OrbSlave.Nodes.Node;
import scripts.OrbSlave.Nodes.TradeMule;
import scripts.OrbSlave.Nodes.TradeSlave;
import scripts.OrbSlave.Nodes.WalkToGe;

/**
 * Created by Matt on 2015-09-21.
 */
@ScriptManifest(authors = {"methodrs"}, name = "OrbSlave", category = "Air orbs")
public class OrbSlave extends Script implements MessageListening07 {
    public static String slaveToTrade;
    public static ArrayList<Node> nodes = new ArrayList<Node>();
    public static String args;
    public static int tradedCount=0;
    @Override
    public void run() {

        Collections.addAll(nodes, new TradeMule(), new WalkToGe(), new TradeSlave());    //add all nodes to the ArrayList
        loop(20, 40);
    }

    private void loop(int min, int max) {
        while (true) {
            for (final Node node : nodes) {    //loop through the nodes
                if (node.validate()) {     //validate each node
                    node.execute();    //execute
                    sleep(General.random(min, max));	//time inbetween executing nodes
                }
            }
        }
    }
    @Override public void clanMessageReceived(String s, String s1) {

    }

    @Override public void personalMessageReceived(String s, String s1) {

    }

    @Override public void tradeRequestReceived(String s) {
        slaveToTrade = s;
        RSPlayer slave = Players.find(s)[0];

        slave.click("Trade with "+s);
        while(Trading.getWindowState()==null){
            sleep(2000, 3000);
                slave.click("Trade with "+s);

        }
    }

    @Override public void duelRequestReceived(String s, String s1) {

    }

    @Override public void playerMessageReceived(String s, String s1) {

    }

    @Override public void serverMessageReceived(String s) {

    }

}
