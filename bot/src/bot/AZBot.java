package bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.Attack;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class AZBot extends AbstractionLayerAI {    


    private UnitTypeTable utt;
    private UnitType worker;
    UnitType workerType;
    UnitType baseType;
  

    public AZBot(UnitTypeTable utt) {
        super(new AStarPathFinding());
        this.utt = utt;
        if (utt!=null)
        {
        	workerType = utt.getUnitType("Worker");
            baseType = utt.getUnitType("Base");
        }
        
    }
    

    @Override
    public void reset() {
    }

    
    @Override
    public AI clone() {
        return new AZBot(utt);
    }
    
  
    @Override
    public PlayerAction getAction(int player, GameState gs) {
    	
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);
     
        for (Unit unit : pgs.getUnits()) {
            // TODO: issue commands to units
        	if (unit.getPlayer() == player
        		&& unit.getType().canAttack) {
        		Unit enemy = findEnemyUnit(player, pgs);
        			if (enemy != null) {
        				attack(unit, enemy);
        			}
        	}
        	//Melee Units
        	 for(Unit u:pgs.getUnits()) {
                 if (u.getType()==baseType && 
                     u.getPlayer() == player && 
                     gs.getActionAssignment(u)==null) {
                     baseBehavior(u,p,pgs);
                 }   
        	 }
        	 // behavior of workers:
             List<Unit> workers = new LinkedList<Unit>();
             for(Unit u:pgs.getUnits()) {
                 if (u.getType().canHarvest && 
                     u.getPlayer() == player) {
                     workers.add(u);
                 }        
        }
        }
        
        return translateActions(player, gs);
    }
        //If player has enough resources, train more workers
        public void baseBehavior(Unit u,Player p, PhysicalGameState pgs) {
            if (p.getResources()>=workerType.cost) train(u, workerType);
            
    }
        
    private Unit findEnemyUnit(int player, PhysicalGameState pgs) {
    	for (Unit unit : pgs.getUnits()) {
    		if (unit.getPlayer() != player && unit.getPlayer() != -1) {
    			return unit;
    		}
    	}
    	return null;
    }
    
    

    @Override
    public List<ParameterSpecification> getParameters() {
        return new ArrayList<>();
    }
}

