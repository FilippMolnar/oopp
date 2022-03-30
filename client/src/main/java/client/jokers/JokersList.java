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
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


public class JokersList {
    private List<Joker> jokers;

    public JokersList(ServerUtils serverUtils, boolean isMultiplayer) {
        if(!isMultiplayer){
            generateSP(serverUtils);
            return;
        }

        List<Joker>j = new ArrayList<>();
        this.jokers = new ArrayList<>();

        Joker doublePoints = new DoublePointsJoker("double points", "joker_double_points.png", serverUtils);
        Joker coverInk = new CoverInkJoker("cover ink", "joker_double_points.png", serverUtils);
        Joker coverHands = new CoverHandsJoker("cover hands", "joker_double_points.png", serverUtils);
        Joker decreaseTime = new DecreaseTimeJoker("decrease time", "joker_decrease_time.png", serverUtils);
        Joker google = new GoogleJoker("google", "joker_google.png", serverUtils);
        Joker elimWrong = new ElimWrongJoker("eliminate wrong answer", "joker_elim_wrong.png", serverUtils);
        j.add(doublePoints);
        j.add(coverInk);
        j.add(coverHands);
        j.add(decreaseTime);
        j.add(google);
        j.add(elimWrong);
        Collections.shuffle(j);

        for(int i=0; i<3; i++){
            this.jokers.add(j.get(i));
        }

    }

    public void generateSP(ServerUtils serverUtils){
        Joker doublePoints = new DoublePointsJoker("double points", "joker_double_points.png", serverUtils);
        Joker google = new GoogleJoker("google", "joker_google.png", serverUtils);
        Joker elimWrong = new ElimWrongJoker("eliminate wrong answer", "joker_elim_wrong.png", serverUtils);
        List<Joker>j = new ArrayList<>();
        j.add(doublePoints);
        j.add(google);
        j.add(elimWrong);
        this.jokers = j;
    }

    public void replaceUsed(ServerUtils serverUtils, boolean isMultiplayer){
        if(!isMultiplayer){
            generateSP(serverUtils);
            return;
        }

        List<Joker>j = new ArrayList<>();
        List<Joker>notUsed = new ArrayList<>();

        Joker doublePoints = new DoublePointsJoker("double points", "joker_double_points.png", serverUtils);
        Joker coverInk = new CoverInkJoker("cover ink", "joker_double_points.png", serverUtils);
        Joker coverHands = new CoverHandsJoker("cover hands", "joker_double_points.png", serverUtils);
        Joker decreaseTime = new DecreaseTimeJoker("decrease time", "joker_decrease_time.png", serverUtils);
        Joker google = new GoogleJoker("google", "joker_google.png", serverUtils);
        Joker elimWrong = new ElimWrongJoker("eliminate wrong answer", "joker_elim_wrong.png", serverUtils);
        j.add(doublePoints);
        j.add(coverInk);
        j.add(coverHands);
        j.add(decreaseTime);
        j.add(google);
        j.add(elimWrong);

        for(Joker joker : j){
            boolean flag = true;
            for (Joker value : this.jokers) {
                if (joker.getClass() == value.getClass()) {
                    flag = false;
                    break;
                }
            }
            if(flag){
                notUsed.add(joker);
            }
        }

        Collections.shuffle(notUsed);
        for(int i=0; i<this.jokers.size(); i++){
            if(this.jokers.get(i).isUsed()){
                this.jokers.set(i, notUsed.get(i));
            }
        }

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