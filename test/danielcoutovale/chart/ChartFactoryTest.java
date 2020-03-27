package danielcoutovale.chart;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ChartFactoryTest {

	public final static class Word {
		private String item;
		private String[] features;

		public Word(String string) {
			String[] tokens = string.split(":");
			this.item = tokens[0];
			this.features = Arrays.copyOfRange(tokens, 1, tokens.length);
		}

		public String toString() {
			return item + ":" + String.join(":", features);
		}
	}

	private final static void addWord(String form, String word) {
		List<Word> words = formToWords.get(form);
		if (words == null) {
			words = new LinkedList<>();
			formToWords.put(form, words);
		}
		words.add(new Word(word));
	}

	private final static Map<String, List<Word>> formToWords = new HashMap<>();

	static {
		addWord("Belgae", "Belgae:nominative");
		addWord("Belgas", "Belgae:accusative");
		addWord("Belgarum", "Belgae:genitive");
		addWord("Belgis", "Belgae:dative");
		addWord("Belgis", "Belgae:ablative");
		addWord("Celtae", "Celtae:nominative");
		addWord("Celtas", "Celtae:accusative");
		addWord("Celtarum", "Celtae:genitive");
		addWord("Celtis", "Celtae:dative");
		addWord("Celtis", "Celtae:ablative");
		addWord("Galli", "Galli:nominative");
		addWord("Gallos", "Galli:accusative");
		addWord("Gallorum", "Galli:genitive");
		addWord("Gallis", "Galli:dative");
		addWord("Gallis", "Galli:ablative");
		addWord("Helvetii", "Helvetii:nominative");
		addWord("Helveti", "Helvetii:nominative");
		addWord("Helvetios", "Helvetii:accusative");
		addWord("Helvetiorum", "Helvetii:genitive");
		addWord("Helvetiis", "Helvetii:dative");
		addWord("Helvetis", "Helvetii:dative");
		addWord("Helvetiis", "Helvetii:ablative");
		addWord("Helvetis", "Helvetii:ablative");
	}

	@Test
	public final void testDeBelloGallicoB1P1() {
		String text = "Gallia est omnis divisa in partes tres, quarum unam incolunt Belgae, aliam Aquitani, tertiam qui ipsorum lingua Celtae, nostra Galli appellantur. Hi omnes lingua, institutis, legibus inter se differunt. Gallos ab Aquitanis Garumna flumen, a Belgis Matrona et Sequana dividit. Horum omnium fortissimi sunt Belgae, propterea quod a cultu atque humanitate provinciae longissime absunt, minimeque ad eos mercatores saepe commeant atque ea quae ad effeminandos animos pertinent important, proximique sunt Germanis, qui trans Rhenum incolunt, quibuscum continenter bellum gerunt. Qua de causa Helvetii quoque reliquos Gallos virtute praecedunt, quod fere cotidianis proeliis cum Germanis contendunt, cum aut suis finibus eos prohibent aut ipsi in eorum finibus bellum gerunt. Eorum una, pars, quam Gallos obtinere dictum est, initium capit a flumine Rhodano, continetur Garumna flumine, Oceano, finibus Belgarum, attingit etiam ab Sequanis et Helvetiis flumen Rhenum, vergit ad septentriones. Belgae ab extremis Galliae finibus oriuntur, pertinent ad inferiorem partem fluminis Rheni, spectant in septentrionem et orientem solem. Aquitania a Garumna flumine ad Pyrenaeos montes et eam partem Oceani quae est ad Hispaniam pertinet; spectat inter occasum solis et septentriones.";

		// Build chart
		Chart<Word> chart = new Chart<Word>(Word.class);
		for (int beginIndex = 0; beginIndex < text.length(); beginIndex++) {
			for (int endIndex = beginIndex + 1; endIndex <= text.length() && endIndex <= beginIndex + 20; endIndex++) {
				String form = text.substring(beginIndex, endIndex);
				List<Word> words = formToWords.get(form);
				if (words == null)
					continue;
				for (Word word : words) {
					chart.add(beginIndex, endIndex, word);
				}
			}
		}
		String[] chartLines = chart.toString().split("\n");

		// Clone chart
		ChartFactory<Word> chartFactory = new ChartFactory<Word>(Word.class);
		Chart<Word> clone = chartFactory.loadChart(chart.toString());
		String[] cloneLines = clone.toString().split("\n");

		// Check for identity in structure
		Assert.assertEquals(chartLines.length, cloneLines.length);
		for (int i = 0; i < chartLines.length; i++) {
			Assert.assertEquals(chartLines[i], cloneLines[i]);
		}
		Assert.assertEquals(chart.toString(), clone.toString());
		
		System.out.println(chart);
	}

}
