import java.util.Stack;
import java.util.concurrent.*;

public class MessageBBQ {
    private BlockingDeque<Message> stack;
    private long startingTimePoint;
    public MessageBBQ(int max, F1driver sainz, F1driver norris, F1driver perez) {
        stack = new LinkedBlockingDeque<>(max);
        CountDownLatch latch = new CountDownLatch(1);

        // start threads
        sainz.setBBQ(this);
        norris.setBBQ(this);
        perez.setBBQ(this);

        sainz.setLatch(latch);
        norris.setLatch(latch);
        perez.setLatch(latch);

        latch.countDown();
        this.startingTimePoint = System.currentTimeMillis();
    }

    public synchronized void push (String driverName, int nr) {
        while (true) {
            try {
                stack.putLast(new Message(driverName, System.currentTimeMillis()-startingTimePoint, nr));
                // putLast was successful
                return;
            } catch (InterruptedException e) {
                // putLast got interrupted - try again
            }
        }
    }

    public Message pop() {
        while (true) {
            try {
                return stack.take();
                // takeLast was successful
            } catch (InterruptedException e) {
                // putLast got interrupted - try again
            }
        }
    }
}
