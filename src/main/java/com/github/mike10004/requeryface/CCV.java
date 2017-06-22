/*
Copyright (c) 2010, Liu Liu
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the authors nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*/
package com.github.mike10004.requeryface;

import java.util.List;
import java.util.function.BiFunction;

class CCV {

    public static Canvas grayscale(Canvas input) {
        throw new UnsupportedOperationException();
    }

    private static int tilde(int n) {
        return -(n + 1);
    }

    public static ArrayGroup array_group(List<Detection> seqList, BiFunction<Detection, Detection, Boolean> gfunc) {
        Detection[] seq = seqList.toArray(new Detection[0]);
        Node[] node = new Node[seq.length];
        for (int i = 0; i < seq.length; i++) {
            node[i] = new Node(seq[i]);
        }
        for (int i = 0; i < seq.length; i++) {
//                if (!node[i].element)
            if (node[i].element == null) {
                continue;
            }
            int root = i;
            while (node[root].parent != -1) {
                root = node[root].parent;
            }
            for (int j = 0; j < seq.length; j++) {
                if( i != j && node[j].isElementTruthy() && gfunc.apply(node[i].element, node[j].element)) {
                    int root2 = j;

                    while (node[root2].parent != -1)
                        root2 = node[root2].parent;

                    if(root2 != root) {
                        if(node[root].rank > node[root2].rank)
                            node[root2].parent = root;
                        else {
                            node[root].parent = root2;
                            if (node[root].rank == node[root2].rank)
                                node[root2].rank++;
                            root = root2;
                        }

                    /* compress path from node2 to the root: */
                        int temp, node2 = j;
                        while (node[node2].parent != -1) {
                            temp = node2;
                            node2 = node[node2].parent;
                            node[temp].parent = root;
                        }

                    /* compress path from node to the root: */
                        node2 = i;
                        while (node[node2].parent != -1) {
                            temp = node2;
                            node2 = node[node2].parent;
                            node[temp].parent = root;
                        }
                    }
                }
            }
        }
        int[] idx = new int[seq.length];
        int class_idx = 0;
        int j;
        for(int i = 0; i < seq.length; i++) {
            j = -1;
            int node1 = i;
            if(node[node1].isElementTruthy()) {
                while (node[node1].parent != -1)
                    node1 = node[node1].parent;
                if(node[node1].rank >= 0)
                    node[node1].rank = ~class_idx++;
                j = tilde(node[node1].rank);
            }
            idx[i] = j;
        }
        return new ArrayGroup(idx, class_idx);
    }


}
