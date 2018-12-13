import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Funciona do mesmo modo que o SharedBathroom1, mudando apenas que uso synchronized, provida pelo java ao inves de usar locks.
Do mesmo jeito que o primeiro, uma pessoa entra no banheiro apenas se alguem do mesmo sexo estiver la dentro ou se o banheiro
estiver vazio.
Sempre que alguem sai, notifica todo mundo para verificar se ja pode entrar
*/

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
