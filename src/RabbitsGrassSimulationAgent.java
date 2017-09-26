import java.awt.Color;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

/**
 * Class that implements the simulation agent for the rabbits grass simulation.
 * 
 * @author
 */

public class RabbitsGrassSimulationAgent implements Drawable {
    private int x;
    private int y;
    private int vX;
    private int vY;
    private int energy;
    private static int IDNumber = 0;
    private int ID;
    private RabbitsGrassSimulationSpace rgSpace;

    public RabbitsGrassSimulationAgent(int initEnergy) {
        x = -1;
        y = -1;
        setVxVy();
        energy = initEnergy;
        IDNumber++;
        ID = IDNumber;
    }

    private void setVxVy() {
        vX = 0;
        vY = 0;
        while ((vX == 0) && (vY == 0)) {
            vX = (int) Math.floor(Math.random() * 3) - 1;
            vY = (int) Math.floor(Math.random() * 3) - 1;
        }
    }

    public void setRgSpace(RabbitsGrassSimulationSpace rgs) {
        rgSpace = rgs;
    }

    public void setXY(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public String getID() {
        return "Rabbit-" + ID;
    }

    public int getEnergy() {
        return energy;
    }

    public void report() {
        System.out.println(getID() + " at " + x + ", " + y + " has " + getEnergy() + " energy");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(SimGraphics G) {
        if (energy <= 5) {
            G.drawFastCircle(Color.RED);
        } else {
            G.drawFastCircle(Color.WHITE);
        }
    }

    public void step() {
        setVxVy();
        int size = rgSpace.getCurrentAgentSpace().getSizeX();
        rgSpace.moveAgentAt(x, y, (x + vX + size) % size , (y + vY + size) % size);
        energy += rgSpace.eatGrassAt(x, y);
        energy--;
    }

    public void loseEnergy(int birthcost) {
        energy -= birthcost;
    }
}
