import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedBathroom2 {
	
	char usando;
	char aguardando;
	int quant;
	Object lock;
	
	public SharedBathroom2() {
		usando = 'M';
		aguardando = 0;
		quant = 0;
	}
	
	public void enter(char quem) throws InterruptedException {
		char oposto = (quem == 'M')? 'F' : 'M';
		synchronized(lock) {
			if(usando == quem) {
				while(aguardando == oposto) lock.wait();
			}
			
			if(usando == oposto) {
				aguardando = quem;
				while(quant != 0 || usando == oposto) lock.wait();
			}
			
			usando = quem;
			quant++;
		}
	}
	
	private void leave() {
		synchronized(lock) {
			quant--;
			lock.notifyAll();
		}
	}
	
	public void enterMale() {
		try {
			enter('M');
		} catch(InterruptedException e) {}
	}
	
	public void enterFemale() {
		try {
			enter('F');
		} catch(InterruptedException e) {}
	}
	
	public void leaveMale() {
		leave();
	}
	
	public void leaveFemale() {
		leave();
	}

}
