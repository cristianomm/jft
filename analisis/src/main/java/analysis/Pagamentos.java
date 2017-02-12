/**
 * 
 */
package analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * <p><code>Pagamentos.java</code></p>
 * @author cristiano
 * @version Jan 31, 2017 3:36:31 PM
 *
 */
public class Pagamentos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File dir = new File("C:\\Disco\\Users\\cristiaNo\\Documents\\Contas\\Extratos Ju");
		
		for(File extrato : dir.listFiles()){
			if(extrato.isFile()){
				Scanner sc = null;
				try {
					sc = new Scanner(extrato);
					System.out.println("\n----------------" + extrato.getName());
					
					double depositos = 0;
					while(sc.hasNextLine()){
						String line = sc.nextLine();
						String fields[] = line.split(";");
						
						for(int i=0; i<fields.length;i++){
							fields[i] = fields[i].replace("\"", "");
						}
						
						if((fields[3].equalsIgnoreCase("CRED TEV") || fields[3].equalsIgnoreCase("DP DINH AG")) 
								&& fields[5].equalsIgnoreCase("C") ){
							double valor = Double.parseDouble(fields[4]);
							System.out.println(fields[3] + ": " + valor);
							depositos += valor;
						}
						
					}
					System.out.println("Depositos: " + depositos);
					System.out.println("\n----------------Fim " + extrato.getName() + "\n");
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally{
					if(sc!=null){
						try{
							sc.close();
						}catch(Exception e){
							
						}
					}
				}
			}
		}

	}

}
