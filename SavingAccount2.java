import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SavingAccount2 {

	int saldo;
	Lock lock;
	Condition conditionO, conditionP;
	
	public SavingAccount2() {
		saldo = 0;
		lock = new ReentrantLock();
		
		conditionO = lock.newCondition();
		conditionP = lock.newCondition();
	}
	
	void deposit(int k) {
		lock.lock();
		saldo += k;
		lock.unlock();
		
		conditionP.signalAll();
		conditionO.signalAll();
	}
	
	void withdrawOrdinary(int k) {
		lock.lock();
		
		try {
			while(saldo < k) conditionO.await();
			saldo -= k;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		lock.unlock();
	}
	
	void withdrawPreferred(int k) {
		lock.lock();
		
		try {
			while(saldo < k) conditionP.await();
			saldo -= k;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		lock.unlock();
	}
}
