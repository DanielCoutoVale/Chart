package danielcoutovale.chart;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A chart for storing candidate values for tokens, i.e. text segments.
 * 
 * @author Daniel Couto-Vale
 *
 * @param <Value> the class of values
 */
public class Chart<Value> {

	// The sparse table
	private final Map<Integer, Map<Integer, Object>> table = new LinkedHashMap<>();

	// The value class
	private final Class<Value> valueClass;

	// An empty set
	private final Set<Value> emptySet = new HashSet<>();

	/**
	 * Constructor
	 * 
	 * @param valueClass the class of the value
	 */
	public Chart(Class<Value> valueClass) {
		this.valueClass = valueClass;
	}

	/**
	 * Adds a candidate value to the specified token.
	 * 
	 * @param beginIndex the begin index of the token
	 * @param endIndex   the end index of the token
	 * @param value      the value of the token
	 */
	@SuppressWarnings("unchecked")
	public final void add(int beginIndex, int endIndex, Value value) {
		if (endIndex <= beginIndex) {
			throw new StringIndexOutOfBoundsException();
		}
		Map<Integer, Object> row = this.table.get(beginIndex);
		if (row == null) {
			row = new LinkedHashMap<>();
			this.table.put(beginIndex, row);
		}
		Object object = row.get(endIndex);
		if (object == null) {
			row.put(endIndex, value);
		} else if (valueClass.isInstance(object)) {
			Set<Value> values = new java.util.LinkedHashSet<Value>();
			values.add((Value) object);
			values.add(value);
		}
	}

	/**
	 * Removes a candidate value from the specified token.
	 * 
	 * @param beginIndex the begin index of the token
	 * @param endIndex   the end index of the token
	 * @param value      the value of the token
	 * @return whether the value was removed
	 */
	public final boolean remove(int beginIndex, int endIndex, Value value) {

		// Get the row
		Map<Integer, Object> row = this.table.get(beginIndex);
		if (row == null)
			return false;

		// Get the cell object
		Object object = row.get(endIndex);
		if (object == null)
			return false;

		// If the object is a value
		if (valueClass.isInstance(object)) {
			if (object == value) {
				row.remove(endIndex);
				return true;
			} else {
				return false;
			}
		}

		// If the object is a set of values
		@SuppressWarnings("unchecked")
		Set<Value> values = (Set<Value>) object;
		if (values.remove(value)) {
			if (values.size() == 1) {
				row.put(endIndex, values.iterator().next());
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets all candidate values for the specified token.
	 * 
	 * @param beginIndex the begin index of the token
	 * @param endIndex   the end index of the token
	 * @return the candidate values of the token
	 */
	@SuppressWarnings("unchecked")
	public final Set<Value> get(int beginIndex, int endIndex) {

		// Get the row
		Map<Integer, Object> row = this.table.get(beginIndex);
		if (row == null)
			return emptySet;

		// Get the cell object
		Object object = row.get(endIndex);
		if (object == null)
			return emptySet;

		// If the object is a value
		if (valueClass.isInstance(object)) {
			Set<Value> values = new HashSet<Value>();
			values.add((Value) object);
			return values;
		}

		// If the object is a set of values
		return (Set<Value>) object;
	}

	/**
	 * Represents the chart as a string.
	 */
	public final String toString() {
		StringBuffer buffer = new StringBuffer();
		for (Integer beginIndex : this.table.keySet()) {
			Map<Integer, Object> row = this.table.get(beginIndex);
			for (Integer endIndex : row.keySet()) {
				Object object = row.get(endIndex);
				if (valueClass.isInstance(object)) {
					if (buffer.length() > 0)
						buffer.append("\n");
					buffer.append(beginIndex + "\t" + endIndex + "\t" + object);
				} else if (object instanceof Set) {
					for (Object item : (Set<?>) object) {
						if (buffer.length() > 0)
							buffer.append("\n");
						buffer.append(beginIndex + "\t" + endIndex + "\t" + item);
					}
				}
			}
		}
		return buffer.toString();
	}

}
