/**
 * 
 */
package com.cmm.jft.marketdata;

import java.util.Random;
import java.util.stream.IntStream;

/**
 * <p>
 * <code>BandLimits.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 06/08/2017 10:50:18
 *
 */
public class BandLimits {

    /**
     * 
     */
    private double hardLimitLow, hardLimitHigh;

    /**
     * 
     */
    private double rejectionBandLow, rejectionBandHigh;

    /**
     * Limites para acionar o leilao automatico.
     * Quando o leilao automatico eh iniciado, na iminencia de uma negociacao
     * proveniente de uma ordem recebida, 
     * 
     */
    private double auctionBandLow, auctionBandHigh;

    /**
     * Variacao maxima permitiva para o ativo ao longo do dia.
     */
    private double staticLimitLow, staticLimitHigh;

    private int quantityLimit;
    
    public static void main(String args[]) {
	
	Random rnd = new Random();
	IntStream it = rnd.ints(50,1000, 1100);
	it.forEach(i -> System.out.println(i));
	
	
	BandLimits bl = new BandLimits(0, .2, .15, .05, 200000);
	bl.calculateBandLimit(10);
	System.out.println(bl);
	
    }
    
    

    public BandLimits(double basePrice, double auctionBand, double rejectHiBand, double rejectLoBand, int qtyLimit) {
	this.auctionBandHigh = auctionBand;
	this.auctionBandLow = -auctionBand;

	this.rejectionBandHigh = rejectHiBand;
	this.rejectionBandLow = -rejectLoBand;
	this.quantityLimit = qtyLimit;
	
	calculateBandLimit(basePrice);
    }

    /**
     * Realiza o ajuste das bandas e limites para o mercado em questao.
     */
    public void calculateBandLimit(double basePrice) {

	this.hardLimitHigh = basePrice + (this.rejectionBandHigh * basePrice);
	this.hardLimitLow = basePrice + (-this.rejectionBandHigh * basePrice);

	this.staticLimitHigh = basePrice + (-this.rejectionBandLow * basePrice);
	this.staticLimitLow = basePrice + (this.rejectionBandLow * basePrice);
    }

    @Override
    public String toString() {
	return "BandLimit [hardLimitLow=" + hardLimitLow + ", hardLimitHigh=" + hardLimitHigh
		+ ", rejectionBandLow=" + rejectionBandLow + ", rejectionBandHigh=" + rejectionBandHigh
		+ ", auctionBandLow=" + auctionBandLow + ", auctionBandHigh=" + auctionBandHigh
		+ ", staticLimitLow=" + staticLimitLow + ", staticLimitHigh=" + staticLimitHigh + "]";
    }

    /**
     * @return the hardLimitLow
     */
    public double getHardLimitLow() {
        return hardLimitLow;
    }

    /**
     * @return the hardLimitHigh
     */
    public double getHardLimitHigh() {
        return hardLimitHigh;
    }

    /**
     * @return the rejectionBandLow
     */
    public double getRejectionBandLow() {
        return rejectionBandLow;
    }

    /**
     * @return the rejectionBandHigh
     */
    public double getRejectionBandHigh() {
        return rejectionBandHigh;
    }

    /**
     * @return the auctionBandLow
     */
    public double getAuctionBandLow() {
        return auctionBandLow;
    }

    /**
     * @return the auctionBandHigh
     */
    public double getAuctionBandHigh() {
        return auctionBandHigh;
    }

    /**
     * @return the staticLimitLow
     */
    public double getStaticLimitLow() {
        return staticLimitLow;
    }

    /**
     * @return the staticLimitHigh
     */
    public double getStaticLimitHigh() {
        return staticLimitHigh;
    }
    
    /**
     * @return the quantityLimit
     */
    public int getQuantityLimit() {
	return quantityLimit;
    }

}
