package com.example.wordpro.rds.dataRequest;

import org.checkerframework.checker.nullness.qual.NonNull;

public class DeleteQuery {

    private String tableName;
    private String optionField;
    private String option;

    private boolean isOption = false;

    public DeleteQuery(Builder builder) throws Exception{
        this.tableName = builder.tableName;

        if(builder.isOptionField && builder.isOption){
            isOption = true;
            this.optionField = builder.optionField;
            this.option = builder.option;
        }else{
            if(!builder.isOptionField && !builder.isOption){
                if(!builder.sudo){
                    throw new Exception("permission denied: delete from " + tableName);
                }
            }else{
                throw new Exception("option count error");
            }
        }
    }

    @Override
    public String toString(){
        if(isOption){ // isOption == true
            return "delete from " + tableName + " where " + optionField + " = " + option;
        }else{ // isOption == false
            return "delete from " + tableName;
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
        private boolean sudo = false;

        public Builder() {
        }

        public Builder tableName(String tableName){
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

        public Builder sudo(@NonNull boolean sudo){
            this.sudo = sudo;
            return this;
        }

        public DeleteQuery build() throws Exception {
            return new DeleteQuery(this);
        }
    }
}
