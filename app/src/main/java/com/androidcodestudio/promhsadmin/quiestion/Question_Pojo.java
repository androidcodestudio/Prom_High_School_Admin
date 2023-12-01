package com.androidcodestudio.promhsadmin.quiestion;

public class Question_Pojo {

    private int setNo;
    private String question;
    private String op_one;
    private String op_two;
    private String op_three;
    private String op_four;
    private String correct_ans;


    public Question_Pojo(){
        //for fireBase
    }

    public Question_Pojo(String question, String op_one, String op_two, String op_three, String op_four, String correct_ans,int setNo) {
        this.question = question;
        this.op_one = op_one;
        this.op_two = op_two;
        this.op_three = op_three;
        this.op_four = op_four;
        this.correct_ans = correct_ans;
        this.setNo = setNo;
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

    public int getSetNo() {
        return setNo;
    }
public void setSetNo(int setNo) {
        this.setNo = setNo;
    }
}
