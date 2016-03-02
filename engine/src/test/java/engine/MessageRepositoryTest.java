package engine;

import static org.junit.Assert.*;

import org.junit.Test;

import quickfix.Message;
import quickfix.field.EncryptMethod;
import quickfix.field.HeartBtInt;
import quickfix.fix44.Logon;

import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;

public class MessageRepositoryTest {

	@Test
	public void testGetInstance() {
		assertNotNull(MessageRepository.getInstance());
	}

	@Test(timeout=1000)
	public void testAddMessage() {
		MessageRepository repo = MessageRepository.getInstance();
		int a = 50;
		int b = 1000;
		
		for(int i=0;i<a;i++) {
			new Thread(new Runnable() {			
				public void run() {
					for(int i=0;i<b;i++) {
						Message message = Fix44EngineMessageEncoder.getInstance().heartbeat();
						//repo.addMessage(message);
					}
				}
			}).start();
		}
		
		try {Thread.sleep(900); }catch(InterruptedException e) {}
		assertTrue(MessageRepository.getInstance().queueSize()== a*b);
	}

	@Test
	public void testRetrieve() {
		MessageRepository repo = MessageRepository.getInstance();
		for(int i=0;i<10000;i++) {
			Logon message = new Logon(new EncryptMethod(0), new HeartBtInt(30));
			//repo.addMessage(message);
		}
		
		assertNotNull(MessageRepository.getInstance().retrieveMessage());
	}

}
