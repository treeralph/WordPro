package com.example.wordpro.rds.dataRequest;

public class UpdateQuery {

    private String tableName;
    private String columnName;
    private String columnNewValue;
    private String optionField;
    private String option;

    private boolean isOption = false;

    public UpdateQuery(Builder builder){
        this.tableName = builder.tableName;
        this.columnName = builder.columnName;
        this.columnNewValue = builder.columnNewValue;
        this.optionField = builder.optionField;
        this.option = builder.option;
        if(builder.isOptionField && builder.isOption){
            this.isOption = true;
        }
    }

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public String toString() {
        if(this.isOption){
            return "update " + tableName + " set " + columnName + "=" + columnNewValue + " where " + optionField + "=" + option;
        }else{
            return "update " + tableName + " set " + columnName + "=" + columnNewValue;
        }
    }

    public static class Builder{

        private String tableName;
        private String columnName;
        private String columnNewValue;
        private String optionField;
        private String option;

        private boolean isOptionField = false;
        private boolean isOption = false;

        public Builder(){

        }

        public Builder tableName(String tableName){
            this.tableName = tableName;
            return this;
        }
        public Builder columnName(String columnName){
            this.columnName = columnName;
            return this;
        }
        public Builder columnNewValue(String columnNewValue){
            this.columnNewValue = columnNewValue;
            return this;
        }
        public Builder optionField(String optionField){
            this.optionField = optionField;
            this.isOptionField = true;
            return this;
        }
        public Builder option(String option){
            this.option = option;
            this.isOption = true;
            return this;
        }
        public UpdateQuery build(){
            return new UpdateQuery(this);
        }
    }
}
