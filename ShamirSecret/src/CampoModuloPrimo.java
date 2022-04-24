import java.util.Objects;

/**
 * Campo definido para realizar operaciones sobre matrices,
 * el valor p del campo es un n�mero primo y 
 * todos sus elementos tienen rango [0, p)
 */
public final class CampoModuloPrimo {

	/*---- Atributos ----*/

	/**
	 * El m�dulo de este campo. Debe ser primo.
	 */
	public final int modulo;



	/*---- Constructor ----*/

	/**
	 * Construye un campo primo con el m�dulo especificado. El m�dulo debe ser un
	 * n�mero primo, pero el constructor no verifica esta propiedad cr�tica.
	 */
	public CampoModuloPrimo(int mod) {
		modulo = mod;
	}


	/*---- M�todos ----*/

	public Integer zero() {
		return 0;
	}

	public Integer one() {
		return 1;
	}


	public boolean equals(Integer x, Integer y) {
		return check(x) == check(y);
	}


	/*---- Operaciones matem�ticas definidas para culaquier campo ----*/


	public Integer negate(Integer x) {
		int rta = (modulo - check(x));
		rta = rta% modulo;
		return rta;
	}

	public Integer add(Integer x, Integer y) {
		long rta = ((long)check(x) + check(y));
		rta = rta % modulo;
		return (int) rta;
	}

	public Integer subtract(Integer x, Integer y) {
		long rta = ((long)check(x) + modulo - check(y));
		rta = rta % modulo;
		return (int) rta;
	}

	public Integer multiply(Integer x, Integer y) {
		long rta = (long)check(x) * check(y);
		rta = rta % modulo;
		return (int) rta;
	}

	public Integer inversoMultiplicativo(Integer w) {
		// Algoritmo de m�ximo com�n divisor extendido
		// para encontrar el inverso multiplicativo
		// seg�n el teorema chino del residuo
		int x = modulo;
		int y = check(w);
		if (y == 0) {
			throw new ArithmeticException("Division by zero");
		}
		int a = 0;
		int b = 1;
		while (y != 0) {
			int z = x % y;
			int c = a - x / y * b;
			x = y;
			y = z;
			a = b;
			b = c;
		}
		if (x == 1) {
			return (int)(((long)a + modulo) % modulo);
		}
		else {
			throw new IllegalStateException("El m�dulo no es primo");
		}
	}


	/*---- Funci�n auxiliar ----*/

	// Comprueba si el objeto dado no es nulo y est� dentro del rango
	// de valores v�lidos y devuelve el valor.
	private int check(Integer x) {
		Objects.requireNonNull(x);
		int y = x.intValue();
		if (y < 0 || y >= modulo) {
			throw new IllegalArgumentException("No puede ser un elemento que pertenezca a este campo: " + y);
		}
		return y;
	}

}
