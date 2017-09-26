import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;

/**
 * Class that implements the simulation model for the rabbits grass simulation.
 * This is the first class which needs to be setup in order to run Repast
 * simulation. It manages the entire RePast environment and the simulation.
 *
 * @author
 */

public class RabbitsGrassSimulationModel extends SimModelImpl {

    private int numAgents;
    private int worldXSize;
    private int worldYSize;

    private Schedule schedule;

    public String getName(){
      return "Carry And Drop";
    }

    public void setup(){
    }

    public void begin(){
      buildModel();
      buildSchedule();
      buildDisplay();
    }

    public void buildModel(){
    }

    public void buildSchedule(){
    }

    public void buildDisplay(){
    }

    public Schedule getSchedule(){
      return schedule;
    }

    public String[] getInitParam(){
      String[] initParams = { "NumAgents" , "WorldXSize", "WorldYSize" };
      return initParams;
    }

    public int getNumAgents(){
      return numAgents;
    }

    public void setNumAgents(int na){
      numAgents = na;
    }

    public int getWorldXSize(){
      return worldXSize;
    }

    public void setWorldXSize(int wxs){
      worldXSize = wxs;
    }

    public int getWorldYSize(){
      return worldYSize;
    }

    public void setWorldYSize(int wys){
      worldYSize = wys;
    }

    public static void main(String[] args) {
      SimInit init = new SimInit();
      RabbitsGrassSimulationModel model = new RabbitsGrassSimulationModel();
      init.loadModel(model, "", false);
    }
}
