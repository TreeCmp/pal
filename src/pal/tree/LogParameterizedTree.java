// LogParameterizedTree.java
//
// (c) 1999-2001 PAL Development Core Team
//
// This package may be distributed under the
// terms of the Lesser GNU General Public License (LGPL)


package pal.tree;

/**
 * This class logarithmically transforms tree parameters.
 * Hopefully this makes the function look more like a quadratic
 * for the optimizer!
 *
 * @author Alexei Drummond
 * @version $Id: LogParameterizedTree.java,v 1.9 2003/06/04 03:17:52 matt Exp $
 */
public class LogParameterizedTree
        extends ParameterizedTree.ParameterizedTreeBase implements ParameterizedTree {
    //
    // Public stuff
    //

    ParameterizedTree params;

    private double[] logMins;
    private double[] logMaxs;
    private double[] logDefaults;


    /**
     * Takes a parameterized object and transforms
     * the parameters logarithmically.
     */
    public LogParameterizedTree(ParameterizedTree params) {
        setBaseTree(params);

        this.params = params;


        logMins = new double[params.getNumParameters()];
        logMaxs = new double[params.getNumParameters()];
        logDefaults = new double[params.getNumParameters()];

        for (int i = 0; i < logMins.length; i++) {
            logMins[i] = Math.log(params.getLowerLimit(i));
            logMaxs[i] = Math.log(params.getUpperLimit(i));
            logDefaults[i] = Math.log(params.getDefaultValue(i));
        }
    }

    // interface Parameterized

    public int getNumParameters() {
        return params.getNumParameters();
    }

    public void setParameter(double logParam, int n) {
        // - logMins scales the value to lower bound of 0
        double realParam = Math.exp(logParam + logMins[n]);

        params.setParameter(realParam, n);
    }

    public double getParameter(int n) {
        return Math.log(params.getParameter(n)) - logMins[n];
    }

    public void setParameterSE(double paramSE, int n) {
    }

    public double getLowerLimit(int n) {
        return 0;
    }

    public double getUpperLimit(int n) {
        return logMaxs[n] - logMins[n];
    }

    public double getDefaultValue(int n) {
        return logDefaults[n] - logMins[n];
    }

    public String getParameterizationInfo() {
        return params.getParameterizationInfo() + " (using log scaling)";
    }
}
