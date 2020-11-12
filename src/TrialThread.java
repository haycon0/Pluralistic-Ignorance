public class TrialThread extends Thread{

    private int a1;
    private int a2;
    private double sd1;
    private double sd2;

    TrialThread(int a1, int a2, double sd1, double sd2){
        this.a1 = a1;
        this.a2 = a2;
        this.sd1 = sd1;
        this.sd2 = sd2;
    }

    public void run(){
        System.out.println("thread " + "a1-" + a1 + " a2-" + a2 + " is running");
        Environment.trialLoop(a1,a2,sd1,sd2);
    }
}
