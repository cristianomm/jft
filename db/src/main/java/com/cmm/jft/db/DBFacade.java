/**
 * 
 */
package com.cmm.jft.db;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import javax.persistence.EntityManagerFactory;

import org.apache.log4j.Level;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;

import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>DBFacade.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 10, 2013 6:45:28 PM
 * 
 */
public class DBFacade {

	private int level;
	private Semaphore sem;
	private static DBFacade instance;
	private EntityManagerFactory emf;

	private static Configuration configuration = new Configuration();
	/* Constante de caminho do arquivo de configura��o do Hibernate */
	private static String DEFAULT_CONFIG_FILE_LOCATION = "./META-INF/hibernate.cfg.xml";

	/* Threads que controlar�o a sess�o e a transa��o */
	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	/* Variaveis do Hibernate */
	private static SessionFactory sessionFactory;

	private int batchNum;
	private int batchSize = 10000;
	private ExecutorService exe;
	private LinkedBlockingQueue<DBObject> batchQueue;
	//    private LinkedBlockingQueue<Callable<Integer>> threads;

	public DBFacade() {
		this.sem = new Semaphore(1);
		this.exe = Executors.newFixedThreadPool(6);
		this.batchQueue = new LinkedBlockingQueue<DBObject>(batchSize);	

		try {
			configuration.configure(DEFAULT_CONFIG_FILE_LOCATION);
			//ServiceRegistry sr = new ServiceRegistryBuilder().buildServiceRegistry();
			//sessionFactory = configuration.buildSessionFactory(sr);
			
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
			.applySettings(configuration.getProperties()).build();
		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			
		} catch (Exception e) {
			throw e;
		}

	}

	//    public EntityManager getEntityManager() {
	//	return emf.createEntityManager();
	//    }
	//
	//    public void close() {
	//	emf.close();
	//    }

	public static synchronized DBFacade getInstance() {

		if (instance == null) {
			instance = new DBFacade();
		}
		return instance;
	}

	//    /**
	//     * M�todo que retorna a instancia da Sess�o utilizando o arquivo default do hibernate (/hibernate.cfg.xml).
	//     * @return Session
	//     * @throws SessionFactoryException
	//     */
	//    public synchronized Session getCurrentSession() throws DataBaseException {
	//	return getCurrentSession(DEFAULT_CONFIG_FILE_LOCATION);
	//    }



	private class Conn{Connection connection;}

	public Connection getConnection() {

		final Conn conn = new Conn();
		Session session;
		try {
			session = getCurrentSession();
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					conn.connection = connection;
				}
			});

		} catch (DataBaseException e) {
			e.printStackTrace();
		}

		return conn.connection;

	}


	int ss=0;
	/**
	 * M�todo que retorna a instancia da Sess�o.
	 * @return Session
	 * @throws SessionFactoryException
	 */
	public synchronized Session getCurrentSession() throws DataBaseException {

		Session session = null;
		try {
			session = threadSession.get();

			if (session == null || !session.isOpen()) {
				session = (sessionFactory != null) ? sessionFactory.openSession() : null;
				threadSession.set(session);
			}
		} catch (Exception e) {
			throw new HibernateException(e);
		}

		return session;
	}

	/**
	 * M�todo que fecha a sess�o do Hibernate.
	 * @throws SessionFactoryException
	 */
	public synchronized void closeSession() throws DataBaseException {
		Session session = getCurrentSession();
		threadSession.set(null);

		try {
			if (session != null && session.isOpen()) {
				session.close();
			}
		} catch (Exception e) {
			throw new HibernateException(e);
		}
	}

	public DBObject attachToSession(DBObject dbObject, Serializable objectID) {
		try {
			Session s = getCurrentSession();
			dbObject = (DBObject) s.get(dbObject.getClass(), objectID);
			//dbObject = (DBObject) s. merge(dbObject);
		} catch (DataBaseException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dbObject;
	}


	/**
	 * M�todo que inicia a transa��o do Hibernate.
	 * @throws SessionFactoryException
	 */
	public synchronized void beginTransaction() throws DataBaseException {

		level++;
		try {
			Transaction tx = threadTransaction.get();
			if(tx == null && level ==1){
				tx = getCurrentSession().beginTransaction();
				threadTransaction.set(tx);
			}
		} catch (Exception e) {
			throw new DataBaseException(e);
		}
	}

	/**
	 * M�todo que executa o rollback da transa��o.
	 * @throws SessionFactoryException
	 */
	public synchronized void rollback() throws DataBaseException {

		//	System.out.println(spaces + "rollback " + level);
		//	spaces = spaces.replaceFirst(" ", "");
		try {
			Transaction tx = threadTransaction.get();
			if(tx != null && !tx.wasCommitted() && !tx.wasRolledBack() && level==1){
				tx.rollback();
				threadTransaction.set(null);
			}
		} catch (Exception e) {
			throw new DataBaseException(e);
		}
		level--;
	}

	/**
	 * M�todo que commita a transa��o.
	 * @throws SessionFactoryException
	 */
	public synchronized void commit() throws DataBaseException {
		//	System.out.println(spaces + "commit "+ level);
		//	spaces = spaces.replaceFirst(" ", "");
		try {
			Transaction tx = threadTransaction.get();
			if(tx != null && !tx.wasCommitted() && !tx.wasRolledBack() && level ==1){
				tx.commit();
				threadTransaction.set(null);
				//closeSession();
			}
		} catch (Exception e) {
			rollback();
			throw new DataBaseException(e);
		}finally {
			//closeSession();
		}
		level--;
	}




	public Object queryByRange(String jpqlStmt, int firstResult, int maxResults) {
		//	Query query = getEntityManager().createQuery(jpqlStmt);
		//	if (firstResult > 0) {
		//	    query = query.setFirstResult(firstResult);
		//	}
		//	if (maxResults > 0) {
		//	    query = query.setMaxResults(maxResults);
		//	}
		//	return query.getResultList();

		//	Session session = null;
		//	try {
		//	    session = getCurrentSession();
		//	    Query qr = session.createQuery("truncate table " + table);	    
		//	    ret = qr.executeUpdate();
		//	    
		//	} catch (Exception e) {
		//	    Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		//	} finally {
		//	    if(session==null) {
		//		rollback();
		//	    }
		//	    else if(session!=null && session.isOpen()&&level==0) {
		//		closeSession();
		//	    }
		//	}
		return null;

	}

	public Object _persist(Object entity) throws DataBaseException {
		Session session = null;
		try {
			session = getCurrentSession();
			beginTransaction();
			entity = session.merge(entity);
			//entity = session.get(entity.getClass(), session.save(entity));
			commit();

		} catch (Exception e) {
			rollback();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} 
		//	finally {
		//	    if(session!=null && session.isOpen() && level==0) {
		//		closeSession();
		//	    }
		//	}
		return entity;
	}

	public Object _update(Object entity) throws DataBaseException {
		Session session = null;
		try {
			session = getCurrentSession();
			beginTransaction();
			entity = session.merge(entity);
			commit();

		} catch (Exception e) {
			rollback();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} 
		//	finally {
		//	    if(session!=null && session.isOpen()&&level==0) {
		//		closeSession();
		//	    }
		//	}

		return entity;
	}

	public Object _remove(Object entity) throws DataBaseException {
		Session session = null;
		try {
			session = getCurrentSession();
			beginTransaction();
			session.delete(entity);
			commit();

		} catch (Exception e) {
			rollback();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} 
		//	finally {
		//	    if(session!=null && session.isOpen()&&level==0) {
		//		closeSession();
		//	    }
		//	}

		return entity;
	}

	public void queryAsMap(String namedQuery, HashMap map, Class cls, String method) throws DataBaseException {
		try {

			if(map==null) {
				map = new HashMap<>();
			}

			List<Object> l = DBFacade.getInstance()._listResults(namedQuery, null);
			for(Object c:l) {

				for(Method m: cls.getMethods()) {
					if(method.equalsIgnoreCase(m.getName())) {

						Object obj = m.invoke(c, null);
						if(obj!=null) {
							map.put(obj, c);
							break;
						}
					}
				}

			}
		}catch(Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}


	public void queryAsMap(String query, String idColumn, HashMap map, Class cls) throws DataBaseException {
		try {

			if(map==null) {
				map = new HashMap<>();
			}

			List<Object> l = DBFacade.getInstance().queryNative(query);
			for(Object c:l) {

				//map.


				//		for(Method m: cls.getMethods()) {
				//		    if(method.equalsIgnoreCase(m.getName())) {
				//			Object obj = m.invoke(c, null);
				//			if(obj!=null) {
				//			    map.put(obj, c);
				//			    break;
				//			}
				//		    }
				//		}

			}
		}catch(Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}


	public List _listResults(String namedQuery, HashMap<String, Object> params) throws DataBaseException {
		List ret = null;
		Session session = null;
		try {
			session = getCurrentSession();
			Query qr = session.getNamedQuery(namedQuery);

			if(params!=null) {
				for(String pName:params.keySet()) {
					qr.setParameter(pName.trim(), params.get(pName));
				}
			}

			ret = qr.list();

		} catch (Exception e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} 
		//	finally {
		//	    if(session!=null && session.isOpen()&&level==0) {
		//		closeSession();
		//	    }
		//	}
		return ret;
	}

	public Object _findByKey(Class<?> clss, Object key) throws DataBaseException {
		Object ret = null;
		Session session = null;
		try {
			session = getCurrentSession();
			ret = session.load(clss, (Serializable) key);
		} catch (Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} 
		//	finally {
		//	    if(session!=null && session.isOpen()&&level==0) {
		//		closeSession();
		//	    }
		//	}

		return ret;

	}

	@SuppressWarnings("unchecked")
	public List<Object> queryNative(String query) throws DataBaseException {
		List<Object> result = null;

		Session session = null;
		try {
			session = getCurrentSession();
			Query qr = session.createSQLQuery(query);
			result = qr.list();

		} catch (Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} finally {
			if(session!=null && session.isOpen()&&level==0) {
				closeSession();
			}
		}

		return result;
	}

	public List<?> queryNamed(String namedQuery, HashMap<String, Object> params) throws DataBaseException {
		return _listResults(namedQuery, params);
	}

	public void _truncateTable(String table) throws DataBaseException {
		int ret=0;
		Session session = null;
		try {
			beginTransaction();
			session = getCurrentSession();
			Query qr = session.createQuery("truncate table " + table);	    
			ret = qr.executeUpdate();
			commit();

		} catch (Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} finally {
			if(session==null) {
				rollback();
			}
			else if(session!=null && session.isOpen()&&level==0) {
				closeSession();
			}
		}

	}

	public Object findObject(String namedQuery, String paramName, Object paramValue) {

		Object object = null;
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put(paramName, paramValue);

			List l = queryNamed(namedQuery, params);
			if(l!=null && !l.isEmpty()) {
				object = l.get(0);
			}
			else {
				Logging.getInstance().log(getClass(), 
						"Nao foi possivel encontrar o registro com os parametros indicados: " + 
								paramName + " - " + paramValue, null, Level.INFO, false);
			}

		}catch(DataBaseException e) {
			Logging.getInstance().log(getClass(), "Erro - findObject", e, Level.ERROR, false);
		}

		return object;
	}

	public synchronized Long getNextKey(String tableName, String keyName) {
		// try {
		// sem.acquire();
		//
		// if (keys.containsKey(tableName)) {
		// if (keys.get(tableName).isEmpty()) {
		// addKeys(tableName, keyName);
		// }
		//
		// } else {
		// keys.put(tableName, new LinkedList<Long>());
		// addKeys(tableName, keyName);
		// }
		//
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } finally {
		// sem.release();
		// }
		//
		// return keys.get(tableName).remove(0);

		long mm = 0;
		try {
			sem.acquire();
			mm = _MINMAX(tableName, keyName, "MAX") + 1;
		} catch (InterruptedException e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} finally {
			sem.release();
		}
		return mm;
	}

	public synchronized Long getMaxKey(String tableName, String keyName) {
		return _MINMAX(tableName, keyName, "MAX");
	}

	public synchronized Long getMinKey(String tableName, String keyName) {
		return _MINMAX(tableName, keyName, "MIN");
	}

	private synchronized Long _MINMAX(String tableName, String keyName,
			String operation) {
		long key = 1;
		try {
			String query = "select " + operation + "(" + keyName + ") from "
					+ tableName;
			//	    key = (Integer) ((Vector<?>) getEntityManager().createNativeQuery(query).getSingleResult()).firstElement();

		} catch (ClassCastException e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} catch (Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
		return key;
	}


	public synchronized int batchInsert(Queue<DBObject> objs) {

		int count=0;
		int flushSize = 50;
		Session session = null;
		Transaction tx = null;
		DBObject obj=null;
		System.out.println("inserindo batch " + batchNum++);
		try {
			session = getCurrentSession();
			tx = session.beginTransaction();
			ArrayList<DBObject> temp = new ArrayList<DBObject>(flushSize);
			while (!objs.isEmpty()) {
				if(!session.isOpen()||tx.wasRolledBack()) {
					session = getCurrentSession();
					tx = session.beginTransaction();
				}

				obj=objs.poll();
				try {
					temp.add(obj);
					session.merge(obj);// merge(obj);
					count++;

					if (count % flushSize == 0 ) { //50, same as the JDBC batch size
						//flush a batch of inserts and release memory:
						session.flush();
						session.clear();
						temp = new ArrayList<DBObject>(flushSize);
					}
				}catch(Exception e) {
					tx.rollback();
					try {
						closeSession();
					} catch (DataBaseException ex) {
						ex.printStackTrace();
					} catch (Exception ex) {
						ex.printStackTrace();
					}

					Logging.getInstance().log(getClass(), "Erro ao ajustar registros no batch:"+e.getMessage(), e, Level.ERROR, false);
					tryToAdd(temp);
					temp = new ArrayList<DBObject>(flushSize);
				}
			}

			try {
				session.flush();
				session.clear();
			}catch(Exception e) {
				Logging.getInstance().log(getClass(), "Erro ao ajustar registros no batch:"+e.getMessage(), e, Level.ERROR, false);
				tryToAdd(temp);
			}

			tx.commit();
		} catch (DataBaseException e) {
			count=0;
			System.out.println(obj.toString());
			tx.rollback();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} finally {
			try {
				closeSession();
			} catch (DataBaseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		return count;
	}

	private void tryToAdd(ArrayList<DBObject> temp) {

		Session session = null;
		Transaction tx = null;

		for (DBObject obj:temp) {
			try {
				session = getCurrentSession();
				tx = session.beginTransaction();
				session.merge(obj);
				tx.commit();
			} catch(DataBaseException e) {
				tx.rollback();
				Logging.getInstance().log(getClass(), "Erro ao adicionar registro: " + obj.toString(), e, Level.ERROR, false);
			} catch(Exception e) {
				tx.rollback();
				Logging.getInstance().log(getClass(), "Erro ao adicionar registro: " + obj.toString(), e, Level.ERROR, false);
			} finally {
				try {
					closeSession();
				} catch (DataBaseException e) {
					e.printStackTrace();
				}
			}

		}
	}



	public synchronized void addToBatch(DBObject obj) {

		if(batchQueue.size()>=batchSize) {
			LinkedBlockingQueue<DBObject> l = new LinkedBlockingQueue<DBObject>(batchSize);

			while(!batchQueue.isEmpty()) {
				l.offer(batchQueue.poll());
			}

			//threads.offer(new BatchWorker(l));
			batchInsert(l);
		}
		batchQueue.offer(obj);

	}

	public synchronized void finalizeBatch() {
		LinkedBlockingQueue<DBObject> l = new LinkedBlockingQueue<DBObject>(batchSize);

		while(!batchQueue.isEmpty()) {
			l.offer(batchQueue.poll());
		}

		batchInsert(l);
		batchNum=0;

		//	threads.offer(new BatchWorker(l));
		//
		//	boolean working=true;
		//	while(working) {
		//	    working=false;
		//	    try {
		//		Thread.sleep(5000);
		//	    } catch (InterruptedException e) {
		//		e.printStackTrace();
		//	    }
		//	    int tc=0;
		//	    for(Future<Integer> ft:runningTasks) {
		//		if(!ft.isDone()) {
		//		    tc++;
		//		    working=true;
		//		}
		//	    }
		//	    System.out.println("Running tasks: " + tc);
		//	}
		//
		//	int qt=0;
		//	for(Future<Integer> ft:runningTasks) {
		//	    try {
		//		qt+=ft.get();
		//	    } catch (InterruptedException e) {
		//		e.printStackTrace();
		//	    } catch (ExecutionException e) {
		//		e.printStackTrace();
		//	    }
		//	}
		//	runningTasks.clear();
		//	c=0;
		//	System.out.println("Batch has inserted " + qt + " objects");

	}

	//    private LinkedBlockingQueue<Future<Integer>> runningTasks = new LinkedBlockingQueue<Future<Integer>>();
	//    private class ThreadExecutor implements Runnable{
	//
	//	/* (non-Javadoc)
	//	 * @see java.lang.Runnable#run()
	//	 */
	//	@Override
	//	public void run() {
	//	    while(true) {
	//		while(!threads.isEmpty()) {
	//		    runningTasks.offer(exe.submit(threads.poll()));
	//		    System.out.println("removendo batch " + " " + threads.size());
	//		}
	//		try {
	//		    Thread.sleep(5000);
	//		} catch (InterruptedException e) {
	//		    e.printStackTrace();
	//		}
	//	    }
	//	}
	//
	//    }
	//
	//
	//    private class BatchWorker implements Callable<Integer> {
	//
	//	private LinkedBlockingQueue<DBObject> objsQueue;
	//
	//	public BatchWorker(LinkedBlockingQueue<DBObject> queue) {
	//	    this.objsQueue = queue;
	//	}
	//
	//	private int batchInsert(Queue<DBObject> objs) {
	//
	//	    int count=0;
	//	    Session session = null;
	//	    Transaction tx = null;
	//	    try {
	////		session=sessionFactory.openSession(); //getCurrentSession();
	//		session = configuration.buildSessionFactory().openSession();
	//		tx = session.beginTransaction();
	//
	//		while(!objs.isEmpty()) {
	//		    session.save(objs.poll());
	//		    count++;
	//		    if (count % 50 == 0 ) { //50, same as the JDBC batch size
	//			//flush a batch of inserts and release memory:
	//			session.flush();
	//			session.clear();
	//		    }
	//		    //System.out.println(objs.size());
	//		}
	//
	//		//salva o resto
	//		session.flush();
	//		session.clear();
	//
	//		tx.commit();
	//		session.close();
	//	    } catch (Exception e) {
	//		count=0;
	//		if(tx!=null) {
	//		    tx.rollback();
	//		}
	//		e.printStackTrace();
	//		Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	//	    } 
	////		catch (Exception e) {
	////		e.printStackTrace();
	////		Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	////	    }
	//	    return count;
	//	}
	//
	//	/* (non-Javadoc)
	//	 * @see java.util.concurrent.Callable#call()
	//	 */
	//	@Override
	//	public Integer call() throws Exception {
	//	    return batchInsert(objsQueue);
	//	}
	//
	//
	//
	//
	//    }


}
