import java.util.ArrayList;
import java.util.List;

import ilog.concert.*;
import ilog.cplex.*;

public class App {
    public static void main(String[] args) {
        try(IloCplex cplex = new IloCplex()) {
            
            List<IloRange> constraints = new ArrayList();
            
            //variable define
            IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x"); // x lies between 0 - infinity
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y"); // y lies between 0 - infinity

            //expression -> objective function = 0.12x + 0.15y
            // IloLinearNumExpr obj = cplex.linearNumExpr();
            // obj.addTerm(0.12, x); // term x = 0.12
            // obj.addTerm(0.15, y); // term y = 0.15
            
            // // Min
            // cplex.addMinimize(obj);

            //expression -> objective function = 0.12x + 0.15y
            cplex.addMinimize(cplex.sum(cplex.prod(0.12, x), cplex.prod(0.15, y)));

            // define constrain
            // 60x + 60y >= 300
            constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x), cplex.prod(60, y)), 300));
            // 12x + 6y >= 36
            constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x), cplex.prod(6, y)), 36));
            // 10x + 30y >= 90
            constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x), cplex.prod(30, y)), 90));

            // stop showing extra text
            cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);

            // solve
            if (cplex.solve()) {
                System.out.println("The objective status = " + cplex.getStatus());
                System.out.println("The objective = " + cplex.getObjValue());
                System.out.println("The x = " + cplex.getValue(x));
                System.out.println("The y = " + cplex.getValue(y));

                for (int i = 0; i < constraints.size(); i++) {
                    System.out.println("Dual constraint-" + (i + 1) + ": " + cplex.getDual(constraints.get(i)));
                    System.out.println("Slack constraint-" + (i + 1) + ": " + cplex.getSlack(constraints.get(i)));
                }
            } else {
                System.out.println("Something went wrong.");
            }

            cplex.end();

        } catch (IloException e) {
            System.err.println("Error: " + e);
        }
    }
}
