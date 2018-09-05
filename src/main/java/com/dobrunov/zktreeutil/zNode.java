/**
 * Copyright 2014 Aleksey Dobrunov
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dobrunov.zktreeutil;

import org.apache.zookeeper.data.Stat;

import java.util.Arrays;
import java.util.Objects;

public class zNode implements Comparable<zNode>, TreeNodeItem {
    public String name;
    public String path;
    public byte[] data = null;
    public Stat stat;
    public boolean has_children;
    private int level;

    public zNode(String name, String path, byte[] data) {
        this(name, path, data, new Stat(), true);
    }

    public zNode(String name, String path, byte[] data, Stat stat, boolean has_children) {
        this.name = name;
        this.path = path;
        if (data != null) {
            this.data = data.clone();
        }
        this.stat = stat;
        this.has_children = has_children;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(zNode o) {
        return o.equals(this)?0:
            (level<o.level?-1:1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof zNode)) {
            return false;
        }
        zNode zNode = (zNode) o;
        return has_children == zNode.has_children &&
            Objects.equals(name, zNode.name) &&
            Objects.equals(path, zNode.path) &&
            Arrays.equals(data, zNode.data) &&
            Objects.equals(stat, zNode.stat) &&
            Objects.equals(level, zNode.level);

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, path, stat, has_children, level);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "zkNode{"+path+" "+name+"=["+(data==null?"NULL":data.length)+"]}";
    }
}
