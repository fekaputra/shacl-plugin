package at.ac.tuwien.ame.shacl.syntax.parsing;

import static org.junit.Assert.fail;

import org.junit.Test;

public class TestQuery1 {
	
	@Test
	public void testTurtleParser() {
		TurtleParser parser = new TurtleParser();
		try {
			parser.parse();
		} catch (ParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}
