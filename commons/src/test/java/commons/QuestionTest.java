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
package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionTest {
    Activity a = new Activity("Activity1", 100, "/path/to/img");
    Activity b = new Activity("Activity2", 200, "/path/to/img");
    Activity c = new Activity("Activity3", 300, "/path/to/img");

    Question q = new Question(c, Arrays.asList(a, b, c), "highest");

    @Test
    public void constructorTest() {
        assertNotNull(q);
    }

    @Test
    public void setChoicesTest()
    {
        Question q = new Question();
        List<Activity> ls = new ArrayList<>();
        ls.add(a);
        ls.add(c);
        q.setCHOICES(ls);
        assertTrue(q.getActivities().equals(ls));
    }

    @Test
    public void setTypeTest()
    {
        Question q = new Question();
        q.setTYPE("Equals");
        assertTrue(q.getType().equals("Equals"));
    }

    @Test
    public void setCorrectTest()
    {
        Question q = new Question();
        q.setCORRECT(a);
        assertTrue(q.getCorrect().equals(a));
    }

    @Test
    public void getterTest() {
        assertEquals("highest", q.getType());
        assertEquals(c, q.getCorrect());
        assertEquals(Arrays.asList(c, b, a), q.getActivities());
    }

    @Test
    public void correctTest() {
        assertTrue(q.isCorrect(c));
    }

    @Test
    public void notCorrectTest() {
        assertFalse(q.isCorrect(b));
    }

    @Test
    public void compareTest() {
        Activity b = new Activity("Activity", 200, "/path/to/img");
        assertEquals(1, a.compareTo(b));
        assertEquals(-1, b.compareTo(a));
        assertEquals(0, a.compareTo(a));
    }

    @Test
    public void sortTest() {
        assertNotEquals(q.getActivities(), Arrays.asList(a, b, c));
    }

    @Test
    public void toStringTest() {
        assertEquals("Question type: highest\nCorrect answer:\n" + c.toString()
                + "\nOptions:\n" + c.toString() + "\n" + b.toString() + "\n"
                + a.toString() + "\n", q.toString());
    }
}
