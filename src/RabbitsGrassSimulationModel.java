import java.awt.Color;
import java.util.ArrayList;

import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.util.SimUtilities;

/**
 * Class that implements the simulation model for the rabbits grass simulation.
 * This is the first class which needs to be setup in order to run Repast
 * simulation. It manages the entire RePast environment and the simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationModel extends SimModelImpl {

    private static final int WORLDSIZE       = 20;
    private static final int NUMAGENTS       = 10;
    private static final int NUMGRASS        = 20;
    private static final int BIRTHTHRESHOLD  = 20;
    private static final int INITENERGY      = 10;
    private static final int BIRTHCOST       = 10;
    private static final int GRASSGROWTHRATE = 100;

    private int worldSize       = WORLDSIZE;
    private int numAgents       = NUMAGENTS;
    private int birthThreshold  = BIRTHTHRESHOLD;
    private int grassGrowthRate = GRASSGROWTHRATE;

    private Schedule schedule;

    private RabbitsGrassSimulationSpace rgSpace;

    private ArrayList<RabbitsGrassSimulationAgent> agentList;

    private DisplaySurface displaySurf;
    
    private OpenSequenceGraph nbOfRabbits;
    
    class NumberOfRabbits implements DataSource, Sequence {
        public Object execute() {
            return new Double(getSValue());
        }
        
        public double getSValue() {
            return (double) rgSpace.nbGrass();
        }
    }

    public String getName() {
        return "Rabbits and Grass";
    }

    public void setup() {
        System.out.println("Running setup");
        rgSpace = null;
        agentList = new ArrayList<>();
        schedule = new Schedule(1);

        if (displaySurf != null) {
            displaySurf.dispose();
        }
        displaySurf = null;
        
        if (nbOfRabbits != null) {
            nbOfRabbits.dispose();
        }
        
        nbOfRabbits = null;

        displaySurf = new DisplaySurface(this, "Rabbits Grass Model Window 1");
        nbOfRabbits = new OpenSequenceGraph("Number of rabbits", this);

        registerDisplaySurface("Rabbits Grass Model Window 1", displaySurf);
        this.registerMediaProducer("Plot", nbOfRabbits);
    }

    public void begin() {
        buildModel();
        buildSchedule();
        buildDisplay();

        displaySurf.display();
        nbOfRabbits.display();
    }

    public void buildModel() {
        System.out.println("Running BuildModel");
        rgSpace = new RabbitsGrassSimulationSpace(worldSize);
        rgSpace.spreadGrass(NUMGRASS);

        for (int i = 0; i < numAgents; i++) {
            addNewAgent();
        }
        for (int i = 0; i < agentList.size(); i++) {
            RabbitsGrassSimulationAgent rga = (RabbitsGrassSimulationAgent) agentList.get(i);
            rga.report();
        }
    }

    public void buildSchedule() {
        System.out.println("Running BuildSchedule");

        class RabbitsGrassSimulationStep extends BasicAction {
            public void execute() {
                SimUtilities.shuffle(agentList);
                for (int i = 0; i < agentList.size(); i++) {
                    RabbitsGrassSimulationAgent rga = (RabbitsGrassSimulationAgent) agentList.get(i);
                    rga.step();
                    
                    if (rga.getEnergy() > BIRTHTHRESHOLD) {
                        rga.loseEnergy(BIRTHCOST);
                        addNewAgent();
                    }
                }
                
                reapDeadAgents();
                rgSpace.spreadGrass(grassGrowthRate);
                
                displaySurf.updateDisplay();
            }
        }

        schedule.scheduleActionBeginning(0, new RabbitsGrassSimulationStep());
        
        class RabbitGrassUpdateNbRabbits extends BasicAction {
            public void execute() {
                nbOfRabbits.step();
            }
        }
        
        schedule.scheduleActionBeginning(0, new RabbitGrassUpdateNbRabbits());
    }

    public void buildDisplay() {
        System.out.println("Running BuildDisplay");

        ColorMap map = new ColorMap();

        for (int i = 1; i < 16; i++) {
            map.mapColor(i, new Color(0, (int) (i * 8 + 127), 0));
        }
        
        map.mapColor(0, Color.BLACK);

        Value2DDisplay displayGrass = new Value2DDisplay(rgSpace.getCurrentGrassSpace(), map);

        Object2DDisplay displayAgents = new Object2DDisplay(rgSpace.getCurrentAgentSpace());
        displayAgents.setObjectList(agentList);

        displaySurf.addDisplayable(displayGrass, "Grass");
        displaySurf.addDisplayable(displayAgents, "Agents");

        nbOfRabbits.addSequence("Number of rabbits", new NumberOfRabbits());
    }

    private void reapDeadAgents() {
        for (int i = (agentList.size() - 1); i >= 0; i--) {
            RabbitsGrassSimulationAgent rga = (RabbitsGrassSimulationAgent) agentList.get(i);
            if (rga.getEnergy() < 1) {
                rgSpace.removeAgentAt(rga.getX(), rga.getY());
                agentList.remove(i);
            }
        }
    }

    private void addNewAgent() {
        RabbitsGrassSimulationAgent a = new RabbitsGrassSimulationAgent(INITENERGY);
        if (rgSpace.addAgent(a)) {
            agentList.add(a);            
        }
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String[] getInitParam() {
        String[] initParams = { "NumAgents", "WorldSize", "BirthThreshold", "GrassGrowthRate" };
        return initParams;
    }

    public int getNumAgents() {
        return numAgents;
    }

    public void setNumAgents(int na) {
        numAgents = na;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int ws) {
        worldSize = ws;
    }
    
    public int getBirthThreshold() {
        return birthThreshold;
    }
    
    public void setBirthThreshold(int bt) {
        birthThreshold = bt;
    }
    
    public int getGrassGrowthRate() {
        return grassGrowthRate;
    }
    
    public void setGrassGrowthRate(int ggr) {
        grassGrowthRate = ggr;
    }

    public static void main(String[] args) {
        SimInit init = new SimInit();
        RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
        init.loadModel(model, "", false);
    }
}
