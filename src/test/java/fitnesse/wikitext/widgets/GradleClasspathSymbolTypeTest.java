package fitnesse.wikitext.widgets;

import fitnesse.wikitext.parser.Path;
import fitnesse.wikitext.parser.SymbolType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GradleClasspathSymbolTypeTest
{
	private GradleClasspathSymbolType type;

	@Before
	public void before()
	{
		type = new GradleClasspathSymbolType();
	}

	@Test
	public void testMatchesForWithPath()
	{
		SymbolType probe = new Path();
		assertTrue(type.matchesFor(probe));
	}

	@Test
	public void testMatchesForWithNamedSymbol()
	{
		SymbolType probe = new SymbolType(GradleClasspathSymbolType.GRADLE_CLASSPATH_SYMBOL_TYPE_NAME);
		assertTrue(type.matchesFor(probe));
	}
}