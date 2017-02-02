package alltests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Maven only runs test classes ending in 'Test'
 * 
 * @author Simon Baird
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ AllTests.class })
public class MavenRunTest {

}
