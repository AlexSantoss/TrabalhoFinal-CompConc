import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
Problema do banheiro compartilhado usando lock (Exercicio 96)
Para esse problema, dois pessoas de sexos diferentes nao podem entrar no banheiro ao mesmo tempo (Exclusao Mutua) e
todo mundo eventualmente vai conseguir entrar no banheiro (Starvation-Free)
Esse codigo satisfaz a Exclusao Mutua porque sempre que uma pessoa quer entrar, ela tem que esperar que alguem do mesmo sexo
entre ou que nao tenha ninguem no banheiro.
Starvation-free tambem foi satisfeito ja que, se alguem de sexo oposto ao que esta no banheiro quiser entrar, uma nova pessoa
do mesmo sexo da pessoa que esta no banheiro nao consegue entrar.
*/

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
