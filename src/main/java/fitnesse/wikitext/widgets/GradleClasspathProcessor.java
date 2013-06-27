package fitnesse.wikitext.widgets;

import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ModelBuilder;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.idea.*;

import java.io.File;

public class GradleClasspathProcessor
{
	public static final String BUILD_FILE_FLAG = "--build-file";
	public static final String UNRESOLVED_DEPENDENCY_PREFIX = "unresolved dependency";

	public Dependencies getDependencies(File source, String scope)
	{
		Dependencies dependencies = new Dependencies();

		GradleConnector connector = GradleConnector.newConnector();

		connector.forProjectDirectory(source.getParentFile());

		ProjectConnection connection = connector.connect();

		ModelBuilder<IdeaProject> builder = connection.model(IdeaProject.class);

		builder.withArguments(BUILD_FILE_FLAG, source.getName());

		try
		{
			IdeaProject project = builder.get();

			for (IdeaModule module : project.getModules())
			{
				for (IdeaDependency dependency : module.getDependencies())
				{
					if (dependency instanceof IdeaSingleEntryLibraryDependency)
					{
						IdeaSingleEntryLibraryDependency ideaDependency = (IdeaSingleEntryLibraryDependency)dependency;
						IdeaDependencyScope ideaDependencyScope = ideaDependency.getScope();
						if (ideaDependencyScope.getScope().equalsIgnoreCase(scope))
						{
							String name = ideaDependency.getFile().getName();
							if (name.startsWith(UNRESOLVED_DEPENDENCY_PREFIX))
							{
								dependencies.addUnresolvedDependency(name);
							}
							else
							{
							 	dependencies.addResolvedDependency(ideaDependency.getFile().getAbsolutePath());
							}
						}
					}
				}
			}
		}
		finally
		{
			connection.close();
		}

		return dependencies;
	}
}