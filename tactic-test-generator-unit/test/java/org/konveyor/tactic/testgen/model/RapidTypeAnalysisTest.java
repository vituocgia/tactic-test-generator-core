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

package pid.fsoft.tactic.testgen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;

import org.junit.Test;
import pid.fsoft.tactic.testgen.util.TacticTestJson;
import pid.fsoft.tactic.testgen.util.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

public class RapidTypeAnalysisTest {

	@Test
	public void testRapidTypeAnalysis() throws Exception {

		Set<String> result = new RapidTypeAnalysis().
				performAnalysis("test/data/daytrader7/monolith/bin",
						Utils.getClasspathEntries(new File("test/data/daytrader7/daytrader7MonoClasspath.txt")));

		// read standard types and make sure they are the same as what we got

		JsonNode standardNode = TacticTestJson.getObjectMapper().readTree(new File("test/data/daytrader7/DayTrader_RTA_output.json"));

		Set<String> standard = TacticTestJson.getObjectMapper().convertValue(standardNode, new TypeReference<Set<String>>() {});

		assertEquals(standard.size(), result.size());
		assertTrue(result.equals(standard));
	}
}
