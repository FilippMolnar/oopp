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

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import client.controllers.MainAppController;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import client.utils.ServerUtils;

public class Joker {
    public String name;
    public String imagePath;
    private boolean used;
    protected ServerUtils serverUtils;
    @SuppressWarnings("unused")
    private Joker() {
        // for object mapper
    }

    public Joker(String name, String imagePath, ServerUtils serverUtils) {
        this.name = name;
        this.imagePath = imagePath;
        this.used = false;
        this.serverUtils = serverUtils;
    }

    public boolean isUsed(){
        return this.used;
    }

    public void use(){
        this.used = true;
    }

    public void onClick(MainAppController mainCtrl){
        System.out.println("joker");
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