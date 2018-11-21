/**
 * 
 */
package com.cmm.jft.data.files;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

/**
 * <p>
 * <code>FTPBMFBovespa.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 24/09/2013 02:53:34
 *
 */
public class FTPBMFBovespa {

	private FTPClient ftp;
	private HashMap<String, String> filesIndex;

	private static final String BASE_DIR = "";
	private static final String FTP_BMFBovespa = "ftp.bmf.com.br";
	private static final String BMF_DIR = "/MarketData/BMF/";
	private static final String OPCOES_DIR = "/MarketData/Bovespa-Opcoes/";
	private static final String AVISTA_DIR = "/MarketData/Bovespa-Vista/";
	private static final String TEMP_DIR = "/MarketData/Temp/";

	public static void main(String[] args) {
		//	int size = 200_000_000;
		//	final Random rand = new Random();
		//	final double[] elements = new double[size];
		//
		//	System.out.println("creating elements list...");
		//
		//	int part=8;
		//	boolean[] status = new boolean[part];
		//	for(int i=0;i<part;i++){
		//	    final int p=i;
		//	    int partSize=size/part;
		//	    int partInit = partSize*i;
		//	    int partEnd = partInit + partSize;
		//	    System.out.println(partSize + " - " + partInit + "-> "+ partEnd);
		//
		//	    new Thread(new Runnable(){
		//		@Override
		//		public void run() {
		//		    for(int i=partInit;i<partEnd;i++){
		//			elements[i] = rand.nextDouble();
		//		    }
		//		    System.out.println(p + " done!");
		//		    status[p] = true;
		//		}
		//	    }).start();
		//	}
		//	System.out.println(status[0]);
		//	boolean end = status[0];
		//	while(!end){
		//	    end = status[0];
		//	    for(boolean b:status){
		//		end = end && b;
		//	    }
		//	    try {
		//		Thread.sleep(500);
		//	    } catch (InterruptedException e) {
		//		e.printStackTrace();
		//	    }
		//
		//	}
		//
		//	System.out.println("done!");
		//
		//	System.exit(0);

		//H:/Disco/Bancos/BM&FBovespa/MarketData/BMF
		final String saveDir = "D:/Disco/Bancos/BM&FBovespa";

		FTPBMFBovespa ftp = new FTPBMFBovespa();
		if(ftp.connect("anonymous", "", FTP_BMFBovespa)) {
			List<String> dFiles = ftp.getInexistentLocalFiles(BMF_DIR, saveDir + BMF_DIR);
			ftp.downloadFiles(dFiles, BMF_DIR, saveDir, TEMP_DIR);
		}

		//anonymous
		new Thread(new Runnable() {
			public void run() {
				//		FTPBMFBovespa ftp = new FTPBMFBovespa();
				//		if(ftp.connect("anonymous", "", FTP_BMFBovespa)) {
				//
				//		    List<String> dFiles = ftp.getInexistentLocalFiles(BMF_DIR, saveDir + BMF_DIR);
				//
				//		    ftp.downloadFiles(dFiles, BMF_DIR, saveDir, TEMP_DIR);
				//		}
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				//new FTPBMFBovespa().dowFiles(FTP_BMFBovespa, OPCOES_DIR, saveDir, TEMP_DIR);
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				//new FTPBMFBovespa().dowFiles(FTP_BMFBovespa, AVISTA_DIR, saveDir, TEMP_DIR);
			}
		}).start();

	}

	public boolean connect(String user, String password, String ftpURL ){
		boolean ret = false;
		try{
			ftp = new FTPClient();
			ftp.connect(ftpURL);
			ftp.login(user, password);
			ret = ftp.isConnected();
		}catch(FTPException e){
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Verifica se os arquivos passados por parametro estao presentes no FTP
	 * @param files Lista de arquivos que ser quer verificar a existencia no diretorio do FTP.
	 * @return Map contendo a intersecao entre os arquivos passados por parametro 
	 * e os arquivos no diretorio do FTP.
	 */
	public HashMap<String, File> checkFilesonFTP(String ftpDir, List<File> files){
		HashMap<String, File> hm = new HashMap<String, File>();
		try{
			if(ftp.isConnected()){
				ftp.changeDirectory(ftpDir);
				FTPFile[] ftpFiles = ftp.list();

				HashMap<String, FTPFile> hFiles = new HashMap<String, FTPFile>();
				for(FTPFile f:ftpFiles){
					hFiles.put(f.getName(), f);
				}

				for(File f:files){
					if(!hFiles.containsKey(f.getName())){
						hm.put(f.getName(), f);
					}
				}

			}
		}catch(IllegalStateException | IOException | 
				FTPIllegalReplyException | FTPException | 
				FTPDataTransferException | FTPAbortedException | 
				FTPListParseException e){

		}

		return hm;
	}

	/**
	 * Retorna uma lista com o nome dos arquivos no FTP que nao existem localmente.
	 * @param ftpDir
	 * @param localDir
	 * @return
	 */
	public List<String> getInexistentLocalFiles(String ftpDir, String localDir){
		List<String> lst = new ArrayList<String>();

		if(ftp.isConnected()){
			try {
				ftp.changeDirectory(ftpDir);
				FTPFile[] files = ftp.list();

				File dir = new File(localDir);
				dir.mkdirs();
				HashMap<String, File> localFiles = new HashMap<String, File>();
				for(File f:dir.listFiles()){
					localFiles.put(f.getName(), f);
				}

				//adiciona na lista de retorno os arquivos do ftp que nao existem localmente
				for(FTPFile f:files){
					//System.out.println(f.getName() +": " + (f.getModifiedDate().getYear()+1900) + " -> " + (f.getModifiedDate().getYear() == 2017));
					if(!localFiles.containsKey(f.getName()) &&  (f.getModifiedDate().getYear() == 117)){
						lst.add(f.getName());
					}
				}

			} catch (IllegalStateException | IOException
					| FTPIllegalReplyException | FTPException | 
					FTPDataTransferException | FTPAbortedException | 
					FTPListParseException e) {
				e.printStackTrace();
			}
		}

		return lst;
	}


	public boolean compareFiles(File local, FTPFile ftpFile){
		boolean ret = local.length() == ftpFile.getSize();

		return ret;
	}

	// ftp://ftp.bmf.com.br/MarketData/BMF/
	public void downloadFiles(List<String> ftpFiles, String ftpDir, String saveDir, String tmpDir) {
		try {
			File finalDir = new File(saveDir + ftpDir);
			finalDir.mkdirs();

			File save = new File(saveDir);
			save.mkdirs();

			File tempDir = new File(saveDir + tmpDir);
			tempDir.mkdirs();

			ftp.changeDirectory(ftpDir);
			for (String fileName : ftpFiles) {
				File f = new File(finalDir.getAbsolutePath() + "\\"	+ fileName);
				File temp = new File(tempDir.getAbsolutePath() + "\\" + fileName);
				if (!f.exists()) {
					System.out.println("Dowloading: " + fileName);
					try {
						//connect("anonymous", "", FTP_BMFBovespa);
						ftp.download(fileName, temp);
						//ftp.disconnect(true);
						Files.move(temp.toPath(), f.toPath(), StandardCopyOption.ATOMIC_MOVE);
						System.out.println(fileName + "... Done!");
					} catch (FTPException e) {
						System.out.println("Error dowloading the file: "
								+ fileName + ": " + e.getMessage());
					} catch (SocketTimeoutException e) {
						System.out.println("Error dowloading the file: "
								+ fileName + ": " + e.getMessage());
					} catch(FTPIllegalReplyException e) {
						e.printStackTrace();
					} 
				}
			}

		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			e.printStackTrace();
		} catch (FTPException e) {
			e.printStackTrace();
		} catch (FTPDataTransferException e) {
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			e.printStackTrace();
		}

	}

}
