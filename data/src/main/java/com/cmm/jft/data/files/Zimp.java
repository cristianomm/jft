/**
 * 
 */
package com.cmm.jft.data.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * <p>
 * <code>Zimp.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 06/10/2013 17:11:34
 *
 */
public class Zimp {

	public static void main(String[] args) throws ZipException, IOException {
		// C:\Disco\Bancos\Bolsa\BM&FBovespa\MarketData\Test
		extract("C:\\Disco\\Bancos\\Bolsa\\BM&FBovespa\\MarketData\\Test\\OFER_CPA_BMF_20111001_a_20111031.zip",
				"");

		// extract("C:\\Disco\\Bancos\\Bolsa\\BM&FBovespa\\MarketData\\Bovespa-Vista\\NEG_20120801_A_20120831.zip",
		// "");
	}

	public static boolean checkZipFile(String zipFile) {

		boolean ret = false;
		try {
			File file = new File(zipFile);
			if (file.exists()) {
				// tenta abrir o zip, em caso de erro o zip tem problemas...
				new ZipFile(file);
			}
			ret = true;
		} catch (Exception e) {
			ret = false;
		}
		return ret;

	}

	public static List<String> extract(String zipFile, String toDir)
			throws ZipException, IOException {

		ArrayList<String> extractedFiles = new ArrayList<String>();

		ZipFile zip = null;
		File file = null;

		if (checkZipFile(zipFile)) {

			if (toDir == null || toDir.isEmpty()) {
				toDir = zipFile.substring(0, zipFile.lastIndexOf('.'));
			}

			File destDir = new File(toDir);
			InputStream is = null;
			OutputStream os = null;
			byte[] buffer = new byte[4096];
			try {
				// cria diretorio informado, caso não exista
				if (!destDir.exists()) {
					destDir.mkdirs();
				}
				if (!destDir.exists() || !destDir.isDirectory()) {
					throw new IOException("Informe um diretório válido");
				}
				zip = new ZipFile(zipFile);
				Enumeration e = zip.entries();
				while (e.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) e.nextElement();
					file = new File(destDir, entry.getName());
					System.out.println("Extracting: " + entry.getName());
					// se for diretório inexistente, cria a estrutura
					// e pula pra próxima entrada
					if (entry.isDirectory() && !file.exists()) {
						file.mkdirs();
						continue;
					}
					// se a estrutura de diretórios não existe, cria
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					try {
						// lê o arquivo do zip e grava em disco
						is = zip.getInputStream(entry);
						os = new FileOutputStream(file);
						int bytesLidos = 0;
						if (is == null) {
							throw new ZipException(
									"Erro ao ler a entrada do zip: "
											+ entry.getName());
						}
						while ((bytesLidos = is.read(buffer)) > 0) {
							os.write(buffer, 0, bytesLidos);
						}

						// adiciona na lista de extraidos
						extractedFiles.add(file.getAbsolutePath());

					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
						if (os != null) {
							try {
								os.close();
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				}
			} finally {
				if (zip != null) {
					try {
						zip.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// System.out.println("Extracted paths:");
			// for(String fn:extractedFiles) {
			// System.out.println(fn);
			// }
		}
		return extractedFiles;
	}

}
