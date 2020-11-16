package dev.jacobpowell.os.bankers;

import java.io.*;
import java.util.*;

public class BankImpl implements Bank {
    private int n;          // the number of threads in the system
    private int m;          // the number of resources
    private int[] available;    // the amount available of each resource
    private int[][] maximum;    // the maximum demand of each thread
    private int[][] allocation; // the amount currently allocated to each thread
    private int[][] need;       // the remaining needs of each thread
    
    
    private void showAllMatrices(int[][] alloc, int[][] max, int[][] need, String msg) { 
        // todo
    }
 
    private void showMatrix(int[][] matrix, String title, String rowTitle) {
        // todo
    }
    
    private void showVector(int[] vect, String msg) {
        // todo
    }
    
    public BankImpl(int[] resources) {      // create a new bank (with resources)
        n = Customer.MAX_COUNT;
        m = resources.length;
        available = new int[m];
        maximum = new int[n][m];
        allocation = new int[n][m];
        need = new int[n][m];
    }
    
    // invoked by a thread when it enters the system;  also records max demand
    public void addCustomer(int threadNum, int[] allocated, int[] maxDemand) {
    	for (int i = 0; i < m; ++i){
            allocation[threadNum][i] = allocated[i];
            maximum[threadNum][i] = maxDemand[i];
            need[threadNum][i] = maxDemand[i] - allocated[i];
        }
    }
    
    // output state for each thread
    public void getState() {
        // todo
    }
    
    private boolean isSafeState (int threadNum, int[] request) {
        // todo -- actual banker's algorithm
    	
    	return false;
    }
    
    // make request for resources. will block until request is satisfied safely
    public synchronized boolean requestResources(int threadNum, int[] request)  {
        // todo
    	
    	return false;
     }
    
    public synchronized void releaseResources(int threadNum, int[] release)  {
        // todo
    }
}
