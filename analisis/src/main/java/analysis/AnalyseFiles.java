/**
 * 
 */
package analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p><code>AnalyseFiles.java</code></p>
 * @author cristiano
 * @version Feb 11, 2017 11:43:48 AM
 *
 */
public class AnalyseFiles {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		//F:\\Disco\\Local Disk\\Auto CAd 2010
		File dir = new File("F:\\Disco\\Local Disk");
		
		
		List<File> lsf = onDir(dir);
		lsf.sort(new Comparator<File>() {
			public int compare(File o1, File o2) {
				int ret = 0;
				if(o1.length() < o2.length()){
					ret = -1;
				}
				else if(o1.length() > o2.length()){
					ret = 1;
				}
				return ret;
			};
		});
		
		for(File f : lsf){
			System.out.println(f.getPath() + " - " + (f.length()/1024)/1024d);
		}
		

	}

	private static List<File> onDir(File f){

		List<File> ret = new ArrayList<>();

		if(f != null && f.isDirectory()){
			if(f.isDirectory()){
				for(File fs : f.listFiles()){
					if(!fs.isDirectory()){
						ret.add(fs);
					}
					else{
						for(File fss:onDir(fs)){
							ret.add(fss);
						}
					}
				}
			}
			else{
				ret.add(f);
			}
		}

		return ret;
	}


}
