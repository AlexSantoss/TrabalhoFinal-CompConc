import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingAccount1 {

	int saldo;
	Lock lock;
	Condition condition;
	
	public SavingAccount1() {
		saldo = 0;
		lock = new ReentrantLock();
		condition = lock.newCondition();
	}
	
	void deposit(int k) {
		lock.lock();
		saldo += k;
		lock.unlock();
		condition.signalAll();
		
	}
	
	void withdraw(int k) {
		lock.lock();
		
		try {
			while(saldo < k) condition.await();
			saldo -= k;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		lock.unlock();
	}
}
