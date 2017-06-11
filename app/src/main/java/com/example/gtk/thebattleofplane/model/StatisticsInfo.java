package com.example.gtk.thebattleofplane.model;

/**
 * Created by gutia on 2017-06-11.
 */

public class StatisticsInfo {
    private Integer totalScore;
    private Integer enemyNumber;

    public StatisticsInfo() {
        this.totalScore = 0;
        this.enemyNumber = 0;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getEnemyNumber() {
        return enemyNumber;
    }

    public void setEnemyNumber(Integer enemyNumber) {
        this.enemyNumber = enemyNumber;
    }
}
