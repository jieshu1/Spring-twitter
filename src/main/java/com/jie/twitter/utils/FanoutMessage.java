package com.jie.twitter.utils;

import com.jie.twitter.entity.Newsfeed;
import com.jie.twitter.entity.User;
import org.springframework.core.style.ToStringCreator;

import java.util.List;

public class FanoutMessage {
    private List<Newsfeed> newsfeedsList;
    private int batchSize;
    private int taskSize;

    public FanoutMessage(List<Newsfeed> newsfeedsList, int batchSize, int taskSize) {
        this.newsfeedsList = newsfeedsList;
        this.batchSize = batchSize;
        this.taskSize = taskSize;
    }

    public int getBatchSize() {
        return batchSize;
    }
    public void setBatchSize(int batchSize){
        this.batchSize = batchSize;
    }

    public int getTaskSize(){
        return taskSize;
    }

    public void setTaskSize(int taskSize) {
        this.taskSize = taskSize;
    }

    public List<Newsfeed> getNewsfeedsList(){
        return newsfeedsList;
    }

    public void setNewsfeedsList(List<Newsfeed> newsfeedsList) {
        this.newsfeedsList = newsfeedsList;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("batchSize", this.batchSize)
                .append("taskSize", this.taskSize)
                .append("newsfeedsList", this.newsfeedsList.toString())
                .toString();
    }

}
