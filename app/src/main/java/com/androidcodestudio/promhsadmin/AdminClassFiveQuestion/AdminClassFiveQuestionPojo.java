package com.androidcodestudio.promhsadmin.AdminClassFiveQuestion;

public class AdminClassFiveQuestionPojo {
    private String id,question,op_one,op_two,op_three,op_four,correct_ans;
    private int set;

    public AdminClassFiveQuestionPojo(String id, String question, String op_one, String op_two, String op_three, String op_four, String correct_ans, int set) {
        this.id = id;
        this.question = question;
        this.op_one = op_one;
        this.op_two = op_two;
        this.op_three = op_three;
        this.op_four = op_four;
        this.correct_ans = correct_ans;
        this.set = set;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOp_one() {
        return op_one;
    }

    public void setOp_one(String op_one) {
        this.op_one = op_one;
    }

    public String getOp_two() {
        return op_two;
    }

    public void setOp_two(String op_two) {
        this.op_two = op_two;
    }

    public String getOp_three() {
        return op_three;
    }

    public void setOp_three(String op_three) {
        this.op_three = op_three;
    }

    public String getOp_four() {
        return op_four;
    }

    public void setOp_four(String op_four) {
        this.op_four = op_four;
    }

    public String getCorrect_ans() {
        return correct_ans;
    }

    public void setCorrect_ans(String correct_ans) {
        this.correct_ans = correct_ans;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
