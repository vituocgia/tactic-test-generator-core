/*
 * Copyright IBM Corporation 2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pid.fsoft.tactictest.ui.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TacticTestLogger {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$-7s] [%2$s] %5$s %n");
    }

    private static Level DEFAULT_LOG_LEVEL = Level.INFO;

    public static Logger getLogger(Class<?> cls) {
        Logger logger = Logger.getLogger(cls.getSimpleName());
        Handler handler = new ConsoleHandler();
        logger.addHandler(handler);
        logger.setLevel(DEFAULT_LOG_LEVEL);
        logger.setUseParentHandlers(false);
        return logger;
    }

}
