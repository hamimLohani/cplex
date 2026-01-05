import ilog.concert.*;
import ilog.cplex.*;

public class Main {
    public static void main(String[] args) {
        try (IloCplex cplex = new IloCplex())
        {
            // Create variables x and y >= 0
            IloNumVar x = cplex.numVar(0, Double.MAX_VALUE, "x");
            IloNumVar y = cplex.numVar(0, Double.MAX_VALUE, "y");

            // Add objective: maximize 2x + 3y
            cplex.addMaximize(cplex.sum(cplex.prod(2.0, x), cplex.prod(3.0, y)));

            // Add constraint: x + y <= 10
            cplex.addLe(cplex.sum(x, y), 10.0);

            // stop showing extra text
            cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);
            // Solve the problem
            if (cplex.solve()) {
                System.out.println("Solution status: " + cplex.getStatus());
                System.out.println("Objective value: " + cplex.getObjValue());
                System.out.println("x = " + cplex.getValue(x));
                System.out.println("y = " + cplex.getValue(y));
            } else {
                System.out.println("No solution found.");
            }

            // Clean up
            cplex.end();
        } catch (IloException e) {
            System.err.println("Concert exception caught: " + e);
        }
    }
}