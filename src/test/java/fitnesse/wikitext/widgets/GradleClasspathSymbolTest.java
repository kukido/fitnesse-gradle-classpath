package fitnesse.wikitext.widgets;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GradleClasspathSymbolTest
{
	@Test
	public void testConstructorWithDefaultScope()
	{
		GradleClasspathSymbol symbol = new GradleClasspathSymbol("");
		assertEquals(GradleClasspathSymbol.DEFAULT_SCOPE, symbol.getScope());
	}

	@Test
	public void testConstructorWithSpecificScope()
	{
		GradleClasspathSymbol symbol = new GradleClasspathSymbol("source@scope");
		assertEquals("scope", symbol.getScope());
	}
}