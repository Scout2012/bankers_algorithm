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
        System.out.print("ALLOCATION MAXIMUM NEED\n");
        for (int i = 0; i < n; ++i){
            System.out.print(" ");
            System.out.print("------- -------- --------\n");
                showVector(alloc[i]," ");
                showVector(max[i]," ");
                showVector(need[i],"\n");
        }
    }
 
    private void showMatrix(int[][] matrix, String title, String rowTitle) {
    	 System.out.println(title);
         for (int i = 0; i < n; ++i){
             showVector(matrix[i], "");
         }
    }
    
    private void showVector(int[] vect, String msg) {
        System.out.print("[");
        for (int i = 0; i < m; ++i){
            System.out.print(Integer.toString(vect[i]) + ' ');
        }
        System.out.print("]" + msg);
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
    	showAllMatrices(allocation, maximum, need, "");
    }
    
    private boolean isSafeState (int threadNum, int[] request) {
    	int[] currAvail = new int[m];
        System.arraycopy(available,0, currAvail, 0, m);
        int[][] currAlloc = new int[n][m];
        int[][] currNeed = new int[n][m];
        for (int i = 0; i < n; i++){
            System.arraycopy(allocation[i], 0, currAlloc[i], 0, m);
            System.arraycopy(need[i], 0, currNeed[i], 0, m);
        }

        boolean[] finish = new boolean[n];
        Arrays.fill(finish, false);

        // pretend we grant the request to customer threadNum
        for (int i = 0; i < m; i++){
            currAvail[i] -= request[i];
            currAlloc[threadNum][i] += request[i];
            currNeed[threadNum][i] -= request[i];
        }

        while (true) {
            int index = -1;
            for (int i = 0; i < n; i++) {
                boolean hasEnoughResource = true;
                for (int j = 0; j < m; ++j) {
                    if (currNeed[i][j] > currAvail[j]) {
                        hasEnoughResource = false;
                        break;
                    }
                }
                if (!finish[i] && hasEnoughResource) {
                    index = i;
                    break;
                }
            }

            if (index > -1){
                for (int i = 0; i < m; i++){
                    currAvail[i] += currAlloc[index][i];
                    finish[index] = true;
                }
            }
            else break;
        }

        for (int i = 0; i < n; i++){
            if (!finish[i]) return false;
        }
        return true;
    }
    
    // make request for resources. will block until request is satisfied safely
    public synchronized boolean requestResources(int threadNum, int[] request)  {
    	for (int i = 0; i < m; ++i){
            if (request[i] > need[threadNum][i]){
                request[i] = need[threadNum][i];
            }
        }
        System.out.print("#P"+ threadNum + " RQ:");
        showVector(request, ", needs: ");
        showVector(need[threadNum], ", available:");
        showVector(available, "\n");
        
        for (int i = 0; i < m; ++i){
            if (request[i] > available[i]){
                System.out.println("--->DENIED");
                return false;
            }
        }

        if (isSafeState(threadNum, request)){
            System.out.print("---> APPROVED, #P" + threadNum + " now at: ");
            for (int i = 0; i < m; ++i){
                available[i] -= request[i];
                allocation[threadNum][i] += request[i];
                need[threadNum][i] -= request[i];
            }

            showVector(allocation[threadNum], " available");
            showVector(available, "\n");
            showAllMatrices(allocation, maximum, need, "\n");

            for (int i = 0; i < m; ++i){
                if (need[threadNum][i] != 0){
                    return false; // customer isn't finished yet
                }
            }
            return true;  // customer finished, waiting to be released
        }
    	
    	return false;
     }
    
    public synchronized void releaseResources(int threadNum, int[] release)  {
        for (int i = 0; i < m; ++i){
            available[i] += allocation[threadNum][i];
            allocation[threadNum][i] = 0;
        }
        showVector(allocation[threadNum], "\n");
    }
}
