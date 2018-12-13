import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedBathroom1 {
	
	char usando;
	char aguardando;
	int quant;
	Lock lock;
	Condition condition;
	
	public SharedBathroom1() {
		usando = 'M';
		aguardando = 0;
		quant = 0;
		lock = new ReentrantLock();
		condition = lock.newCondition();
	}
	
	public void enter(char quem) {
		char oposto = (quem == 'M')? 'F' : 'M';
		lock.lock();
		
		try {
			if(usando == quem) {
				while(aguardando == oposto) condition.await();
			}
			
			if(usando == oposto) {
				aguardando = quem;
				while(quant != 0 || usando == oposto) condition.await();
			}
			
			usando = quem;
			quant++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		lock.unlock();
		
	}
	
	private void leave() {
		lock.lock();
		quant--;
		lock.unlock();
		
		condition.signalAll();
	}
	
	public void enterMale() {
		enter('M');
	}
	
	public void enterFemale() {
		enter('F');
	}
	
	public void leaveMale() {
		leave();
	}
	
	public void leaveFemale() {
		leave();
	}

}
