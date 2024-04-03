import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.*;

public class MessageBBQ {
    private Deque<Message> messageDeque;
    private long startingTimePoint;
    private int max;
    public MessageBBQ(int max, F1driver sainz, F1driver norris, F1driver perez) {
        messageDeque = new ArrayDeque<>(max);
        this.max = max;
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
        // wait for the stack not to be full
        // if stack is full -> Thread is waiting
        while (max >= messageDeque.size()) {
            try {
                // waiting
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // if stack is not full
            try {
                // -> save message (on tail)
                messageDeque.addLast(new Message(driverName, System.currentTimeMillis()-startingTimePoint, nr));
                // putLast was successful

                // All waiting objects registered with the "this" object are notified and their block is removed.
                // blocking is removed
                this.notifyAll();
                return;
            } catch (IllegalStateException e) {
                System.out.println("Capacity restrictions - alternatively: offerLast(E e)");
            }

        }
    }

    public synchronized Message pop() {
        while (messageDeque.isEmpty()) {
            try {
                return messageDeque.pop();
                // takeLast was successful
            } catch (IllegalStateException e) {
                // putLast got interrupted - try again
            }
        }
    }
}
