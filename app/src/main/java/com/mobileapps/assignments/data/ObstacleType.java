package com.mobileapps.assignments.data;

import com.mobileapps.assignments.R;

public enum ObstacleType {
    DUFF(R.drawable.img_obstacle_duff, 9),
    DONUT(R.drawable.img_obstacle_donut, 1);

    private final int resourceId;

    private final int obstacleWeight;

    ObstacleType(int resourceId, int obstacleWeight) {
        this.resourceId = resourceId;

        this.obstacleWeight = obstacleWeight;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getObstacleWeight() {
        return obstacleWeight;
    }

    public static ObstacleType getRandomObstacleType() {
        int totalWeight = 0;

        for (ObstacleType obstacleType : ObstacleType.values())
            totalWeight += obstacleType.getObstacleWeight();

        int random = (int)(Math.random() * totalWeight);

        for (ObstacleType obstacleType : ObstacleType.values()) {
            random -= obstacleType.getObstacleWeight();
            if (random < 0)
                return obstacleType;
        }
        return null;
    }

}
