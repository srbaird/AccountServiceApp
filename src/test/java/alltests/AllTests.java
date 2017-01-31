package alltests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	com.bac.accountserviceapp.data.AllTests.class,
	com.bac.accountserviceapp.hibernate.AllTests.class
})
public class AllTests {

}
