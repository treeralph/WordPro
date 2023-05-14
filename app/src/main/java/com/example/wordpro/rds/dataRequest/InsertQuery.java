package com.example.wordpro.rds.dataRequest;

public class InsertQuery {

    Builder builder;

    public InsertQuery(Builder builder){
        this.builder = builder;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    public static class Builder{

    }

    public static class TeamsBuilder extends Builder{
        private String team_name;
        private String nicknames;
        private String path;

        public TeamsBuilder(String team_name, String nicknames, String path) {
            this.team_name = team_name;
            this.nicknames = nicknames;
            this.path = path;
        }

        @Override
        public String toString() {
            return "insert into teams(team_name, nicknames, path) values('" +
                    team_name + "', '" +
                    nicknames + "', '" +
                    path + "')";
        }
    }

    public static class UsersBuilder extends Builder {
        private String nickname;
        private String device_token;

        public UsersBuilder(String nickname, String device_token) {
            this.nickname = nickname;
            this.device_token = device_token;
        }

        @Override
        public String toString() {
            return "insert into users(nickname, device_token) values('" +
                    nickname + "', '" +
                    device_token + "')";
        }
    }

    public static class SentencesBuilder extends Builder{
        private String sentence;
        private String word;
        private String word_index;
        private String cheat_sheet;
        private String path;
        private String uid;
        private String permission;
        private String team_identifier;
        private boolean isPermission = false;

        public SentencesBuilder(String sentence, String word, String word_index, String cheat_sheet, String path, String uid) {
            this.sentence = sentence;
            this.word = word;
            this.word_index = word_index;
            this.cheat_sheet = cheat_sheet;
            this.path = path;
            this.uid = uid;
        }

        public SentencesBuilder(String sentence, String word, String word_index, String cheat_sheet, String path, String uid, String permission, String team_identifier) {
            this.sentence = sentence;
            this.word = word;
            this.word_index = word_index;
            this.cheat_sheet = cheat_sheet;
            this.path = path;
            this.uid = uid;
            this.team_identifier = team_identifier;
            this.permission = permission;
            isPermission = true;
        }

        @Override
        public String toString() {
            if(isPermission){
                return "insert into sentences(sentence, word, word_index, cheat_sheet, path, uid, permission, team_identifier) values('" +
                        sentence + "', '" +
                        word + "', '" +
                        word_index + "', '" +
                        cheat_sheet + "', '" +
                        path + "', '" +
                        uid + "', '" +
                        permission + "', '" +
                        team_identifier + "')";
            }else{
                return "insert into sentences(sentence, word, word_index, cheat_sheet, path, uid) values('" +
                        sentence + "', '" +
                        word + "', '" +
                        word_index + "', '" +
                        cheat_sheet + "', '" +
                        path + "', '" +
                        uid + "')";
            }
        }
    }
}
