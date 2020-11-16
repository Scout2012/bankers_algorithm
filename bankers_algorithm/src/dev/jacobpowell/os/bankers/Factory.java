package dev.jacobpowell.os.bankers;

import java.io.*;
import java.util.*;

public class Factory {
    public static void main(String[] args) {
        String filename = "./src/dev/jacobpowell/os/bankers/infile.txt";
        Thread[] workers = new Thread[Customer.COUNT];      // the customers
        // read in values and initialize the matrices
        // to do
        // ...
        try {
        	File infile = new File(filename);
        	Scanner infileScanner = new Scanner(infile);
        	
        	String resourceLine = infileScanner.nextLine();
        	String[] resourcePieces = resourceLine.split(",");
        	
        	int nResources = resourcePieces.length;
            int[] resources = new int[nResources];
            for (int i = 0; i < nResources; i++) { resources[i] = Integer.parseInt(resourcePieces[i].trim()); }
            Bank theBank = new BankImpl(resources);
            int[] maxDemand = new int[nResources];
            int[] allocated = new int[nResources];
            
        	int threadNum = 0;
        	while(threadNum < Customer.COUNT) {
                workers[threadNum] = new Thread(new Customer(threadNum, maxDemand, theBank));
				theBank.addCustomer(threadNum, allocated, maxDemand);
                ++threadNum;        //theBank.getCustomer(threadNum);
            }
        	
        	infileScanner.close();
        } catch (FileNotFoundException fnfe) { throw new Error("Unable to find file \"" + filename + "\"");
        } catch (IOException ioe) { throw new Error("Error processing \"" + filename + "\""); }
        System.out.println("FACTORY: created threads");     // start the customers
        for (int i = 0; i < Customer.COUNT; i++) { workers[i].start(); }
        System.out.println("FACTORY: started threads");
    }
}
