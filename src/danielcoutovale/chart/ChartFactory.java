package danielcoutovale.chart;

import java.lang.reflect.InvocationTargetException;

public class ChartFactory<Value> {

	// The value class
	private final Class<Value> valueClass;

	/**
	 * Constructor
	 * 
	 * @param valueClass the class of the value
	 */
	public ChartFactory(Class<Value> valueClass) {
		this.valueClass = valueClass;
	}

	/**
	 * Loads a string as a chart.
	 * 
	 * @param string the string representing the chart
	 */
	public final Chart<Value> loadChart(String string) {
		Chart<Value> chart = new Chart<Value>(valueClass);
		for (String line : string.split("\n")) {
			String[] tokens = line.split("\t");
			if (tokens.length != 3) {
				System.err.println("Skip: '" + line + "'.");
				continue;
			}
			try {
				Integer beginIndex = Integer.parseInt(tokens[0]);
				Integer endIndex = Integer.parseInt(tokens[1]);
				Value value = valueClass.getConstructor(String.class).newInstance(tokens[2]);
				chart.add(beginIndex, endIndex, value);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.err.println("Skip: '" + line + "'.");
				continue;
			}
		}
		return chart;
	}

}
