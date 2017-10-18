package fr.univartois.sonargo.toolkit;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PathToolkitTest {


    @Test
    public void testIsExcludedPath() {
        String excludedPattern = "**/vendor/**,**/test/**/*,node_modules/*.js";

        assertTrue(PathToolkit.isExcludedPath("project/package/vendor/test.go", excludedPattern));
        assertFalse(PathToolkit.isExcludedPath("project/node_modules/test.js", excludedPattern));
        assertFalse(PathToolkit.isExcludedPath("project/somepackage/file.go", excludedPattern));
        assertTrue(PathToolkit.isExcludedPath("project/test/file.go", excludedPattern));

        assertTrue(PathToolkit.isExcludedPath("/Users/maxzollneritsch/projekte/adma/groupengine/vendor/golang.org/x/net/proxy/proxy_test.go", excludedPattern));

    }
}
