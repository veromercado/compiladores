
public class AnLex {
	
	private static final int TAMLEX = 50;
	
	private int consumir;
	private String id = "";
	
	public void error(String mensaje)
	{
		System.out.println(" Error Lexico. "+mensaje);	
	}
	
	public CompLexico buscarPalabraReservada(String palabra){
		if (palabra.equalsIgnoreCase("if")){
			return CompLexico.PR_IF;
		} else if (palabra.equalsIgnoreCase("true")){
			return CompLexico.PR_TRUE;
		} else if (palabra.equalsIgnoreCase("false")){
			return CompLexico.PR_FALSE;
		} else if (palabra.equalsIgnoreCase("alert")){
			return CompLexico.PR_ALERT;
		}
		return CompLexico.ID;
	}
	
	public void sigLex(String linea){
		
		//Token t []= new Token[100];
		Token t = new Token ();
		int i = 0;
		int nro = 0;
		String c = "";
		int acepto = 0;
		int estado = 0;
		String msg;
		int index = 0;		
		linea = linea.trim();
		
		while( index < linea.length()){
			c = String.valueOf(linea.charAt(index));
					
			if (c.matches("[a-zA-Z_]")){ 
				// reconocedor de identificadores o palabras reservadas
				
				do{
					index++;
					id = id + c;
					i++;
					c = String.valueOf(linea.charAt(index));					
					if (i > TAMLEX){
						error("Longitud de Identificador excede tamaño de buffer");
					}
					System.out.println("Palabra: "+ id);
					System.out.println("Caracter: "+ c);
					//index++;
				}while(index < linea.length()-1 && (c.matches("[a-zA-Z_]") || c.matches("[0-9]") ));
									
			
				if (index < linea.length()-1){
					c = String.valueOf(linea.charAt(index-1));
				}else{								
					t.setLexema(id);
					t.setCompLexico(buscarPalabraReservada(id));				
					id = "";					
					nro++;
					System.out.print(t.getCompLexico() + " ");
					
				} 
							
			} else if (c.matches("[0-9]")){
				
				i = 0;
				index++;
				estado = 0;
				acepto = 0;
				id = id + c;
				
				while(acepto == 0 && index < linea.length())
				{
					switch(estado){
					case 0: //una secuencia netamente de digitos, puede ocurrir . o e
						
						c = String.valueOf(linea.charAt(index));
						
						if (c.matches("[0-9]"))	{							
							id = id + c;
							estado = 0;
							index++;
							
						} else if(c.equals(".")){
							id = id + c;
							estado = 1;
							index++;
							
						} else if(c.toLowerCase().equals("e")){
							id = id + c;
							estado = 3;
							index++;
							
						} else{
							
							estado = 6;
							index++;
						}
						
						break;
					
					case 1://un punto, debe seguir un digito 
						
						c = String.valueOf(linea.charAt(index));
						
						if (c.matches("[0-9]")) { 
							id = id + c;
							estado = 2;
							index++;
							
						} else {
							System.out.println("No se esperaba " + c);
							estado = -1;
							
						}
						
						break;
					
					case 2://la fraccion decimal, pueden seguir los digitos o e
					
						c = String.valueOf(linea.charAt(index));
						
						if (c.matches("[0-9]")) {
							id = id + c;
							estado = 2;
							index++;
							
						} else if(c.toLowerCase().equals("e")) {
							id = c;
							estado = 3;
							index++;
							
						} else {
							estado = 6;
							index++;
						}
					
						break;
						
					case 3://una e, puede seguir +, - o una secuencia de digitos
						
						c = String.valueOf(linea.charAt(index));
						
						if (c.equals("+") || c.equals("-")) {
							id = id + c;
							estado = 4;
							index++;
							
						} else if(c.matches("[0-9]")) {
							id = id + c;
							estado = 5;
							index++;
							
						} else {
							System.out.println("No se esperaba " + c);
							estado = -1;
							
						}
						break;
						
					case 4://necesariamente debe venir por lo menos un digito
						
						c = String.valueOf(linea.charAt(index));
						
						if (c.matches("[0-9]")) {
							id = id + c;
							estado = 5;
							index++;
							
						} else {
							System.out.println("No se esperaba " + c);
							estado = -1;
							
						}
						
						break;
						
					case 5://una secuencia de digitos correspondiente al exponente
						
						c = String.valueOf(linea.charAt(index));
						
						if (c.matches("[0-9]")){
							id = id + c;
							estado = 5;
							index++;
							
						} else {
							estado = 6;
							index++;
							
						}
						
						break;
						
					case 6://estado de aceptacion, devolver el caracter correspondiente a otro componente lexico
						
						if (!c.equals(CompLexico.EOF)){													
							index--;
							
						} else {
							c = "";
							id = id + "\0";
							acepto = 1;
							t.setCompLexico(CompLexico.LITERAL_NUM);
							System.out.print(t.getCompLexico() + " ");
							index++;
						}
						
						break;
						
					case -1:
						if (c.equals(CompLexico.EOF)){							
							error("No se esperaba el fin de archivo");
						} 
						
					}
				}
				
				break;
			
			} else if (c.equals("<")){
				//es un operador relacional, averiguar cual
				index++;
				c = String.valueOf(linea.charAt(index));
				
				if (c.equals("=")){
					t.setCompLexico(CompLexico.OP_RELACIONAL);
					System.out.println(t.getCompLexico());
				}
				else{
					index--;
					t.setCompLexico(CompLexico.OP_RELACIONAL);
					System.out.println(t.getCompLexico());
				}
				break;
				
			} else if (c.equals(">")) {
				//es un operador relacional, averiguar cual
				index++;
				c = String.valueOf(linea.charAt(index));

				if (c.equals("=")){
					t.setCompLexico(CompLexico.OP_RELACIONAL);
					System.out.println(t.getCompLexico());
				}
				else{
					index--;
					t.setCompLexico(CompLexico.OP_RELACIONAL);
					System.out.println(t.getCompLexico());
				}
				break;
				
			} else if (c.equals("+")) {
				
				t.setCompLexico(CompLexico.OP_SUMA);
				System.out.println(t.getCompLexico());
				break;
				
			} else if (c.equals("-")) {
				
				index++;
				c = String.valueOf(linea.charAt(index));

				if (c.equals(">")){
					t.setCompLexico(CompLexico.DELIMITADOR_CODIGO);
					System.out.println(t.getCompLexico());
				}
				else{
					index--;
					t.setCompLexico(CompLexico.OP_SUMA);
					System.out.println(t.getCompLexico());
				}
				break;
				
				
			} else if (c.equals("*")) {
				t.setCompLexico(CompLexico.OP_MUL);
				System.out.println(t.getCompLexico());				
				break;
				
			} else if (c.equals("/")) {
				
				t.setCompLexico(CompLexico.OP_MUL);
				System.out.println(t.getCompLexico());
				break;
				
			} else if (c.equals("=")) {
				//operador asignacion o op_relacional
				index++;
				c = String.valueOf(linea.charAt(index));

				if (c.equals("=")){
					t.setCompLexico(CompLexico.OP_RELACIONAL);
					System.out.println(t.getCompLexico());
				}
				else{
					index--;
					t.setCompLexico(CompLexico.OP_ASIGNACION);
					System.out.println(t.getCompLexico());
				}
				break;
				
			} else if (c.equals("!")) {
				//operador relacional
				index++;
				c = String.valueOf(linea.charAt(index));

				if (c.equals("=")){
					t.setCompLexico(CompLexico.OP_RELACIONAL);
					System.out.println(t.getCompLexico());
				}
				else{
					index--;
					System.out.println("No se esperaba " + c );					
				}
				break;
				
			} else if (c.equals("?")) {

				t.setCompLexico(CompLexico.OP_CONDICION);
				System.out.println(t.getCompLexico());
				break;
				
			} else if (c.equals(",")) {

				t.setCompLexico(CompLexico.COMA);
				System.out.println(t.getCompLexico());
				break;
				
			} else if (c.equals(";")) {
				
				t.setCompLexico(CompLexico.TERMINADOR_PUNTOCOMA);
				System.out.println(t.getCompLexico());
				break;
				
			} else if (c.equals("(")) {
				
				t.setCompLexico(CompLexico.L_PARENTESIS);
				System.out.println(t.getCompLexico());
				break;
			} else if (c.equals(")")) {
				
				t.setCompLexico(CompLexico.R_PARENTESIS);
				System.out.println(t.getCompLexico());
				break;
				
			} else if (c.equals("[")) {
				
				t.setCompLexico(CompLexico.L_CORCHETE);
				System.out.println(t.getCompLexico());
				break;
			} else if (c.equals("]")) {
				
				t.setCompLexico(CompLexico.R_CORCHETE);
				System.out.println(t.getCompLexico());
				break;
			} else if (c.equals("#")) {
				//comentario
				t.setCompLexico(CompLexico.COMMENT);
				System.out.println(t.getCompLexico());
				index = linea.length();
				break;
			}
			
		}
		if (c.equals("EOF"))
		{
			t.setCompLexico(CompLexico.EOF);
			System.out.println(t.getCompLexico());
		}
															
	}

}
