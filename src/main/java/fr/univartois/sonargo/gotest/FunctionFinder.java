package fr.univartois.sonargo.gotest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import fr.univartois.sonargo.coverage.AntPathMatcher;
import fr.univartois.sonargo.toolkit.PathToolkit;
import org.sonar.api.CoreProperties;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class FunctionFinder {
	private static final Logger LOGGER = Loggers.get(FunctionFinder.class);

	private HashMap<String, String> result = new HashMap<>();
	private String baseDir;
	private Stream<Path> paths;

	public FunctionFinder(SensorContext context) throws IOException {
		this.baseDir = context.fileSystem().baseDir().getPath();
		String excludedPattern = context.settings().getString(CoreProperties.PROJECT_EXCLUSIONS_PROPERTY);

		paths = Files.walk(Paths.get(baseDir)).filter(p -> p.toFile().getName().endsWith("_test.go")
				&& !PathToolkit.isExcludedPath(p.toFile().getPath(), excludedPattern));
	}

	public HashMap<String, String> searchFunction() {

		paths.forEach((p) -> {
			LOGGER.info("search test function in " + p.toFile().getAbsolutePath());
			searchFunctionInFile(p);
		});
		LOGGER.debug(result.toString());
		return result;
	}

	private void searchFunctionInFile(Path p) {

		try (Stream<String> stream = Files.lines(p)) {

			stream.forEach((s) -> {
				String nameFunction = searchInLine(s);
				if (nameFunction == null)
					return;
				String key = p.getFileName() + "#" + nameFunction;
				LOGGER.info("Create the key " + key);
				result.put(key, p.toFile().getAbsolutePath());
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String searchInLine(String s) {
		int indexTesting = s.indexOf("Test");
		int indexBrace = s.indexOf('{');

		if (indexTesting == -1) {
			LOGGER.debug("This function name is not correct: " + s);
			return null;
		}
		int indexParen = s.indexOf("(", indexTesting);
		if (indexParen == -1) {
			LOGGER.debug("This function name is not correct: " + s);
			return null;
		}
		int indexCloseParen = s.indexOf(")");
		if (indexCloseParen < indexParen) {
			indexTesting = s.indexOf("Test", indexCloseParen);
		}

		return s.substring(indexTesting, indexParen);
	}

}
