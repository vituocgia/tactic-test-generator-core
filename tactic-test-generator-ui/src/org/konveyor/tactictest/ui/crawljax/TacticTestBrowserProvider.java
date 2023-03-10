package pid.fsoft.tactictest.ui.crawljax;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.browser.WebDriverBackedEmbeddedBrowser;
import com.google.inject.Inject;
import com.google.inject.Provider;
import pid.fsoft.tactictest.ui.util.DriverProvider;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Browser provider for TacticTest: creates web driver instance, sets browser configuration
 * options, and returns an embedded browser
 */
public class TacticTestBrowserProvider implements Provider<EmbeddedBrowser> {

    private final EmbeddedBrowser.BrowserType browserType;

    @Inject
    public TacticTestBrowserProvider(EmbeddedBrowser.BrowserType browserType) {
        this.browserType = browserType;
    }

    @Override
    public EmbeddedBrowser get() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--ignore-certificate-errors");
        if (browserType == EmbeddedBrowser.BrowserType.CHROME_HEADLESS) {
            chromeOptions.addArguments("--headless");
        }
        WebDriver chromeDriver = DriverProvider.getInstance().getDriver(chromeOptions);
        chromeDriver.manage().window().setSize(new Dimension(1200, 890));
        return WebDriverBackedEmbeddedBrowser.withDriver(chromeDriver);
    }
}
