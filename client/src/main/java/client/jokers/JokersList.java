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
package client.jokers;

import client.utils.ServerUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


public class JokersList {
    private List<Joker> jokers;

    public JokersList(ServerUtils serverUtils) {
        this.jokers = new ArrayList<>();
        //TODO randomly generate jokers, insert them to fxml
        jokers.add(new DoublePointsJoker("double points", "@client/pictures/joker_double_points.png", serverUtils));
        jokers.add(new DecreaseTimeJoker("decrease time", "@client/pictures/joker_decrease_time.png", serverUtils));
        jokers.add(new ElimWrongJoker("eliminate wrong answer", "@client/pictures/joker_elim_wrong.png", serverUtils));
    }

    public List<Joker> getJokers() {
        return jokers;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}