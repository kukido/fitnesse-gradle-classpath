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
	public void testGetDependenciesWithInvalidGradle()
	{
		File source = getResource("invalid.gradle");
		Dependencies result = processor.getDependencies(source, null);
		assertTrue(result.getResolvedDependencies().isEmpty());
		assertTrue(result.getUnresolvedDependencies().isEmpty());
	}

	@Test
	public void testGetDependenciesWithLibrariesInScope()
	{
		File source = getResource("scope.gradle");
		List<String> result = processor.getDependencies(source, "compile").getResolvedDependencies();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetDependenciesWithUnresolvedDependency()
	{
		File source = getResource("unresolved.gradle");
		Dependencies result = processor.getDependencies(source, "test");
		assertEquals(1, result.getUnresolvedDependencies().size());
		assertEquals(1, result.getResolvedDependencies().size());
	}

	@Test
	public void testGetDependenciesWithNoLibrariesInScope()
	{
		File source = getResource("scope.gradle");
		List<String> result = processor.getDependencies(source, "runtime").getResolvedDependencies();
		assertTrue(result.isEmpty());
	}

	@SuppressWarnings("ConstantConditions")
	private File getResource(String name)
	{
		return new File(this.getClass().getClassLoader().getResource(name).getPath());
	}
}