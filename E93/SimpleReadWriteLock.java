import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SimpleReadWriteLock implements ReadWriteLock{
	
	int readers;
	boolean writer;
	Object lock;
	Lock readLock, writeLock;
	
	public SimpleReadWriteLock() {
		writer = false;
		readers = 0;
		readLock = new ReadLock();
		writeLock = new WriteLock();
	}

	@Override
	public Lock readLock() {
		return readLock;
	}

	@Override
	public Lock writeLock() {
		return writeLock;
	}
	
	class ReadLock implements Lock {

		@Override
		public void lock() {
			synchronized(lock) {
				try {
					while(writer) lock.wait();
					readers++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void unlock() {
			synchronized(lock) {
				readers--;
				if(readers == 0) lock.notifyAll();
			}
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	class WriteLock implements Lock {

		@Override
		public void lock() {
			synchronized(lock) {
				try {
					while(readers > 0) lock.wait();
					writer = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void unlock() {
			writer = false;
			lock.notifyAll();
		}
		
		@Override
		public void lockInterruptibly() throws InterruptedException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean tryLock() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Condition newCondition() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
