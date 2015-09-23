package scripts.OrbSlave.Utils;

import java.awt.Rectangle;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab.TABS;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.NPCChat;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;

public class Hopper {

    public static Condition switchedWorld(final int world) {
        return new Condition() {
            public boolean active() {
                return Game.getCurrentWorld() == world;
            }
        };
    }

    public static RSInterfaceComponent getWorldComponent(int world) {
        RSInterfaceChild worldList = Interfaces.get(69, 7);

        if (worldList != null) {
            for (RSInterfaceComponent i : worldList.getChildren()) {
                if (i.getText().equals(String.valueOf(world))) {
                    return worldList.getChildren()[i.getIndex() - 2];
                }
            }
        }
        return null;
    }

    public static void scrollToWorld(int world) {
        RSInterfaceComponent worldComponent = getWorldComponent(world);
        Rectangle rect = new Rectangle(547, 234, 190, 190);

        if (worldComponent != null) {
            if (rect.contains(Mouse.getPos())) {
                while (!rect.contains(worldComponent.getAbsolutePosition())) {
                    General.println(worldComponent.getAbsoluteBounds().y);
                    Mouse.scroll(worldComponent.getAbsoluteBounds().y < rect.y);
                    General.sleep(50, 100);
                }
            } else {
                Mouse.moveBox(rect);
            }
        }
    }

    public static void switchWorld(int world) {
        RSInterfaceChild worldList = Interfaces.get(69, 7);
        RSInterfaceChild btnWorldSwitcher = Interfaces.get(182, 1);
        Rectangle rect = new Rectangle(547, 234, 190, 190);

        if (TABS.LOGOUT.isOpen()) {
            if (worldList != null) {
                RSInterfaceComponent worldComponent = getWorldComponent(world);

                if (NPCChat.getOptions() != null) {
                    if (NPCChat.getOptions()[0] != null && NPCChat.getOptions()[0].contains("Yes")) {
                        if (NPCChat.selectOption("Yes", true)) {
                            Timing.waitCondition(switchedWorld(world), General.random(3000, 6000));
                        }
                    } else if (NPCChat.getOptions()[1] != null && NPCChat.getOptions()[1].contains("Switch")) {
                        if (NPCChat.selectOption(NPCChat.getOptions()[1], true)) {
                            Timing.waitCondition(switchedWorld(world), General.random(3000, 6000));
                        }
                    }
                }

                General.println(worldComponent.getIndex());

                if (worldComponent != null) {
                    if (rect.contains(worldComponent.getAbsolutePosition())) {
                        worldComponent.click("Switch");
                    } else {
                        scrollToWorld(world);
                    }
                }
            } else {
                if (btnWorldSwitcher != null) {
                    btnWorldSwitcher.click("World Switcher");
                }
            }
        } else {
            TABS.LOGOUT.open();
        }
    }
}
