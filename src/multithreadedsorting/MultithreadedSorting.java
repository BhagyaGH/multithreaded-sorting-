package multithreadedsorting;

import java.util.Random;

//Global class is used to declare the global variables
class Global {
    public static int n = 25;
    public static int inlist[] = new int[n];
    public static int outlist[] = new int[n];
}

//Sorting thread
class SortingThread extends Thread {
    int start, end;
    Sorter sort;
    boolean isDone;
    
    SortingThread(int start, int end) {
        this.start = start;
        this.end = end;
        sort = new Sorter();
        isDone = false;
    }
    
    @Override
    public void run() {
        sort.insertionSort(start, end);
        isDone = true;
    }

} 

//Merging thread
class MergingThread extends Thread {
    int start, mid, end;
    Sorter combine;
    
    MergingThread(int start, int mid, int end) {
        this.start = start;
        this.mid = mid;
        this.end = end;
        combine = new Sorter();
    }
    
    @Override
    public void run() {
        combine.merge(start, mid, end);
    }
}

//Sorter to divide the array, sort the two parts concurrently and merge them
class Sorter {
    //Dividing the array and sorting the parts
    public void sort(int start, int end) {
        int mid = (start+end)/2;
        SortingThread left = new SortingThread(start, mid);
        SortingThread right = new SortingThread(mid+1, end);
        left.start();
        right.start();
        while(!left.isDone || !right.isDone);
        new MergingThread(start, mid, end).start();
    }
    //Insertion sort for sorting
    public void insertionSort(int start, int end) {
        int i, key;
        for(int j=start+1; j<=end; j++) {
            key = Global.inlist[j];
            i = j-1;
            while(i>=start && Global.inlist[i]>key) {
                int temp = Global.inlist[i];
                Global.inlist[i] = Global.inlist[i+1];
                Global.inlist[i+1] = temp;
                i=i-1;
            }
            Global.inlist[i+1] = key;
        }
    }
    //Merging the 2 parts into a single array
    public void merge(int start, int mid, int end) {
        int i=start;
        int j=mid+1;
        
        for(int k=start; k<=end; k++) {
            if(i<=mid && j<=end) {
                if(Global.inlist[i] <= Global.inlist[j]) {
                    Global.outlist[k] = Global.inlist[i];
                    i++;
                }
                else {
                    Global.outlist[k] = Global.inlist[j];
                    j++;
                }
            }
            else if(i<=mid) {
                Global.outlist[k] = Global.inlist[i];
                i++;
            }
            else {
                Global.outlist[k] = Global.inlist[j];
                j++;
            }
        }
    }
}


public class MultithreadedSorting {
    
    Sorter s = new Sorter();
    
    public static void main(String[] args) {
        MultithreadedSorting ms = new MultithreadedSorting();       
        
        Random randomGenerator = new Random();
        
        for (int i=0; i<Global.n; i++){
            Global.inlist[i] = randomGenerator.nextInt(100);
        }
        
        System.out.println("-----Mutithreaded Sorting-----\nBefore sorting - Sample Input:");        
        for(int i=0; i<Global.inlist.length; i++) {
            System.out.printf("%d  ", Global.inlist[i]);
        }
        
        ms.s.sort(0, Global.inlist.length-1);
        
        System.out.println("\nAfter sorting - Sample Output:");
        for(int i=0; i<Global.outlist.length; i++) {
            System.out.printf("%d  ", Global.outlist[i]);
        }
        System.out.println();
    }   
}

/*
 * Output of the program
 * 
-----Mutithreaded Sorting-----
Before sorting:
26  94  70  45  54  98  93  89  53  21  32  67  72  73  45  66  66  35  49  81  67  85  3  47  21  
After sorting:
3  21  21  26  32  35  45  45  47  49  53  54  66  66  67  67  70  72  73  81  85  89  93  94  98  
 * 
 * 
 */