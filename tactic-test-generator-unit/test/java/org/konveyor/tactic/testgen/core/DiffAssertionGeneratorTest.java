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

package pid.fsoft.tactic.testgen.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import pid.fsoft.tactic.testgen.TestUtils;
import pid.fsoft.tactic.testgen.util.Constants;
import pid.fsoft.tactic.testgen.util.TacticTestJson;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class DiffAssertionGeneratorTest {

	private final File outputFile = new File("DayTrader_"+ Constants.DIFF_ASSERTIONS_OUTFILE_SUFFIX);


	@Before
	/**
	 * Delete existing output files
	 */
	public void cleanUp() {

		FileUtils.deleteQuietly(outputFile);
	}

	@Test
	public void testDiffAssertionGenerator() throws Exception {

		TestUtils.launchProcess(DiffAssertionsGenerator.class.getSimpleName(),
            "DayTrader",
            "test/data/daytrader7/monolith/bin",
            "test/data/daytrader7/daytrader7MonoClasspath.txt",
            "test/data/daytrader7/DayTrader_extended_sequences.json",
            null,
            null,
            null,
            null,
            "test/data/daytrader7/DayTrader_extended_sequences_results.json");

		assertTrue(outputFile.exists());

		ObjectNode mainObject = (ObjectNode) TacticTestJson.getObjectMapper().readTree(outputFile);
		ObjectNode standardObject = (ObjectNode) TacticTestJson.getObjectMapper().readTree(new File("test/data/daytrader7/DayTrader_extended_sequences_with_assertions.json"));


		Set<String> seqKeys = StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(mainObject.fieldNames(),
						Spliterator.ORDERED), false)
		  .collect(Collectors.toSet());

		Set<String> seqStandardKeys = StreamSupport.stream(
				Spliterators.spliteratorUnknownSize(standardObject.fieldNames(),
						Spliterator.ORDERED), false)
		  .collect(Collectors.toSet());

		assertEquals(seqStandardKeys, seqKeys);

		mainObject.fieldNames().forEachRemaining(key -> {
			ObjectNode currentObject = (ObjectNode) mainObject.get(key);
			ObjectNode currentStandardObject = (ObjectNode) standardObject.get(key);
			assertEquals(currentStandardObject.get("class_name").asText(), currentObject.get("class_name").asText());
			assertEquals(currentStandardObject.get("imports"), currentObject.get("imports"));

			String currentSeqCode = currentObject.get("sequence").asText();
			String standardSeqCode = currentStandardObject.get("sequence").asText();

			int assertCount = countMatches(currentSeqCode, "assertEquals(");
			int standardAssertCount = countMatches(standardSeqCode, "assertEquals(");

			assertEquals(standardAssertCount, assertCount);

			assertCount = countMatches(currentSeqCode, "assertNull(");
			standardAssertCount = countMatches(standardSeqCode, "assertNull(");

			assertEquals(standardAssertCount, assertCount);

		});
	}

	private int countMatches(String str, String findStr) {
		int count = 0;
		int lastIndex = 0;

		while (lastIndex != -1) {

		    lastIndex = str.indexOf(findStr, lastIndex);

		    if (lastIndex != -1) {
		        count++;
		        lastIndex += findStr.length();
		    }
		}
		return count;
	}

}
