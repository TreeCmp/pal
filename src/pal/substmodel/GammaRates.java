// GammaRates.java
//
// (c) 1999-2001 PAL Development Core Team
//
// This package may be distributed under the
// terms of the Lesser GNU General Public License (LGPL)


package pal.substmodel;

import pal.misc.*;
import pal.statistics.*;

import java.io.*;


/**
 * discrete Gamma distribution (Z. Yang. 1994. JME 39:306-314)
 *
 * @version $Id: GammaRates.java,v 1.10 2003/06/11 05:26:46 matt Exp $
 *
 * @author Korbinian Strimmer
 */
public class GammaRates extends RateDistribution
{
	//
	// Public stuff
	//

	/**
	 * construct discrete Gamma distribution (mean = 1.0)
	 *
	 * @param n number of categories
	 * @param a shape parameter (alpha)
	 */
	public GammaRates(int n, double a)
	{
		super(n);
		alpha = a;
		showSE = false;

		makeGamma(alpha);
	}

	// interface Report

	public void report(PrintWriter out)
	{
		out.println("Model of rate heterogeneity: Discrete Gamma");
		out.println("Number of rate categories: " + numRates);
		out.print("Gamma distribution parameter alpha: ");
		format.displayDecimal(out, alpha, 2);
		if (showSE)
		{
			out.print("  (S.E. ");
			format.displayDecimal(out, alphaSE, 2);
			out.println(")");
		}
		else
		{
			out.println();
		}
		out.println();
		printRates(out);
	}

	// interface Parameterized

	public int getNumParameters()
	{
		return 1;
	}

	public void setParameter(double param, int n)
	{
		alpha = param;
		makeGamma(alpha);
	}

	public double getParameter(int n)
	{
		return alpha;
	}

	public void setParameterSE(double paramSE, int n)
	{
		alphaSE = paramSE;
		showSE = true;
	}

	public double getLowerLimit(int n) {
		//I changed this from 0.0001, because that caused illegal value exceptions in GammaDistribution if gamma went that low
		return 0.001;//0001;
	}

	public double getUpperLimit(int n)	{	return 100.0;	}

	public double getDefaultValue(int n)
	{
		return 0.5;
	}


	//
	// Private stuff
	//

	private boolean showSE;

	// Shape parameter
	private double alpha;
	private double alphaSE;

	private void makeGamma(double a)
	{
		double mean = 0.0;
		for (int i = 0; i < numRates; i++)
		{
			rate[i] = GammaDistribution.quantile((2.0*i+1.0)/(2.0*numRates), a, 1.0/a);
			mean += rate[i];
		}
		mean = mean/(double) numRates;
		for (int i = 0; i < numRates; i++)
		{
			rate[i] /= mean;
			probability[i] = 1.0/(double) numRates;
		}
		fireParametersChangedEvent();
	}

}
