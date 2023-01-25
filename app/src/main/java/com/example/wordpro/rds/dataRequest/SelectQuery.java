package com.example.wordpro.rds.dataRequest;

import org.checkerframework.checker.nullness.qual.NonNull;

public class SelectQuery {

    private String tableName;
    private String optionField;
    private String option;

    private boolean isOption = false;

    public SelectQuery(Builder builder) throws Exception{
        this.tableName = builder.tableName;

        if(builder.isOptionField && builder.isOption){
            isOption = true;
            this.optionField = builder.optionField;
            this.option = builder.option;
        }else{
            if(builder.isOptionField || builder.isOption){
                throw new Exception("option count error");
            }else{
                // non option fine
            }
        }
    }

    @Override
    public String toString(){
        if(isOption){ // isOption == true
            return "select * from " + tableName + " where " + optionField + " = " + option;
        }else{ // isOption == false
            return "select * from " + tableName;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private String tableName;
        private String optionField;
        private String option;

        private boolean isOptionField = false;
        private boolean isOption = false;

        public Builder() {
        }

        public Builder tableName(@NonNull String tableName){
            this.tableName = tableName;
            return this;
        }

        public Builder optionField(@NonNull String optionField){
            this.optionField = optionField;
            isOptionField = true;
            return this;
        }

        public Builder option(@NonNull String option){
            this.option = option;
            isOption = true;
            return this;
        }

        public SelectQuery build() throws Exception {
            return new SelectQuery(this);
        }
    }
}
