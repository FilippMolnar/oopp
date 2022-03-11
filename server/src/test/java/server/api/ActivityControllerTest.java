/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Activity;

public class ActivityControllerTest {

    private TestActivityRepository repo;
    private ActivityController sut;
	Activity a = new Activity("Activity", 100, "/path/to/img");

    @BeforeEach
    public void setup() {
        repo = new TestActivityRepository();
        sut = new ActivityController(repo);
    }

    @Test
    public void cannotAddNull() {
        assertFalse(sut.addActivity(null));
    }

    @Test
    public void getRandomTest() {
        sut.addActivity(a);
        var actual = sut.getRandom();
        assertEquals(a, actual);
    }

    @Test
    public void databaseIsUsed() {
        sut.addActivity(a);
        assertTrue(repo.calledMethods.contains("save"));
    }
}
