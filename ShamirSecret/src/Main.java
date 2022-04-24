import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {


	/* Función iterativa para calcular (x^y) con complejidad temporal O(log y) */
	static int power(int x, int y)
	{
		int res = 1;     // Inicializar resultado

		while (y > 0)
		{

			// si 'y' es impar, multiplicar x con resultado
			if ((y % 2) != 0) {
				res = res * x;
			}

			// 'y' ahora es impar
			y = y >> 1; // y = y/2
			x = x * x;  // x = x^2
		}
		return res;
	}

	public static void main(String[] args) {

		// lectura del archivo que contiene los valores de los secret shares y del número primo p
		try (FileReader reader = new FileReader("data//caso1.txt"); BufferedReader in = new BufferedReader(reader)) { 
			String line = in.readLine();

			int primo = Integer.parseInt(line);

			ArrayList<String> secretos = new ArrayList<String>();

			line = in.readLine();

			while (line!=null) {
				secretos.add(line);
				line = in.readLine();
			}


			// Paso 1: armar la matriz de acuerdo a los puntos secretos
			int[][] array = new int[secretos.size()][secretos.size()+1];

			for(int i = 0; i<secretos.size(); i++) {

				String valor = secretos.get(i).substring(1, secretos.get(i).length()-1);
				String[] valores = valor.split(",");
				int x = Integer.parseInt(valores[0]);
				int y = Integer.parseInt(valores[1]);

				// Armar las potencias de los valores x de los secretos
				for(int j = 0; j<secretos.size()-1; j++) {
					array[i][j] = power(x, secretos.size()-1-j);		
				}
				// Colocar el coeficiente del secreto
				array[i][secretos.size()-1] = 1;

				// Colocar el secreto
				array[i][secretos.size()] = y;
			}	


			// Paso 2: Inicializar la estructura de matriz
			Matriz<Integer> matriz = new Matriz<Integer>(
					secretos.size(), 
					secretos.size()+1, 
					new CampoModuloPrimo(primo)
					);

			// rellenar la matriz con los valores de los secretos
			for (int i = 0; i < matriz.rowCount(); i++) {
				for (int j = 0; j < matriz.columnCount(); j++)
					matriz.set(i, j, array[i][j]);
			}


			// Paso 3: Realizar Gauss-Jordan para hallar el polinomio a partir de los Secret Shares
			// como la matriz se inicializa con un campo primo, entonces Gauss-Jordan se hará con aritmética modular
			matriz.gaussJordanIdentidad();


			// Paso 4: Retornar el resultado
			String rta = "P(x) = ";
			for (int i = 0; i < matriz.rowCount(); i++) {
				
				if(i<matriz.rowCount()-1) {
					rta += matriz.get(i, matriz.columnCount()-1)+"x^"+(matriz.rowCount()-1-i) + " + ";
				} else {
					rta += matriz.get(i, matriz.columnCount()-1);
				}
			}
			// Mostrar polinomio
			System.out.println(rta);
			
			// Nota aclaratoria
			System.out.println();
			System.out.println("Nota: Todos los coeficientes del polinomio son positivos, esto porque el campo correspondiente se definió con rango [0,p).");
			System.out.println("Sin embargo, el valor positivo es equivalente en módulo al valor negativo mostrado en el documento de la actividad.");


		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
