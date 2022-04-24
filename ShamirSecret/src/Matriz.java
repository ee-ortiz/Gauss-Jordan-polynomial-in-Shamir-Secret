/**
 * Clase para representar una matriz, 
 * en ella se encuentran todas las operaciones básicas sobre filas para poder realizar Gauss Jordan
 * cabe aclarar que las operaciones se realizan sobre un campo especifico, en este caso sobre un campo primo
 * entonces se resolverá Gauss Jordan modulo f. Donde f es el campo primo.
 */
public final class Matriz<E>{

	/*---- Atributos ----*/

	// Valores de la matriz
	private Integer[][] values;

	// Campo  para operar con los valores de la matriz.
	private final CampoModuloPrimo f;


	/*---- Constructores ----*/

	/**
	 * Construye una matriz en blanco con el número especificado de filas y columnas,
	 * con operaciones del campo especificado. Todos los elementos son inicialmente son nulos.
	 */
	public Matriz(int rows, int cols, CampoModuloPrimo f) {
		this.f = f;
		if (rows <= 0 || cols <= 0)
			throw new IllegalArgumentException("Número de filas o columnas inválido");
		values = new Integer[rows][cols];
	}


	/*---- Métodos Básicos ----*/

	/**
	 * Devuelve el número de filas de esta matriz, que es positivo.
	 */
	public int rowCount() {
		return values.length;
	}


	/**
	 * Devuelve el número de columnas de esta matriz, que es positivo.
	 */
	public int columnCount() {
		return values[0].length;
	}


	/**
	 * Devuelve el elemento en la ubicación especificada en esta matriz.
	 */
	public int get(int row, int col) {
		return values[row][col];
	}


	/**
	 * Almacena el elemento especificado en la ubicación especificada en esta matriz.
	 */
	public void set(int row, int col, int val) {
		values[row][col] = val;
	}


	/*---- Operaciones simples de filas de matrices ----*/

	/**
	 * Intercambia las dos filas especificadas de esta matriz. Si los dos índices de las filas son iguales, el intercambio es un no-op.
	 * los valores de la matriz pueden ser nulos.
	 */
	public void swapRows(int row0, int row1) {
		Integer[] temp = values[row0];
		values[row0] = values[row1];
		values[row1] = temp;
	}


	/**
	 * Multiplica la fila especificada en esta matriz por el factor especificado. En otras palabras, fila *= factor.
	 * Los elementos de la fila especificada deben ser todos no{@code null} al realizar esta operación.
	 */
	public void multiplyRow(int row, int factor) {
		if (row < 0 || row >= values.length)
			throw new IndexOutOfBoundsException("Row index out of bounds");
		for (int j = 0, cols = columnCount(); j < cols; j++)
			set(row, j, f.multiply(get(row, j), factor));
	}


	/**
	 * Añade la primera fila especificada de esta matriz multiplicada por el factor especificado a la segunda fila especificada.
	 * En otras palabras, destRow += srcRow * factor. Los elementos de las dos filas especificadas
	 * no pueden ser nulos.
	 */
	public void addRows(int srcRow, int destRow, int factor) {
		if (srcRow < 0 || srcRow >= values.length || destRow < 0 || destRow >= values.length)
			throw new IndexOutOfBoundsException("Row index out of bounds");
		for (int j = 0, cols = columnCount(); j < cols; j++)
			set(destRow, j, f.add(get(destRow, j), f.multiply(get(srcRow, j), factor)));
	}


	/**
	 * Devuelve una nueva matriz que representa esta matriz multiplicada por la matriz especificada. 
	 * Requiere que la matriz especificada tenga el mismo número de filas que el número de columnas de esta matriz.
	 */
	public Matriz<E> multiply(Matriz<E> other) {
		if (columnCount() != other.rowCount()) {
			throw new IllegalArgumentException("Tamaños de matriz incompatibles para la multiplicación");
		}
		int rows = rowCount();
		int cols = other.columnCount();
		int cells = columnCount();
		Matriz<E> result = new Matriz<>(rows, cols, f);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int sum = f.zero();
				for (int k = 0; k < cells; k++) {
					sum = f.add(f.multiply(get(i, k), other.get(k, j)), sum);
				}
				result.set(i, j, sum);
			}
		}
		return result;
	}


	/*---- Gauss Jordan modulo f ----*/

	/**
	 * Convierte esta matriz a forma escalonada por filas reducida utilizando la eliminación de Gauss-Jordan.
	 */
	public void gaussJordanIdentidad() {
		int rows = rowCount();
		int cols = columnCount();

		int numPivotes = 0;
		for (int j = 0; j < cols && numPivotes < rows; j++) {  // For each column
			// Encontrar la fila pivote
			int pivotRow = numPivotes;
			// Mientras el pivote sea menor que el número de filas y
			// el valor actual del recorrido en la matriz sea igual a 0
			while (pivotRow < rows && f.equals(get(pivotRow, j), f.zero())) {
				pivotRow++;
			}
			swapRows(numPivotes, pivotRow);
			pivotRow = numPivotes;
			numPivotes++;

			// Multiplicar la fila por el valor reciproco del valor actual 
			multiplyRow(pivotRow, f.inversoMultiplicativo(get(pivotRow, j)));

			// Actualizar el valor de la fila pivote
			for (int i = pivotRow + 1; i < rows; i++) {
				addRows(pivotRow, i, f.negate(get(i, j)));
			}
		}

		// Llegar a la matriz identidad
		for (int i = numPivotes - 1; i >= 0; i--) {
			// Encontrar columna pivote
			int pivotCol = 0;
			while (pivotCol < cols && f.equals(get(i, pivotCol), f.zero())) {
				pivotCol++;
			}

			// Actualizar el valor de la fila actual a través de la columna pivote
			for (int j = i - 1; j >= 0; j--) {
				addRows(i, j, f.negate(get(j, pivotCol)));
			}
		}
	}

}
