package io.github.dddplus.ast.algorithm;

import org.junit.jupiter.api.Test;

import java.util.*;

class KMeansTest {

    @Test
    void cluster() {
        List<String> methodNames = new ArrayList<>(Arrays.asList(
                "getUserName", "setUserName", "getUserAge", "setUserAge", "getUserEmail", "setUserEmail",
                "getAccountBalance", "setAccountBalance", "deposit", "withdraw", "transferMoney",
                "createUser", "deleteUser", "updateUser", "getUserById", "getUsersByAge",
                "getUserByEmail", "getUsersByBalance", "getTransactionHistory", "generateReport"
        ));
        List<double[]> vectors = new ArrayList<>();
        Map<String, double[]> vectorMap = new HashMap<>();

        // 将方法名转换为向量
        int maxLength = -1;
        for (String methodName : methodNames) {
            if (methodName.length() > maxLength) {
                maxLength = methodName.length();
            }
        }
        for (String methodName : methodNames) {
            double[] vector = new double[maxLength];
            for (int i = 0; i < methodName.length(); i++) {
                vector[i] = methodName.charAt(i); // 向量值为ascii
            }
            // padding with 0
            for (int i = methodName.length(); i < maxLength; i++) {
                vector[i] = 0;
            }
            vectors.add(vector);
            vectorMap.put(methodName, vector);
        }

        // 使用K-Means算法进行聚类
        KMeans kMeans = new KMeans();
        List<List<String>> clusters = kMeans.cluster(vectors, methodNames, vectorMap);

        // 输出聚类结果
        for (int i = 0; i < clusters.size(); i++) {
            System.out.println("Cluster " + (i + 1) + ": " + clusters.get(i));
        }
    }

}