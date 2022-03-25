package client;/*
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

import client.jokers.Joker;
import client.jokers.JokersList;
import org.junit.jupiter.api.Test;

import client.utils.ServerUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JokerListTest {

    @Test
    public void equalsHashCode() {
        var a = new Joker("a", "b",null);
        var b = new Joker("a", "b",null);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var a = new Joker("a", "b",null);
        var b = new Joker("a", "c",null);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }


}