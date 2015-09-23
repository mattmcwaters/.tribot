package scripts;

import org.tribot.api.input.Mouse;
import org.tribot.api2007.Game;
import org.tribot.api2007.Player;
import org.tribot.api2007.Projection;
import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.EventBlockingOverride;
import org.tribot.script.interfaces.Painting;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Matt on 2015-09-22.
 */
@ScriptManifest(name = "Area Creator", description = "Tool that helps you to create areas", category = "Tools", authors = "AlphaDog")
public class Tool extends Script implements Painting, EventBlockingOverride {

    private static final String PREFIX = "private final RSArea area = ";
    private final ArrayList<RSTile> tileList = new ArrayList<RSTile>();
    private volatile RSTile hoveredTile = null;
    private RSArea area = null;

    @Override
    public void run() {
        while (true) {
            hoveredTile = getHoveredTile();
            sleep(100);
        }
    }

    public RSTile getHoveredTile() {
        if (!Projection.isInViewport(Mouse.getPos()))
            return null;

        RSTile pos = Player.getPosition();
        for (int x = pos.getX() - 10; x < pos.getX() + 10; x++) {
            for (int y = pos.getY() - 10; y < pos.getY() + 10; y++) {
                final RSTile tile = new RSTile(x, y, Game.getPlane());
                final Polygon tileBounds = Projection.getTileBoundsPoly(tile, 0);
                if (tileBounds != null && tileBounds.contains(Mouse.getPos()))
                    return new RSTile(x, y, Game.getPlane());
            }
        }
        return null;
    }

    @Override
    public void onPaint(Graphics graphics) {
        if (hoveredTile != null) {
            graphics.setColor(Color.GREEN);
            graphics.drawPolygon(Projection.getTileBoundsPoly(hoveredTile, 0));
        }

        if (area != null) {
            graphics.setColor(Color.LIGHT_GRAY);
            for (RSTile tile : area.getAllTiles())
                graphics.drawPolygon(Projection.getTileBoundsPoly(tile, 0));

            graphics.setColor(Color.RED);
            for (RSTile tile : tileList)
                graphics.drawPolygon(Projection.getTileBoundsPoly(tile, 0));
        }

    }

    private void onTileListChanged() {
        final RSTile[] tiles = tileList.toArray(new RSTile[tileList.size()]);
        area = new RSArea(tiles);

        //copy to clipboard
        final String tileString = Arrays.toString(tiles).replaceAll("\\(", "\nnew RSTile(");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
            new StringSelection(
                PREFIX + "new RSArea(new RSTile[] { " + tileString.substring(1, tileString.length() - 1) + "\n});"), null
        );
    }

    @Override
    public OVERRIDE_RETURN overrideKeyEvent(KeyEvent key) {
        if (key.getID() != KeyEvent.KEY_PRESSED)
            return OVERRIDE_RETURN.DISMISS;

        switch (key.getKeyCode()) {
            case KeyEvent.VK_F1:
                key.consume();
                if (hoveredTile != null && !tileList.contains(hoveredTile)) {
                    tileList.add(hoveredTile);
                    onTileListChanged();
                }
                break;
            case KeyEvent.VK_F2:
                key.consume();
                if (!tileList.isEmpty()) {
                    tileList.remove(tileList.size() - 1);
                    onTileListChanged();
                }
                break;
            case KeyEvent.VK_F3:
                key.consume();
                tileList.clear();
                area = null;
                break;
            default:
                return OVERRIDE_RETURN.SEND;
        }

        return OVERRIDE_RETURN.DISMISS;
    }

    @Override
    public OVERRIDE_RETURN overrideMouseEvent(MouseEvent mouse) {
        return OVERRIDE_RETURN.SEND;
    }
}
