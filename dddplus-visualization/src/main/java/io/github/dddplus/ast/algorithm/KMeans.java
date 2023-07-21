/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * KMeans cluster java method names.
 */
public class KMeans {
    private static final int NUM_CLUSTERS = 3;
    private static final int MAX_ITERATIONS = 100;
    private static final double THRESHOLD = 0.001;

    private static Random random = new Random();

    private int numClusters; // 簇的数量
    private int maxIterations; // 最大迭代次数
    private double threshold; // 收敛阈值

    public KMeans() {
        this(NUM_CLUSTERS, MAX_ITERATIONS, THRESHOLD);
    }

    public KMeans(int numClusters, int maxIterations, double threshold) {
        this.numClusters = numClusters;
        this.maxIterations = maxIterations;
        this.threshold = threshold;
    }

    public List<List<String>> cluster(List<double[]> vectors, List<String> labels, Map<String, double[]> vectorMap) {
        // 随机初始化簇中心
        List<double[]> centroids = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            centroids.add(vectors.get(random.nextInt(vectors.size())));
        }

        // 迭代聚类
        List<List<String>> clusters = new ArrayList<>();
        for (int i = 0; i < maxIterations; i++) {
            // 分配样本到最近的簇
            clusters.clear();
            for (int j = 0; j < numClusters; j++) {
                clusters.add(new ArrayList<>());
            }
            for (int j = 0; j < vectors.size(); j++) {
                double[] vector = vectors.get(j);
                double minDistance = Double.MAX_VALUE;
                int nearestCluster = -1;
                for (int k = 0; k < numClusters; k++) {
                    double[] centroid = centroids.get(k);
                    double distance = euclideanDistance(vector, centroid);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestCluster = k;
                    }
                }
                clusters.get(nearestCluster).add(labels.get(j));
            }

            // 更新簇中心
            boolean converged = true;
            for (int j = 0; j < numClusters; j++) {
                double[] newCentroid = calculateCentroid(clusters.get(j), vectorMap);
                double[] oldCentroid = centroids.get(j);
                if (euclideanDistance(newCentroid, oldCentroid) > threshold) {
                    centroids.set(j, newCentroid);
                    converged = false;
                }
            }

            // 如果簇中心未发生变化，则结束迭代
            if (converged) {
                break;
            }
        }

        return clusters;
    }

    private double[] calculateCentroid(List<String> cluster, Map<String, double[]> vectorMap) {
        int dimension = vectorMap.values().iterator().next().length;
        double[] centroid = new double[dimension];
        for (String label : cluster) {
            double[] vector = vectorMap.get(label);
            for (int i = 0; i < dimension; i++) {
                centroid[i] += vector[i];
            }
        }
        for (int i = 0; i < dimension; i++) {
            centroid[i] /= cluster.size();
        }
        return centroid;
    }

    private double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }
}
