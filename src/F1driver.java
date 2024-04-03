import java.util.concurrent.CountDownLatch;

public class F1driver implements Runnable {
    private int carNumber;
    private String name;
    private boolean running;
    private CountDownLatch latch;
    private MessageBBQ bbq;
    private int[] method;

    F1driver(int number, int[] method, String name) {
        this.carNumber = number;
        this.method = method;
        this.name = name;
        // wait for three threads before it starts
        this.latch = new CountDownLatch(3);
    }

    @Override
    public void run() {
        // blocked / waiting
        try {
            latch.await();

            // fibonacci calculation
            for(int i = 0; i < 3; i++) {
                //System.out.println(this.name + " hat Runde " + (i+1) + " nach " +  (int) (Math.random() * this.count) + "ms abgeschlossen!");
                if(method[i] == 1){
                    int n = (int) (Math.random() * 21) + 20;
                    fibonacci(n);
                }else{
                    quadrat();
                }

                this.bbq.push(this.name,this.carNumber);

            }
            //System.out.println("Die Läufer sind bereit...\n" + "Start!");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n - 1) + fibonacci(n - 2);
        }
    }

    private void quadrat(){
        //Quadratwurzeln mit der babylonischen Methode berechnen
        double n = 1000000;
        double x = n;
        double y = 1;
        double e = 0.000001;
        while(x - y > e) {
            x = (x + y) / 2;
            y = n / x;
        }
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public void setBBQ(MessageBBQ bbq) {
        this.bbq = bbq;
    }
}
