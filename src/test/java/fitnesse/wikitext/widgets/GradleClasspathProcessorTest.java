package fitnesse.wikitext.widgets;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GradleClasspathProcessorTest
{
	private GradleClasspathProcessor processor;

	@Before
	public void before()
	{
		processor = new GradleClasspathProcessor();
	}

	@Test
	public void testGetClasspathEntriesWithInvalidGradle()
	{
		File source = getResource("invalid.gradle");
		List<String> result = processor.getClasspathEntries(source, null);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testGetClasspathEntriesWithLibrariesInScope()
	{
		File source = getResource("scope.gradle");
		List<String> result = processor.getClasspathEntries(source, "compile");
		assertEquals(2, result.size());
	}

	@Test
	public void testGetClasspathEntriesWithNoLibrariesInScope()
	{
		File source = getResource("scope.gradle");
		List<String> result = processor.getClasspathEntries(source, "runtime");
		assertTrue(result.isEmpty());
	}

	@SuppressWarnings("ConstantConditions")
	private File getResource(String name)
	{
		return new File(this.getClass().getClassLoader().getResource(name).getPath());
	}
}