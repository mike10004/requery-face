/*
Copyright (c) 2010, Liu Liu
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the authors nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*/
package com.github.mike10004.requeryface;

import com.github.mike10004.requeryface.Cascade.Classifier;
import com.github.mike10004.requeryface.Cascade.Feature;
import com.google.common.collect.ImmutableList;
import com.google.common.math.IntMath;
import com.google.common.primitives.Ints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

public class ObjectDetector {

    private final Canvas<?> canvas;
    private final int interval;
    private final int min_neighbors;
    private final double scale;
    private final int next;
    private final int scale_upto;

    public ObjectDetector(Canvas<?> canvas, Cascade cascade, int interval, int min_neighbors) {
        this(canvas, cascade.width, cascade.height, interval, min_neighbors);
    }

    public ObjectDetector(Canvas<?> canvas, int cascadeWidth, int cascadeHeight, int interval, int min_neighbors) {
        this.canvas = canvas;
        this.interval = interval;
        this.min_neighbors = min_neighbors;
        checkArgument(min_neighbors >= 0, "min_neighbors >= 0 required");
        this.scale = Math.pow(2, 1d / (interval + 1d));
        this.next = IntMath.checkedAdd(interval, 1);
        this.scale_upto = Ints.checkedCast(Math.round(Math.floor(Math.log(Math.min(canvas.width / cascadeWidth, canvas.height / cascadeHeight)) / Math.log(scale))));
        // TODO: use line below where canvas/cascade dimension proportions are computed using floating point arithmetic instead of int arithmetic (truncation)
//        this.scale_upto = Ints.checkedCast(Math.round(Math.floor(Math.log(Math.min((double) canvas.width / (double) cascadeWidth, (double) canvas.height / (double) cascadeHeight)) / Math.log(scale))));
//        cascade.storeFeaturesInClassifiers();
    }

    public Canvas[] prepare() {
        Canvas[] pyr = new Canvas[(scale_upto + next * 2) * 4];
        Canvas[] ret = new Canvas[(scale_upto + next * 2) * 4];
        pyr[0] = canvas;
        ret[0] = newCanvas(pyr[0].width, pyr[0].height, pyr[0].getSubimage(0, 0, pyr[0].width, pyr[0].height));

        for (int i = 1; i <= interval; i++) {
            pyr[i * 4] = newCanvas((int) Math.floor(pyr[0].width / Math.pow(scale, i)), (int) Math.floor(pyr[0].height / Math.pow(scale, i)));
            pyr[i * 4].drawImage(pyr[0], 0, 0, pyr[0].width, pyr[0].height, 0, 0, pyr[i * 4].width, pyr[i * 4].height);
            ret[i * 4] = newCanvas(pyr[i * 4].width, pyr[i * 4].height, pyr[i * 4].getSubimage(0, 0, pyr[i * 4].width, pyr[i * 4].height) );
        }
        for (int i = next; i < scale_upto + next * 2; i++) {
            pyr[i * 4] = newCanvas((int) Math.floor(pyr[i * 4 - next * 4].width / 2), (int) Math.floor(pyr[i * 4 - next * 4].height / 2));
            pyr[i * 4].drawImage(pyr[i * 4 - next * 4], 0, 0, pyr[i * 4 - next * 4].width, pyr[i * 4 - next * 4].height, 0, 0, pyr[i * 4].width, pyr[i * 4].height);
            ret[i * 4] = newCanvas(pyr[i * 4].width, pyr[i * 4].height, pyr[i * 4].getSubimage(0, 0, pyr[i * 4].width, pyr[i * 4].height) );
        }
        for (int i = next * 2; i < scale_upto + next * 2; i++) {
            pyr[i * 4 + 1] = newCanvas((int) Math.floor(pyr[i * 4 - next * 4].width / 2), (int) Math.floor(pyr[i * 4 - next * 4].height / 2));
            pyr[i * 4 + 1].drawImage(pyr[i * 4 - next * 4], 1, 0, pyr[i * 4 - next * 4].width - 1, pyr[i * 4 - next * 4].height, 0, 0, pyr[i * 4 + 1].width - 2, pyr[i * 4 + 1].height);
            ret[i * 4 + 1] = newCanvas(pyr[i * 4 + 1].width, pyr[i * 4 + 1].height, pyr[i * 4 + 1].getSubimage(0, 0, pyr[i * 4 + 1].width, pyr[i * 4 + 1].height) );
            pyr[i * 4 + 2] = newCanvas((int) Math.floor(pyr[i * 4 - next * 4].width / 2), (int) Math.floor(pyr[i * 4 - next * 4].height / 2));
            pyr[i * 4 + 2].drawImage(pyr[i * 4 - next * 4], 0, 1, pyr[i * 4 - next * 4].width, pyr[i * 4 - next * 4].height - 1, 0, 0, pyr[i * 4 + 2].width, pyr[i * 4 + 2].height - 2);
            ret[i * 4 + 2] = newCanvas(pyr[i * 4 + 2].width, pyr[i * 4 + 2].height, pyr[i * 4 + 2].getSubimage(0, 0, pyr[i * 4 + 2].width, pyr[i * 4 + 2].height) );
            pyr[i * 4 + 3] = newCanvas((int) Math.floor(pyr[i * 4 - next * 4].width / 2), (int) Math.floor(pyr[i * 4 - next * 4].height / 2));
            pyr[i * 4 + 3].drawImage(pyr[i * 4 - next * 4], 1, 1, pyr[i * 4 - next * 4].width - 1, pyr[i * 4 - next * 4].height - 1, 0, 0, pyr[i * 4 + 3].width - 2, pyr[i * 4 + 3].height - 2);
            ret[i * 4 + 3] = newCanvas(pyr[i * 4 + 3].width, pyr[i * 4 + 3].height, pyr[i * 4 + 3].getSubimage(0, 0, pyr[i * 4 + 3].width, pyr[i * 4 + 3].height) );
        }
        return ret;
    }

    private static <T> Canvas<T> newCanvas(Canvas<T> old, int width, int height, T data) {
        return old.createCanvas(width, height, data);
    }

    private static <T> Canvas<T> newCanvas(Canvas<T> old, int width, int height) {
        return old.createCanvas(width, height);
    }

    @SuppressWarnings("unchecked")
    private <T> Canvas<T> newCanvas(int width, int height, T imageData) {
        return newCanvas((Canvas<T>) canvas, width, height, imageData);
    }

    @SuppressWarnings("unchecked")
    private <T> Canvas<T> newCanvas(int width, int height) {
        return newCanvas((Canvas<T>)canvas, width, height);
    }

    @SuppressWarnings("SameParameterValue")
    private static int get(int[] array, int index, int defaultValue) {
        if (index >= 0 && index < array.length) {
            return array[index];
        } else {
            return defaultValue;
        }
    }

    public List<Detection> analyze(Canvas<?>[] pyr, Cascade cascade) {
        float scale_x = 1, scale_y = 1;
        int[] dx = new int[]{0, 1, 0, 1};
        int[] dy = new int[]{0, 0, 1, 1};
        List<Detection> seq = new ArrayList<>();
        Classifier[] stage_classifier = cascade.getStageClassifiers().toArray(new Classifier[0]);
        Feature[][] orig_feature_aa = new Feature[stage_classifier.length][];
        for (int j = 0; j < stage_classifier.length; j++) {
            orig_feature_aa[j] = stage_classifier[j].copyFeatures();
        }
        Feature[][] current_feature_aa = new Feature[stage_classifier.length][];
        for (int i = 0; i < scale_upto; i++) {
            int qw = pyr[i * 4 + next * 8].width - (int) Math.floor(cascade.width / 4);
            int qh = pyr[i * 4 + next * 8].height - (int) Math.floor(cascade.height / 4);
            int[] step = new int[]{pyr[i * 4].width * 4, pyr[i * 4 + next * 4].width * 4, pyr[i * 4 + next * 8].width * 4};
            int[] paddings = new int[]{pyr[i * 4].width * 16 - qw * 16,
                    pyr[i * 4 + next * 4].width * 8 - qw * 8,
                    pyr[i * 4 + next * 8].width * 4 - qw * 4};
            for (int j = 0; j < stage_classifier.length; j++) {
                Feature[] orig_feature = orig_feature_aa[j];
                current_feature_aa[j] = new Feature[stage_classifier[j].count];
                for (int k = 0; k < stage_classifier[j].count; k++) {
                    current_feature_aa[j][k] = new Feature(orig_feature[k].size);
                    for (int q = 0; q < orig_feature[k].size; q++) {
                        current_feature_aa[j][k].px[q] = orig_feature[k].px[q] * 4 + orig_feature[k].py[q] * get(step, orig_feature[k].pz[q], 0);
                        current_feature_aa[j][k].pz[q] = orig_feature[k].pz[q];
                        current_feature_aa[j][k].nx[q] = orig_feature[k].nx[q] * 4 + orig_feature[k].ny[q] * get(step, orig_feature[k].nz[q], 0);
                        current_feature_aa[j][k].nz[q] = orig_feature[k].nz[q];
                    }
                }
            }
            for (int q = 0; q < 4; q++) {
                int[] pyri4Data = pyr[i * 4].getRgbaData();
                int[][] u8 = new int[][]{pyri4Data, pyr[i * 4 + next * 4].getRgbaData(), pyr[i * 4 + next * 8 + q].getRgbaData()};
                int[] u8o = new int[]{dx[q] * 8 + dy[q] * pyr[i * 4].width * 8, dx[q] * 4 + dy[q] * pyr[i * 4 + next * 4].width * 4, 0};
                for (int y = 0; y < qh; y++) {
                    for (int x = 0; x < qw; x++) {
                        double sum = 0;
                        boolean flag = true;
                        for (int j = 0; j < stage_classifier.length; j++) {
                            sum = 0;
                            double[] alpha = stage_classifier[j].alpha;
                            Feature[] feature = current_feature_aa[j];
                            for (int k = 0; k < stage_classifier[j].count; k++) {
                                Feature feature_k = feature[k];
                                double pmin;
                                try {
                                    pmin = u8[feature_k.pz[0]][u8o[feature_k.pz[0]] + feature_k.px[0]];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    pmin = Double.NaN;
                                }
                                double nmax;
                                try {
                                    nmax = u8[feature_k.nz[0]][u8o[feature_k.nz[0]] + feature_k.nx[0]];
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    nmax = Double.NaN;
                                }
                                if (pmin <= nmax) {
                                    sum += alpha[k * 2];
                                } else {
                                    boolean shortcut = true;
                                    for (int f = 0; f < feature_k.size; f++) {
                                        if (feature_k.pz[f] >= 0) {
                                            double p;
                                            try {
                                                p = u8[feature_k.pz[f]][u8o[feature_k.pz[f]] + feature_k.px[f]];
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                                p = Double.NaN;
                                            }
                                            if (p < pmin) {
                                                if (p <= nmax) {
                                                    shortcut = false;
                                                    break;
                                                }
                                                pmin = p;
                                            }
                                        }
                                        if (feature_k.nz[f] >= 0) {
                                            double n;
                                            try {
                                                n = u8[feature_k.nz[f]][u8o[feature_k.nz[f]] + feature_k.nx[f]];
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                                n = Double.NaN;
                                            }
                                            if (n > nmax) {
                                                if (pmin <= n) {
                                                    shortcut = false;
                                                    break;
                                                }
                                                nmax = n;
                                            }
                                        }
                                    }
                                    sum += (shortcut) ? alpha[k * 2 + 1] : alpha[k * 2];
                                }
                            }
                            if (sum < stage_classifier[j].threshold) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            seq.add(new Detection((x * 4 + dx[q] * 2) * scale_x,
                                    (y * 4 + dy[q] * 2) * scale_y,
                                    cascade.width * scale_x,
                                    cascade.height * scale_y,
                                    1,
                                    sum));
                        }
                        u8o[0] += 16;
                        u8o[1] += 8;
                        u8o[2] += 4;
                    }
                    u8o[0] += paddings[0];
                    u8o[1] += paddings[1];
                    u8o[2] += paddings[2];
                }
            }
            scale_x *= scale;
            scale_y *= scale;
        }
        return seq;
    }

    public List<Detection> clean(List<Detection> seq) {
        int i, j;
        if (!(min_neighbors > 0))
            return seq;
        else {
            Detections.ArrayGroup result = Detections.array_group(seq, (r1, r2) -> {
                double distance = Math.floor(r1.width * 0.25 + 0.5);
                return r2.x <= r1.x + distance &&
                        r2.x >= r1.x - distance &&
                        r2.y <= r1.y + distance &&
                        r2.y >= r1.y - distance &&
                        r2.width <= Math.floor(r1.width * 1.5 + 0.5) &&
                        Math.floor(r2.width * 1.5 + 0.5) >= r1.width;
            });
            int ncomp = result.cat;
            int[] idx_seq = result.index;
            Detection[] comps = new Detection[ncomp + 1];
            for (i = 0; i < comps.length; i++) {
                comps[i] = new Detection();
            }
            // count number of neighbors
            for(i = 0; i < seq.size(); i++)
            {
                Detection r1 = seq.get(i);
                int idx = idx_seq[i];

                if (comps[idx].neighbors == 0)
                    comps[idx].confidence = r1.confidence;

                ++comps[idx].neighbors;

                comps[idx].x += r1.x;
                comps[idx].y += r1.y;
                comps[idx].width += r1.width;
                comps[idx].height += r1.height;
                comps[idx].confidence = Math.max(comps[idx].confidence, r1.confidence);
            }

            List<Detection> seq2 = new ArrayList<>();
            // calculate average bounding box
            for(i = 0; i < ncomp; i++)
            {
                int n = comps[i].neighbors;
                if (n >= min_neighbors)
                    seq2.add(new Detection((comps[i].x * 2 + n) / (2 * n),
                            (comps[i].y * 2 + n) / (2 * n),
                            (comps[i].width * 2 + n) / (2 * n),
                            (comps[i].height * 2 + n) / (2 * n),
                            comps[i].neighbors,
                            comps[i].confidence));
            }

            List<Detection> result_seq = new ArrayList<>();
            // filter out small face rectangles inside large face rectangles
            for(i = 0; i < seq2.size(); i++)
            {
                Detection r1 = seq2.get(i);
                boolean flag = true;
                for(j = 0; j < seq2.size(); j++)
                {
                    Detection r2 = seq2.get(j);
                    double distance = Math.floor(r2.width * 0.25 + 0.5);

                    if(i != j &&
                            r1.x >= r2.x - distance &&
                            r1.y >= r2.y - distance &&
                            r1.x + r1.width <= r2.x + r2.width + distance &&
                            r1.y + r1.height <= r2.y + r2.height + distance &&
                            (r2.neighbors > Math.max(3, r1.neighbors) || r1.neighbors < 3))
                    {
                        flag = false;
                        break;
                    }
                }

                if(flag)
                    result_seq.add(r1);
            }
            return result_seq;
        }
    }

}
