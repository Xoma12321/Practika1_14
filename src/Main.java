public class Main {
    private static int nextThreadIndex = 1;

    public static void main(String[] args) {
        Thread t1 = new Thread(new WorkerThread(1), "Thread-1");
        Thread t2 = new Thread(new WorkerThread(2), "Thread-2");
        Thread t3 = new Thread(new WorkerThread(3), "Thread-3");

        t1.start();
        t2.start();
        t3.start();


        while (true) {
            try {
                Thread.sleep(1000); // Пауза в 1 секунду перед оповещением следующего потока
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (WorkerThread.class) {
                WorkerThread.class.notify();
            }
        }
    }

    static class WorkerThread implements Runnable {
        private int threadIndex;

        public WorkerThread(int index) {
            this.threadIndex = index;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (this.getClass()) {
                    try {
                        while (threadIndex != nextThreadIndex) {
                            this.getClass().wait();
                        }
                        System.out.println(Thread.currentThread().getName());

                        Thread.sleep(1000); // Пауза в 1 секунду после вывода перед оповещением следующего потока

                        nextThreadIndex = nextThreadIndex % 3 + 1;
                        this.getClass().notifyAll();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
