public class GrandPrix {
    public static void main (String[] args) {
        int driversFinished = 0; // 0/3

        int[][] method = new int[3][3];
        for (int index = 0; index < 9; index++) {
            int i = index / 3; // Calculate the row
            int j = index % 3; // Calculate the column
            method[i][j] = (int) (Math.random() * 2) + 1;
        }

        F1driver sainz = new F1driver(1, method[0], "Charlos Sainz");
        F1driver norris = new F1driver(2, method[1], "Lando Norris");
        F1driver perez = new F1driver(3, method[2],"Sergio Perez");
        // ready
        System.out.println("Die Läufer sind bereit...");

        // DeblockingQueue
        MessageBBQ bbq = new MessageBBQ(9, sainz, norris, perez);

        Thread t1 = new Thread(sainz);
        Thread t2 = new Thread(norris);
        Thread t3 = new Thread(perez);

        // NEW
        System.out.println(t1.getState().name());
        System.out.println(t2.getState().name());
        System.out.println(t3.getState().name());

        // start Threads
        t1.start();
        t2.start();
        t3.start();

        // start
        System.out.println("Start!");

        /*for(int i = 0; i < 3; i++) {
            // RUNNABLE
            System.out.println(t1.getState().name());
            System.out.println(t2.getState().name());
            System.out.println(t3.getState().name());
        }*/

        int[] cars = new int[3];
        String news = "";

        do {
            Message breakingNews = bbq.pop();
            cars[breakingNews.driverNr-1]++;

            news = breakingNews.driverName + " hat Runde " + cars[breakingNews.driverNr-1] + " nach " + breakingNews.time + " ms abgeschlossen!";
            // not all roundes finished
            if(cars[breakingNews.driverNr-1] < 3) {
                System.out.println(news);

            } else { // finished race
                driversFinished++;

                // info output
                System.out.println(news + "(Platz " + driversFinished + "!)");
            }

        } while (driversFinished < 3);

        /*sainz.setRunning(false);
        norris.setRunning(false);
        perez.setRunning(false);*/

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
