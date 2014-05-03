import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class App {

	public static void main(String[] args) {
		
		AnLex an = new AnLex();
		File f = new File( "D:fuente.txt" );
		BufferedReader entrada;
		try {
			entrada = new BufferedReader( new FileReader( f ) );
			String linea;
			
			while(entrada.ready()){
				linea = entrada.readLine();
				an.sigLex(linea);
				System.out.println(linea);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		

		

	}
	
}
