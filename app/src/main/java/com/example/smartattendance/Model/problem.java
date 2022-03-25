package com.example.smartattendance.Model;

public class problem {

    String problemTitle;
    int  vote;

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemStatement(String problemStatement) {
        this.problemTitle = problemStatement;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public problem() {
    }

    public problem(String problemStatement, int vote) {
        this.problemTitle = problemStatement;
        this.vote = vote;
    }
}
